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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.vuzix.sdk.barcode.Location;

/**
 * An image view specifically to draw the barcode to our screen
 */
public class ScanResultImageView extends ImageView {

    private Location location;
    private Paint locationPaint;

    public ScanResultImageView(Context context) {
        this(context, null);
    }

    public ScanResultImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * The constructor taking all available parameters. Called directly, or by other constructors
     *
     * @param context The Context in which we are operating
     * @param attrs - The AttributeSet or null
     * @param defStyleAttr The int style, or zero
     */
    public ScanResultImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAdjustViewBounds(true);

        // Setup a Paint object to draw on the scanned barcode with these attributes
        locationPaint = new Paint();
        locationPaint.setColor(Color.GREEN);
        locationPaint.setStrokeWidth(5);
        locationPaint.setStyle(Paint.Style.STROKE);
        locationPaint.setStrokeCap(Paint.Cap.ROUND);
        locationPaint.setAntiAlias(true);
    }

    /**
     * Accessor to get the location of the barcode within the image
     * @return Location of barcode
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Mutator to set the location of the barcode within the image, and re-draw accordingly
     *
     * @param location  Location object defining the recognized barcode within the image
     */
    public void setLocation(Location location) {
        this.location = location;
        invalidate();
    }

    /**
     * Override this to draw a box around the barcode at the location it was found
     *
     * @param canvas Canvas upon which to draw
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (location != null) {
            Drawable d = getDrawable();
            if (d instanceof BitmapDrawable) {
                Bitmap b = ((BitmapDrawable)d).getBitmap();
                float scale = (float)getWidth() / b.getWidth();
                drawLine(canvas, location.getPoint1(), location.getPoint2(), scale);
                drawLine(canvas, location.getPoint2(), location.getPoint3(), scale);
                drawLine(canvas, location.getPoint3(), location.getPoint4(), scale);
                drawLine(canvas, location.getPoint4(), location.getPoint1(), scale);
            }
        }
    }

    /**
     * Utility to draw a single line onto the canvas
     *
     * @param canvas Canvas upon which to draw
     * @param p1 Point to start the line
     * @param p2 Point to end the line
     * @param scale float by which to scale the line. Converts from high-res bitmap dimensions to Canvas dimensions
     */
    private void drawLine(Canvas canvas, PointF p1, PointF p2, float scale) {
        canvas.drawLine(p1.x * scale, p1.y * scale, p2.x * scale, p2.y * scale, locationPaint);
    }
}
