package edu.kit.wbk.smartfantaapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import edu.kit.wbk.smartfantaapp.data.QrCode;

public class OverlayView extends View {
    private QrCode[] scanResults;

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
        scanResults = new QrCode[0];
        paint = new Paint();
        paint.setColor(Color.GREEN);
    }

    public QrCode[] getScanResults() {
        return scanResults;
    }

    public void setScanResults(QrCode[] scanResults) {
        this.scanResults = scanResults;
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(QrCode code : scanResults) {
            code.render(canvas, paint);
        }
    }
}
