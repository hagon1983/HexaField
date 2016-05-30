package com.example.aleksander.hexafield.library.controller;

import android.content.ClipData;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

import com.example.aleksander.hexafield.Cell;
import com.example.aleksander.hexafield.library.FractionalHex;
import com.example.aleksander.hexafield.library.Hex;
import com.example.aleksander.hexafield.library.LayoutHelper;
import com.example.aleksander.hexafield.library.model.GameField;
import com.example.aleksander.hexafield.library.model.GameFigure;
import com.example.aleksander.hexafield.library.model.FigureFactory;
import com.example.aleksander.hexafield.library.view.CellView;
import com.example.aleksander.hexafield.library.view.FigureView;
import com.example.aleksander.hexafield.library.view.FigureShadowBuilder;
import com.example.aleksander.hexafield.library.view.GameFieldView;
import com.example.aleksander.hexafield.utils.AnimationHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by aleksander on 30.10.15.
 */
public class GameController implements View.OnTouchListener, View.OnDragListener {

    private static final String TAG = "GameController";

    private static final String BUNDLE_KEY_HEX_CONTAINER = "hexContainer";
    private static final String BUNDLE_KEY_FIGURES = "figures";

    final int FIGURE_RADIUS = 2;
    final int MAP_RADIUS = 4;

    private View rootView;
    private GameFieldView gameFieldView;
    private ArrayList<FigureView> dragableItems;
    private AnimationHelper animationHelper;
    private LinesDetector linesDetector;

    public GameController(View rootView, GameFieldView gameFieldView) {
        this.rootView = rootView;
        this.gameFieldView = gameFieldView;
        rootView.setOnDragListener(this);

        this.dragableItems = new ArrayList<>(3);
        animationHelper = new AnimationHelper(gameFieldView.getContext().getApplicationContext());
        linesDetector = new LinesDetector();
    }

    public void startGame() {
        gameFieldView.setGameField(new GameField(MAP_RADIUS));
        for (int i = 0; i < dragableItems.size(); i++) {
            dragableItems.get(i).setFigure(FigureFactory.createFigure(FIGURE_RADIUS));
        }
        checkConstraints();
    }

    public void resumeGame(Bundle bundle) {
        GameField gameField = bundle.getParcelable(BUNDLE_KEY_HEX_CONTAINER);
        gameFieldView.setGameField(gameField);

        ArrayList<GameFigure> figures = bundle.getParcelableArrayList(BUNDLE_KEY_FIGURES);
        for (int i = 0, num = figures.size(); i < num; i++) {
            dragableItems.get(i).setFigure(figures.get(i));
        }
        checkConstraints();
    }

    public Bundle saveGame() {
        Bundle ret = new Bundle();

        final int numFigures = dragableItems.size();
        ArrayList<Parcelable> figures = new ArrayList<>();
        for (int i = 0; i < numFigures; i++) {
            figures.add(dragableItems.get(i).getFigure());
        }

        ret.putParcelable(BUNDLE_KEY_HEX_CONTAINER, gameFieldView.getGameField());
        ret.putParcelableArrayList(BUNDLE_KEY_FIGURES, figures);
        return ret;
    }


    public GameFieldView getGameFieldView() {
        return gameFieldView;
    }

    public void addDragableItem(FigureView dragableItem) {
        dragableItems.add(dragableItem);
        setDragListeners(dragableItem);
    }

    private void setDragListeners(FigureView dragableItem) {
        dragableItem.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v instanceof FigureView) {
            FigureView item = (FigureView) v;
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new FigureShadowBuilder(gameFieldView.getLayoutHelper(), item.getFigure());
            item.startDrag(data, shadowBuilder, item, 0);
            return true;
        }
        return false;
    }

    private Set<Cell> previousMarked;
    private boolean bMayDrop = false;

    @Override
    public boolean onDrag(View v, DragEvent event) {
        if (!(event.getLocalState() instanceof FigureView)) {
            return false;
        }

        FigureView item = (FigureView) event.getLocalState();
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                previousMarked = new HashSet<>(item.getFigure().getItems().size());
                // do nothing
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                break;
            case DragEvent.ACTION_DROP:
                if (bMayDrop) {
                    performDrop(item);
                }
                break;
            case DragEvent.ACTION_DRAG_LOCATION:
                bMayDrop = true;
                Log.i(TAG, " x: " + event.getX() + "\t y: " + event.getY());
                FractionalHex fractionalHex = LayoutHelper.pixelToHex(getGameFieldView().getLayoutHelper(), new PointF(event.getX() - gameFieldView.getOffsetInRoot(rootView).x, event.getY() - gameFieldView.getOffsetInRoot(rootView).y));
                Hex centralHex = FractionalHex.hexRound(fractionalHex);

                HashSet<Cell> cells = new HashSet<>(item.getFigure().getItems().size());

                for (Hex h : item.getFigure().getItems()) {
                    Cell cell = getGameFieldView().getGameField().getCell(centralHex.q + h.q, centralHex.r + h.r);
                    if (cell != null) {
                        if (!cell.isEmpty()) {
                            bMayDrop = false;
                        }
                        cells.add(cell);
                    } else {
                        bMayDrop = false;
                    }
                }

                previousMarked.removeAll(cells);
                setEffect(previousMarked, Cell.EFFECT_NONE);
                setEffect(cells, bMayDrop ? Cell.EFFECT_AVAILABLE : Cell.EFFECT_UNAVAILABLE);
                previousMarked = cells;

                break;
            case DragEvent.ACTION_DRAG_ENDED:
                setEffect(previousMarked, Cell.EFFECT_NONE);
                previousMarked.clear();
            default:
                break;
        }

        return true;

    }

    private void setEffect(Collection<Cell> cells, final int effect) {
        for (Cell cell : cells) {
            CellView cellView = getGameFieldView().getCellView(cell);
            cell.setEffect(effect);
            cellView.updateBackgroundColor();
            cellView.invalidate();
        }
    }

    private void performDrop(FigureView item) {
        markDroppedCells();
        generateNextFigure(item);
        checkConstraints();
    }

    private void markDroppedCells() {
        for (Cell cell : previousMarked) {
            CellView cellView = getGameFieldView().getCellView(cell);
            cell.setType(Cell.CELL_FILLED);
            cellView.updateColor();
            cellView.invalidate();
        }
    }


    private void generateNextFigure(FigureView container) {
        container.setFigure(FigureFactory.createFigure(FIGURE_RADIUS));
    }

    private void checkLines() {
        Collection<Cell> cells = linesDetector.detectLines(gameFieldView.getGameField());
        if (!cells.isEmpty()) {
            //vanish line(s)
            animationHelper.animateVanish(cells, gameFieldView);
        }
    }

    private void checkGameOver() {
        for (FigureView figureView : dragableItems) {
            if (CanPutDetecdor.hasRoomForFigure(gameFieldView.getGameField(), figureView.getFigure())) {
                figureView.setBackgroundColor(Color.GREEN);
            } else {
                figureView.setBackgroundColor(Color.RED);
            }
        }
    }

    private void checkConstraints(){
        checkLines();
        checkGameOver();
    }

}
