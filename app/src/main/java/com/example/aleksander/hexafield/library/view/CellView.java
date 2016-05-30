package com.example.aleksander.hexafield.library.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import com.example.aleksander.hexafield.Cell;
import com.example.aleksander.hexafield.library.Hex;
import com.example.aleksander.hexafield.library.LayoutHelper;

import java.util.ArrayList;

/**
 * Created by aleksander on 25.10.15.
 */
public class CellView extends View {

    private Cell mCell;

    private Paint mPaint;
    private Path mPath = new Path();

    private Paint mBackPaint;
    private Path mBackPath = new Path();

    public CellView(Context context) {
        this(context, null);
    }

    public CellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public Cell getCell() {
        return mCell;
    }

    public void setCell(Cell cell) {
        this.mCell = cell;
        mPaint.setColor(cell.getColor());
        mBackPaint.setColor(cell.getBackgroundColor());
    }

    public void setLayout(LayoutHelper layoutHelper, Hex hex, int left, int top) {
        ArrayList<PointF> corners = LayoutHelper.polygonCorners(layoutHelper, hex, 0.9f);
        mPath.reset();
        for (int i = 0, num = corners.size(); i < num; i++) {
            PointF p = corners.get(i);
            if (i == 0) {
                mPath.moveTo(p.x - left, p.y - top);
            } else {
                mPath.lineTo(p.x - left, p.y - top);
            }
        }
        mPath.close();

        ArrayList<PointF> cornersBackground = LayoutHelper.polygonCorners(layoutHelper, hex, 0.95f);
        mBackPath.reset();
        for (int i = 0, num = cornersBackground.size(); i < num; i++) {
            PointF p = cornersBackground.get(i);
            if (i == 0) {
                mBackPath.moveTo(p.x - left, p.y - top);
            } else {
                mBackPath.lineTo(p.x - left, p.y - top);
            }
        }
        mBackPath.close();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mCell != null && mCell.getEffect() != Cell.EFFECT_NONE) {
            canvas.drawPath(mBackPath, mBackPaint);
        }
        if (!mPath.isEmpty()) {
            canvas.drawPath(mPath, mPaint);
        }
    }

    public void updateColor() {
        mPaint.setColor(mCell.getColor());
    }

    public void updateBackgroundColor() {
        mBackPaint.setColor(mCell.getBackgroundColor());
    }


}
