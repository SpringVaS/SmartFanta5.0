package edu.kit.wbk.smartfantaapp;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.vuzix.sdk.barcode.ScanResult;
import com.vuzix.sdk.barcode.ScannerFragment;
import com.vuzix.sdk.barcode.ScanningRect;

import java.io.IOException;

public class MainActivity extends Activity {

    private ScannerFragment.Listener mScannerListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        createScannerListener();
    }

    /**
     * Helper method to show a scan result
     *
     * @param bitmap -  the bitmap in which barcodes were found
     * @param result -  an array of ScanResult
     */
    private void showScanResult(Bitmap bitmap, ScanResult result) {
        // ScanResultFragment scanResultFragment = new ScanResultFragment();
        // Bundle args = new Bundle();
        // args.putParcelable(ScanResultFragment.ARG_BITMAP, bitmap);
        // args.putParcelable(ScanResultFragment.ARG_SCAN_RESULT, result);
        // scanResultFragment.setArguments(args);
        // getFragmentManager().beginTransaction().replace(R.id.fragment_container, scanResultFragment).commit();
        beep();
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
        ScannerFragment scannerFragment = (ScannerFragment)getFragmentManager().findFragmentById(R.id.fragment_container);
        scannerFragment.setListener(null);
        showScanResult(bitmap, results[0]);
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
        showScanner();
    }

    /**
     * Shows the scanner fragment in our activity
     */
    private void showScanner() {
        ScannerFragment scannerFragment = new ScannerFragment();
        Bundle args = new Bundle();
        // A rectangle must be defined for the scanner to function. This is a recommended default.
        args.putParcelable(ScannerFragment.ARG_SCANNING_RECT, new ScanningRect(.6f, .75f));
        // Zoom-in mode is recommended for scanning hand-held barcodes
        args.putBoolean(ScannerFragment.ARG_ZOOM_IN_MODE, true );
        scannerFragment.setArguments(args);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, scannerFragment).commit();
        scannerFragment.setListener(mScannerListener);     // Required to get scan results
    }



    /**
     * A best practice is to give some audible feedback during scan operations. This beeps.
     */
    private void beep() {
        MediaPlayer player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        try {
            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            player.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
            file.close();
            player.setVolume(.1f, .1f);
            player.prepare();
            player.start();
        } catch (IOException e) {
            player.release();
        }
    }
}
