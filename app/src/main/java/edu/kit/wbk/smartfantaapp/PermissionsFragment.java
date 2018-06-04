/***************************************************************************************
 Copyright (c) 2018, Vuzix Corporation
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:

 *  Redistributions of source code must retain the above copyright
 notice, this list of conditions and the following disclaimer.

 *  Redistributions in binary form must reproduce the above copyright
 notice, this list of conditions and the following disclaimer in the
 documentation and/or other materials provided with the distribution.

 *  Neither the name of Vuzix Corporation nor the names of
 its contributors may be used to endorse or promote products derived
 from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 **************************************************************************************/
package edu.kit.wbk.smartfantaapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

/**
 * A fragment to encapsulate the run-time permissions
 */
@TargetApi(23)
public class PermissionsFragment extends Fragment {

    private static final int REQUEST_CODE_PERMISSIONS = 0;

    private Listener listener;

    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE};
    private int permissionIndex = 0;

    /**
     * One-time initialization. Sets up the view
     * @param savedInstanceState - we have no saved state. Just pass through to superclass
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        requestPermissions();
    }

    /**
     * Make the permissions request to the system
     */
    private void requestPermissions() {
        while(permissionIndex < permissions.length && getContext().checkSelfPermission(permissions[permissionIndex]) == PackageManager.PERMISSION_GRANTED) {
            permissionIndex++;
        }
        if(permissionIndex == permissions.length) {
            permissionsGranted();
        } else {
            requestPermissions(new String[] {permissions[permissionIndex]}, REQUEST_CODE_PERMISSIONS);
        }
    }

    /**
     * Called upon the permissions being granted. Notifies the permission listener.
     */
    private synchronized void permissionsGranted() {
        if (listener != null) {
            listener.permissionsGranted();
        }
    }


    /**
     * Sets the listener on which we will call permissionsGranted()
     * @param listener pointer to the class implementing the PermissionsFragment.Listener
     */
    public synchronized void setListener(Listener listener) {
        this.listener = listener;
    }

    /**
     * Required interface for any activity that requests a run-time permission
     *
     * @see <a href="https://developer.android.com/training/permissions/requesting.html">https://developer.android.com/training/permissions/requesting.html</a>
     * @param requestCode int: The request code passed in requestPermissions(android.app.Activity, String[], int)
     * @param permissions String: The requested permissions. Never null.
     * @param grantResults int: The grant results for the corresponding permissions which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSIONS:
                if (permissions.length == 1) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        requestPermissions();  // Permission was just granted by the user.
                    } else if (shouldShowRequestPermissionRationale(permissions[permissionIndex])) {
                        requestPermissions();  // Ask for permission again
                    } else {
                        // Permission was denied. Give the user a hint, and exit
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.fromParts("package", getContext().getPackageName(), null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        getActivity().finish();
                        Toast.makeText(getContext(), R.string.grant_camera_permission, Toast.LENGTH_LONG).show();
                    }
                }
                return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Define the interface of a permission fragment listener
     */
    interface Listener {
        void permissionsGranted();
    }
}
