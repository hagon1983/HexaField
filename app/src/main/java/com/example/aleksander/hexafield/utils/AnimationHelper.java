package com.example.aleksander.hexafield.utils;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.aleksander.hexafield.Cell;
import com.example.aleksander.hexafield.R;
import com.example.aleksander.hexafield.library.view.CellView;
import com.example.aleksander.hexafield.library.view.GameFieldView;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by aleksander on 30.10.15.
 */
public class AnimationHelper implements Animation.AnimationListener {
    private Collection<CellView> mCells;
    private final Animation clearAnimation;

    public AnimationHelper(Context context) {
        clearAnimation = AnimationUtils.loadAnimation(context, R.anim.clear_cellview);
        clearAnimation.setAnimationListener(this);
    }

    public void animateVanish(Collection<Cell> cells, GameFieldView gameFieldView) {
        mCells = new ArrayList<>(cells.size());
        for (Cell cell : cells) {
            mCells.add(gameFieldView.getCellView(cell));
        }
        for (CellView cellView : mCells) {
            cellView.startAnimation(clearAnimation);
            cellView.getCell().setType(Cell.CELL_EMPTY);
        }
    }


    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        for (CellView cellView : mCells) {
            cellView.updateColor();
            cellView.invalidate();
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }
}
