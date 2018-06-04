package edu.kit.wbk.smartfantaapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.vuzix.sdk.barcode.Location;
import com.vuzix.sdk.barcode.ScanResult;

public class OverlayView extends View {
    private ScanResult[] scanResults;

    Paint paint;
    public OverlayView(Context context) {
        super(context);
        this.init();
    }

    public OverlayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public OverlayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    public OverlayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init();
    }

    private void init() {
        scanResults = new ScanResult[0];
        paint = new Paint();
        paint.setColor(Color.GREEN);
    }

    public ScanResult[] getScanResults() {
        return scanResults;
    }

    public void setScanResults(ScanResult[] scanResults) {
        this.scanResults = scanResults;
        this.invalidate();
    }

    private void drawLine(Canvas canvas, PointF location1, PointF location2) {
        float density = getResources().getDisplayMetrics().scaledDensity;
        float factor = 0.333f;
        canvas.drawLine(factor * location1.x, factor * location1.y, factor * location2.x, factor * location2.y, paint);

        // canvas.drawLine(location.getPoint1().x, location.getPoint1().y, location.getPoint2().x, location.getPoint2().y, paint);
        // canvas.drawLine(location.getPoint2().x, location.getPoint2().y, location.getPoint3().x, location.getPoint3().y, paint);
        // canvas.drawLine(location.getPoint3().x, location.getPoint3().y, location.getPoint4().x, location.getPoint4().y, paint);
        // canvas.drawLine(location.getPoint4().x, location.getPoint4().y, location.getPoint1().x, location.getPoint1().y, paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // paint.setColor(Color.RED);
        // canvas.drawLine(0, 0, 100, 100, paint);
        // paint.setColor(Color.GREEN);
        // paint.setStyle(Paint.Style.FILL);
        for(ScanResult result : scanResults) {
            Location location = result.getLocation();
            // drawLine(canvas, location.getPoint1(), location.getPoint2());
            // drawLine(canvas, location.getPoint2(), location.getPoint3());
            // drawLine(canvas, location.getPoint3(), location.getPoint4());
            // drawLine(canvas, location.getPoint4(), location.getPoint1());
            Path p = new Path();
            p.reset();
            float factor = 0.33333f;
            p.moveTo(factor * location.getPoint1().x, factor * location.getPoint1().y);
            p.lineTo(factor * location.getPoint2().x, factor * location.getPoint2().y);
            p.lineTo(factor * location.getPoint3().x, factor * location.getPoint3().y);
            p.lineTo(factor * location.getPoint4().x, factor * location.getPoint4().y);
            p.lineTo(factor * location.getPoint1().x, factor * location.getPoint1().y);
            canvas.drawPath(p, paint);
        }
    }
}
