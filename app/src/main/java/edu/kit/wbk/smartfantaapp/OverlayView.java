package edu.kit.wbk.smartfantaapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import edu.kit.wbk.smartfantaapp.data.PickingProduct;
import edu.kit.wbk.smartfantaapp.data.QrCode;

public class OverlayView extends View {
    private QrCode[] scanResults;
    private Collection<PickingProduct> products;
    private String currentGroup = "";

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
        products = new ArrayList<>(0);
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

    public Collection<PickingProduct> getProducts() {
        return products;
    }

    public String getCurrentGroup() {
        return currentGroup;
    }

    public void setCurrentGroup(String currentGroup) {
        this.currentGroup = currentGroup;
        this.invalidate();
    }

    public void setProducts(Collection<PickingProduct> products) {
        this.products = products;
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(QrCode code : scanResults) {
            code.render(canvas, paint);
        }
        paint.setTextSize(24);
        paint.setColor(Color.GREEN);
        int i = 0;
        for (PickingProduct product : products) {
            if(!currentGroup.isEmpty() && !currentGroup.equals(product.getGroup())) {
                continue;
            }
            canvas.drawText(product.getName() + ": " + product.getAmount(), 10, i * 32, paint);
            i++;
        }
    }
}
