package edu.kit.wbk.smartfantaapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.vuzix.sdk.barcode.ScanResult;
import com.vuzix.sdk.barcode.ScannerFragment;
import com.vuzix.sdk.barcode.ScanningRect;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import edu.kit.wbk.smartfantaapp.data.Order;
import edu.kit.wbk.smartfantaapp.data.PickingProduct;
import edu.kit.wbk.smartfantaapp.data.QrCode;
import edu.kit.wbk.smartfantaapp.data.Tracker;

public class MainActivity extends Activity implements PermissionsFragment.Listener {
    private static final String TAG_PERMISSIONS_FRAGMENT = "permissions";
    public static final int REQUEST_CODE = 1;
    protected static final String PICKED_PRODUCTS = "picked products";

    public  List<Order> orderQueue = new LinkedList<>();
    private TextView infoView;
    private OverlayView overlayView;
    private ScannerFragment.Listener mScannerListener;
    private QrCode[] scanResults = {};
    private Intent intent;
    private boolean leftShelf = false;

   // private List<Order> orderQueue = new LinkedList<>();
    private Order currentOrder;
    private HashMap<String, PickingProduct> products = new HashMap<>();
    private HashMap<String, String> groups;
    {
        groups = new HashMap<>();
        groups.put("Regal 1", "1");
        groups.put("Regal 2", "4");
        groups.put("Regal 3", "3");
        groups.put("Regal 4", "4");
    }

    public static Activity subActivity;

    private long lastScannedResult = 0;
    private boolean shouldTrack = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // This is a best practice on the M300. Once the activity is started, the user will likely
        // look straight down to scan a barcode. Allow left and right eye operation, but lock it
        // in once started
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Since the Vuzix M300 API is level 23, always use runtime permissions
        PermissionsFragment permissionsFragment = (PermissionsFragment)getFragmentManager().findFragmentByTag(TAG_PERMISSIONS_FRAGMENT);
        if (permissionsFragment == null) {
            permissionsFragment = new PermissionsFragment();
            getFragmentManager().beginTransaction().add(permissionsFragment, TAG_PERMISSIONS_FRAGMENT).commit();
        }
        // Register as a PermissionsFragment.Listener so our permissionsGranted() is called
        permissionsFragment.setListener(this);

        // Hide the instructions until we have permission granted
        infoView = (TextView) findViewById(R.id.scan_instructions);

        overlayView = (OverlayView) findViewById(R.id.overlayView);
        this.overlayView.setZ(100);
        currentOrder = null;
        intent = null;

        createScannerListener();
        updateFromGroup();

