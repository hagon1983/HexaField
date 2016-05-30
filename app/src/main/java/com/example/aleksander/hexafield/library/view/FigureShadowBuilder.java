package com.example.aleksander.hexafield.library.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;

import com.example.aleksander.hexafield.library.Hex;
import com.example.aleksander.hexafield.library.LayoutHelper;
import com.example.aleksander.hexafield.library.model.GameFigure;

import java.util.ArrayList;

/**
 * Created by aleksander on 30.10.15.
 */
public class FigureShadowBuilder extends View.DragShadowBuilder {
    private static final String TAG = "FigureShadowBuilder";
    private Path mPath = new Path();
    private Paint mPaint;
    private RectF bounds;

    public FigureShadowBuilder(LayoutHelper layoutHelper, GameFigure figure) {
        bounds = null;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.YELLOW);

        for (Hex hex : figure.getItems()) {
            ArrayList<PointF> corners = LayoutHelper.polygonCorners(layoutHelper, hex, 0.8f);
            for (int j = 0, numCorners = corners.size(); j < numCorners; j++) {
                PointF corner = corners.get(j);
                Log.i(TAG, "Add corner: " + corner);
                if (j == 0) {
                    mPath.moveTo(corner.x, corner.y);
                    if (bounds == null) {
                        bounds = new RectF(corner.x, corner.y, corner.x, corner.y);
                    } else {
                        bounds.union(corner.x, corner.y);
                    }
                } else {
                    mPath.lineTo(corner.x, corner.y);
                    bounds.union(corner.x, corner.y);
                }
            }
            mPath.close();

            //ensure that our shadow is centered again the Hex with coords (0,0,0);
            PointF shadowCenter = LayoutHelper.hexToPixel(layoutHelper, new Hex(0, 0, 0));
            if ((bounds.right - shadowCenter.x) > (shadowCenter.x - bounds.left)) {
                bounds.left = bounds.right - 2 * (bounds.right - shadowCenter.x);
            } else {
                bounds.right = bounds.left + 2 * (shadowCenter.x - bounds.left);
            }

            if ((bounds.bottom - shadowCenter.y) > (shadowCenter.y - bounds.top)) {
                bounds.top = bounds.bottom - 2 * (bounds.top - shadowCenter.y);
            } else {
                bounds.bottom = bounds.top + 2 * (shadowCenter.y - bounds.top);
            }
        }

        Log.i(TAG, "BOUNDS: " + bounds.toShortString());
    }

    @Override
    public void onDrawShadow(Canvas canvas) {
        canvas.save();
        canvas.translate(-bounds.left, -bounds.top);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
    }

    @Override
    public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {
        shadowSize.x = (int) bounds.width();
        shadowSize.y = (int) bounds.height();
        shadowTouchPoint.x = shadowSize.x / 2;
        shadowTouchPoint.y = shadowSize.y / 2;
    }
}
