package com.example.aleksander.hexafield.library.view;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.widget.AbsoluteLayout;

import com.example.aleksander.hexafield.Cell;
import com.example.aleksander.hexafield.library.Hex;
import com.example.aleksander.hexafield.library.LayoutHelper;
import com.example.aleksander.hexafield.library.model.GameFigure;

/**
 * Created by aleksander on 28.10.15.
 */
@SuppressWarnings("deprecation")
public class FigureView extends AbsoluteLayout {

    private GameFigure figure;

    private int childSize = -1;
    private LayoutHelper layoutHelper = null;


    public FigureView(Context context) {
        this(context, null);
    }

    public FigureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameFigure getFigure() {
        return figure;
    }

    public void setFigure(GameFigure figure) {
        removeAllViewsInLayout();
        resetChildSizeAndOffsets();
        this.figure = figure;
        createChildViews();
    }

    private void createChildViews() {
        for (Hex h : figure.getItems()) {
            Cell cell = new Cell(Cell.CELL_FILLED);
            CellView cellView = new CellView(getContext());
            cellView.setCell(cell);
            LayoutParams layoutParams = new LayoutParams(10, 10, 0, 0);
            cellView.setLayoutParams(layoutParams);
            cellView.setTag(h);
            addView(cellView);
        }
    }

    private void calcChildSizeAndOffsets() {
        if (childSize <= 0) {
            if (getMeasuredWidth() == 0) {
                throw new IllegalStateException("View is not measured yet");
            }
            if (figure == null) {
                throw new IllegalStateException("figure should be specified");
            }

            float h1 = (4 * getMeasuredHeight()) / (3 * figure.getDimension() + 1);
            float h2 = ((2 * getMeasuredWidth()) / (figure.getDimension() * (float) Math.sqrt(3.0)));
            int h = (int) Math.min(h1, h2);
            childSize = h / 2;

            PointF size = new PointF(childSize, childSize);
            PointF origin = new PointF(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
            layoutHelper = new LayoutHelper(LayoutHelper.pointy, size, origin);
        }
    }

    private void resetChildSizeAndOffsets() {
        childSize = -1;
    }

    private void layoutChild(CellView v) {
        calcChildSizeAndOffsets();
        Hex h = (Hex) v.getTag();

        final int childHeight = childSize * 2;
        final int childWidth = (int) (childHeight * Math.sqrt(3) / 2);
        PointF center = LayoutHelper.hexToPixel(layoutHelper, h);

        int left = (int) (center.x - (childWidth) / 2);
        int top = (int) (center.y - childSize);
        v.layout(left, top, left + childWidth, top + childHeight);
        v.setLayout(layoutHelper, h, left, top);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getMeasuredWidth() == 0 || figure == null) {
            super.onLayout(changed, l, t, r, b);
        } else {

            if (changed) {
                resetChildSizeAndOffsets();
            }
            for (int i = 0, num = getChildCount(); i < num; i++) {
                layoutChild((CellView) getChildAt(i));
            }
        }
    }


}