        final Handler handler = new Handler();
        Timer timer = new Timer(false);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(System.currentTimeMillis() - lastScannedResult > 1500 && scanResults.length != 0) {
                            clearScanResult();
                        }
                    }
                });
            }
        };

        timer.scheduleAtFixedRate(timerTask, 200, 200);
    }

    /**
     * Called upon permissions being granted. This is the only way we show the scanner with API 23
     */
    @Override
    public void permissionsGranted() {
        showScanner();
    }

    private void clearScanResult() {
        this.onScanFragmentScanResult(null, new ScanResult[0]);
    }

    /**
     * This callback gives us the scan result.  This is relayed through mScannerListener.onScanResult
     *
     * This sample calls a helper class to display the result to the screen
     *
     * @param bitmap -  the bitmap in which barcodes were found
     * @param results -  an array of ScanResult
     */
    private void onScanFragmentScanResult(Bitmap bitmap, ScanResult[] results) {
        this.lastScannedResult = System.currentTimeMillis();
        this.scanResults = processResults(results);
        this.overlayView.setScanResults(this.scanResults);
    }

    private QrCode[] processResults(ScanResult[] results) {
        final float scale = 1.f / 3;
        QrCode[] codes = new QrCode[results.length];
        for(int i = 0; i < codes.length; i++) {
            QrCode code = new QrCode(results[i].getLocation(), results[i].getText().trim());
            code.scalePoints(scale);
            PickingProduct product = products.get(code.getCode());
            code.setRequestedAmount(product == null ? "0" : product.getAmount());
            String group = groups.get(code.getCode().trim());
            if(group != null) {
                code.setRequestedAmount(code.getCode());
                if(group.equals("4")) {
                    code.setRequestedAmount("Regal 4");
                }
                this.overlayView.setCurrentGroup(group);
                updateFromGroup();
            }
            codes[i] = code;
        }
        return codes;
    }

    /**
     * This callback gives us scan errors. This is relayed through mScannerListener.onError
     *
     * At a minimum the scanner fragment must be removed from the activity. This sample closes
     * the entire activity, since it has no other functionality
     */
    private void onScanFragmentError() {
        finish();
        Toast.makeText(this, R.string.scanner_error_message, Toast.LENGTH_LONG).show();
    }

    private void createScannerListener() {
        try {
            /**
             * This is a simple wrapper class.
             *
             * We do this rather than having our MainActivity directly implement
             * ScannerFragment.Listener so we may gracefully catch the NoClassDefFoundError
             * if we are not running on an M300.
             */
            class OurScannerListener implements ScannerFragment.Listener {
                @Override
                public void onScanResult(Bitmap bitmap, ScanResult[] results) {
                    onScanFragmentScanResult(bitmap, results);
                }

                @Override
                public void onError() {
                    onScanFragmentError();
                }
            }

            mScannerListener = new OurScannerListener();

        } catch (NoClassDefFoundError e) {
            // We get this exception if the SDK stubs against which we compiled cannot be resolved
            // at runtime. This occurs if the code is not being run on an M300 supporting the voice
            // SDK
            Toast.makeText(this, R.string.only_on_m300, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private boolean receivedOrder (Order ord) {
        return orderQueue.add(ord);
    }

    /*public List<Order> getOrderQueue() {
        return orderQueue;
    }*/

    private boolean takeOrder() {
        if (orderQueue.isEmpty()) {
            return false;
        }
        currentOrder = orderQueue.get(0);
        this.products = currentOrder.getProductsToPick();
        // this.overlayView.setProducts(this.products.values());
        this.overlayView.setCurrentGroup("");
        updateFromGroup();
        return true;
    }

    private void updateFromGroup() {
        String currentGroup = this.overlayView.getCurrentGroup();
        StringBuilder productStrings = new StringBuilder();
        for (PickingProduct product : products.values()) {
            if(!currentGroup.isEmpty() && !currentGroup.equals(product.getGroup())) {
                continue;
            }
            productStrings.append(product.getName()).append(": ").append(product.getAmount()).append("\n");
        }
        infoView.setText(productStrings.toString());
    }

    /**
     * Shows the scanner fragment in our activity
     */
    private void showScanner() {
        ScannerFragment scannerFragment = new ScannerFragment();
        Bundle args = new Bundle();
        // A rectangle must be defined for the scanner to function. This is a recommended default.
        args.putParcelable(ScannerFragment.ARG_SCANNING_RECT, new ScanningRect(1, 1));
        // Zoom-in mode is recommended for scanning hand-held barcodes
        args.putBoolean(ScannerFragment.ARG_ZOOM_IN_MODE, true );
        scannerFragment.setArguments(args);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, scannerFragment).commit();
        scannerFragment.setListener(mScannerListener);     // Required to get scan results
        new Tracker(this);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            if (currentOrder == null) {
                takeOrder();
            } else {
                intent = new Intent(this, RouteActivity.class);
                intent.putExtra(Order.ORDER, currentOrder);
                startActivityForResult(intent, REQUEST_CODE);

            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
            receivedOrder(Order.getOrderOne());
        }

        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_CODE) {
            // Make sure the request was successful
            //if (resultCode == RESULT_OK) {
                orderQueue.remove(currentOrder);
                this.products.clear();
                //overlayView.setProducts(this.products.values());
                currentOrder = null;
                updateFromGroup();
                Log.i("Main", "Back and done");
            //}
        }

        subActivity = null;
    }

    public void receivedTrackerInfo(String info) {
        // TODO do stuff with info

        if (info.equals(Tracker.PLACE_ORDER)) {
            receivedOrder(Order.getRefillOrder());
        } else if (info.equals(Tracker.SHELF)) {
            if(subActivity != null && leftShelf) {
                leftShelf = false;
                subActivity.setResult(RESULT_OK);
                subActivity.finish();
            }
        }

        if (info.equals("Presse 2") || info.equals("Presse 4")) {
            leftShelf = true;
        }

        if(this.shouldTrack) {
            new Tracker(this);
        }
    }
}
