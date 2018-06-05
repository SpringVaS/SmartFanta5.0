package edu.kit.wbk.smartfantaapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.vuzix.sdk.barcode.ScanResult;
import com.vuzix.sdk.barcode.ScannerFragment;
import com.vuzix.sdk.barcode.ScanningRect;

import java.util.HashMap;

import edu.kit.wbk.smartfantaapp.data.Order;
import edu.kit.wbk.smartfantaapp.data.PickingProduct;
import edu.kit.wbk.smartfantaapp.data.QrCode;

public class MainActivity extends Activity implements PermissionsFragment.Listener {
    private static final String TAG_PERMISSIONS_FRAGMENT = "permissions";
    private View infoView;
    private OverlayView overlayView;
    private ScannerFragment.Listener mScannerListener;
    private QrCode[] scanResults;

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
        infoView = findViewById(R.id.scan_instructions);
        infoView.setVisibility(View.GONE);

        overlayView = (OverlayView) findViewById(R.id.overlayView);
        currentOrder = null;

        createScannerListener();

        // Tracker.main();
    }

    /**
     * Called upon permissions being granted. This is the only way we show the scanner with API 23
     */
    @Override
    public void permissionsGranted() {
        showScanner();
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
        this.scanResults = processResults(results);
        this.overlayView.setScanResults(this.scanResults);
        this.overlayView.setProducts(this.products.values());
        this.overlayView.setZ(100);
    }

    private QrCode[] processResults(ScanResult[] results) {
        final float scale = 1.f / 3;
        QrCode[] codes = new QrCode[results.length];
        for(int i = 0; i < codes.length; i++) {
            QrCode code = new QrCode(results[i].getLocation(), results[i].getText());
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

    private boolean receivedOrder () {
        currentOrder = Order.getOrderOne();
        this.products = currentOrder.getProductsToPick();
        this.overlayView.setCurrentGroup("");
        return true;
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
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && currentOrder != null) {
            Intent intent = new Intent(this, RouteActivity.class);
            intent.putExtra(Order.ORDER, currentOrder);
            startActivity(intent);
            return true;
        } else if (keyCode == 22){
            receivedOrder();
        }

        return super.onKeyUp(keyCode, event);
    }
}
