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

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vuzix.sdk.barcode.ScanResult;

/**
 * A fragment to show the result of the barcode scan
 */
public class ScanResultFragment extends Fragment {

    public static final String ARG_BITMAP = "bitmap";
    public static final String ARG_SCAN_RESULT = "scan_result";

    ///**
    // * Inflate the correct layout upon creation
    // *
    // * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment,
    // * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
    // *                  The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
    // * @param savedInstanceState  If non-null, this fragment is being re-constructed from a previous saved state as given here.
    // * @return - Returns the View for the fragment's UI, or null.
    // */
    //@Override
    //public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    //    return inflater.inflate(R.layout.fragment_result, container, false);
    //}

    // /**
    //  * Once our view is created, we will show the image with the scan result
    //  *
    //  * @param view - The new view
    //  * @param savedInstanceState - required argument that we ignore
    //  */
    // @Override
    // public void onViewCreated(View view, Bundle savedInstanceState) {
    //     ScanResultImageView bitmap = (ScanResultImageView)view.findViewById(R.id.bitmap);
    //     TextView text = (TextView)view.findViewById(R.id.text);
    //     // The arguments Bundle gives us the bitmap that was taken upon recognition of a barcode, and
    //     // the text extracted from the barcode within the image
    //     Bundle args = getArguments();
    //     if (args != null) {
    //         ScanResult scanResult = args.getParcelable(ARG_SCAN_RESULT);
    //         bitmap.setImageBitmap((Bitmap)args.getParcelable(ARG_BITMAP));
    //         bitmap.setLocation(scanResult.getLocation());
    //         text.setText(scanResult.getText());
    //     }
    // }
}
