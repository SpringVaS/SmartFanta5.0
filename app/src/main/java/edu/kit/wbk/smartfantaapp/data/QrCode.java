package edu.kit.wbk.smartfantaapp.data;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import com.vuzix.sdk.barcode.Location;

public class QrCode {
    private final String code;
    private PointF point1;
    private PointF point2;
    private PointF point3;
    private PointF point4;

    public QrCode(Location location, String code) {
        point1 = location.getPoint1();
        point2 = location.getPoint2();
        point3 = location.getPoint3();
        point4 = location.getPoint4();
        this.code = code;
    }

    public void scalePoints(float scale) {
        point1.x *= scale;
        point1.y *= scale;
        point2.x *= scale;
        point2.y *= scale;
        point3.x *= scale;
        point3.y *= scale;
        point4.x *= scale;
        point4.y *= scale;
    }

    private static void drawLine(Canvas canvas, Paint paint, PointF location1, PointF location2) {
        canvas.drawLine(location1.x, location1.y, location2.x, location2.y, paint);
    }

    public void render(Canvas canvas, Paint paint) {
        drawLine(canvas, paint, point1, point2);
        drawLine(canvas, paint, point2, point3);
        drawLine(canvas, paint, point3, point4);
        drawLine(canvas, paint, point4, point1);
    }
}
