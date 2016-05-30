package com.example.aleksander.hexafield.library;

import android.graphics.PointF;

import java.util.ArrayList;

/**
 * Created by aleksander on 26.10.15.
 */
public class LayoutHelper {
    public LayoutHelper(Orientation orientation, PointF size, PointF origin) {
        this.orientation = orientation;
        this.size = size;
        this.origin = origin;
    }

    public final Orientation orientation;
    public final PointF size;
    public final PointF origin;
    static public Orientation pointy = new Orientation(Math.sqrt(3.0), Math.sqrt(3.0) / 2.0, 0.0, 3.0 / 2.0, Math.sqrt(3.0) / 3.0, -1.0 / 3.0, 0.0, 2.0 / 3.0, 0.5);

    static public PointF hexToPixel(LayoutHelper layoutHelper, Hex h) {
        Orientation M = layoutHelper.orientation;
        PointF size = layoutHelper.size;
        PointF origin = layoutHelper.origin;
        float x = (M.f0 * h.q + M.f1 * h.r) * size.x;
        float y = (M.f2 * h.q + M.f3 * h.r) * size.y;
        return new PointF(x + origin.x, y + origin.y);
    }


    static public FractionalHex pixelToHex(LayoutHelper layoutHelper, PointF p) {
        Orientation M = layoutHelper.orientation;
        PointF size = layoutHelper.size;
        PointF origin = layoutHelper.origin;
        PointF pt = new PointF((p.x - origin.x) / size.x, (p.y - origin.y) / size.y);
        double q = M.b0 * pt.x + M.b1 * pt.y;
        double r = M.b2 * pt.x + M.b3 * pt.y;
        return new FractionalHex(q, r, -q - r);
    }

    static public PointF hexCornerOffset(LayoutHelper layoutHelper, int corner, float scale) {
        Orientation M = layoutHelper.orientation;
        PointF size = layoutHelper.size;
        double angle = 2.0 * Math.PI * (corner + M.start_angle) / 6;
        return new PointF((float) (size.x * scale * Math.cos(angle)), (float) (size.y * scale * Math.sin(angle)));
    }


    static public ArrayList<PointF> polygonCorners(LayoutHelper layoutHelper, Hex h, float scale) {
        ArrayList<PointF> corners = new ArrayList<PointF>() {{
        }};
        PointF center = LayoutHelper.hexToPixel(layoutHelper, h);
        for (int i = 0; i < 6; i++) {
            PointF offset = LayoutHelper.hexCornerOffset(layoutHelper, i, scale);
            corners.add(new PointF(center.x + offset.x, center.y + offset.y));
        }
        return corners;
    }
}
