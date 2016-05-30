package com.example.aleksander.hexafield.library.view;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;

import com.example.aleksander.hexafield.Cell;
import com.example.aleksander.hexafield.library.Hex;
import com.example.aleksander.hexafield.library.LayoutHelper;
import com.example.aleksander.hexafield.library.model.GameField;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aleksander on 24.10.15.
 */
@SuppressWarnings("deprecation")
public class GameFieldView extends AbsoluteLayout {
    private int childSize = -1;

    private LayoutHelper layoutHelper = null;

    private GameField gameField;
    private Map<Cell, CellView> cellCellViewMap;

    private Point offsetInRoot;

    public GameFieldView(Context context) {
        this(context, null);
    }

    public GameFieldView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GameFieldView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setGameField(GameField gameField) {
        removeAllViewsInLayout();
        resetChildSizeAndOffsets();
        this.gameField = gameField;
        createChildViews();
    }

    public LayoutHelper getLayoutHelper() {
        return layoutHelper;
    }

    public GameField getGameField() {
        return gameField;
    }

    public CellView getCellView(final Cell cell) {
        return cellCellViewMap.get(cell);
    }

    public Point getOffsetInRoot(final View parent) {
        if (offsetInRoot == null) {
            offsetInRoot = new Point(0, 0);
            ViewGroup tmp = this;
            do {
                offsetInRoot.x += tmp.getLeft();
                offsetInRoot.y += tmp.getTop();
                tmp = (ViewGroup) tmp.getParent();
                if (tmp == null) {
                    throw new IllegalArgumentException("wrong parent submitted");
                }
            } while (tmp.equals(parent));
        }
        return offsetInRoot;
    }

    private int getDimension() {
        return gameField.getDimension();
    }

    private void createChildViews() {
        cellCellViewMap = new HashMap<>();
        final int mapRadius = gameField.getMapRadius();
        for (int q = -mapRadius; q <= mapRadius; q++) {
            for (int r = -mapRadius; r <= mapRadius; r++) {
                final Cell cell = gameField.getCell(q, r);
                if (cell != null) {
                    Hex hex = new Hex(q, r, -q - r);
                    CellView cellView = new CellView(getContext());
                    cellView.setCell(cell);
                    LayoutParams layoutParams = new LayoutParams(10, 10, 0, 0);
                    cellView.setLayoutParams(layoutParams);
                    cellView.setTag(hex);
                    addView(cellView);
                    cellCellViewMap.put(cell, cellView);
                }
            }
        }
    }

    private void calcChildSizeAndOffsets() {
        if (childSize <= 0) {
            if (getMeasuredWidth() == 0) {
                throw new IllegalStateException("View is not measured yet");
            }
            if (gameField == null) {
                throw new IllegalStateException("gameField should be specified");
            }

            float h1 = (4 * getMeasuredHeight()) / (3 * getDimension() + 1);
            float h2 = ((2 * getMeasuredWidth()) / (getDimension() * (float) Math.sqrt(3.0)));
            int h = (int) Math.min(h1, h2);
            childSize = h / 2;

            PointF size = new PointF(childSize, childSize);
            PointF origin = new PointF(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
            layoutHelper = new LayoutHelper(LayoutHelper.pointy, size, origin);
        }
    }

    private void resetChildSizeAndOffsets() {
        childSize = -1;
        offsetInRoot = null;
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
        if (getMeasuredWidth() == 0 || gameField == null) {
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
