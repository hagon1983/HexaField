package com.example.aleksander.hexafield.library.controller;

import com.example.aleksander.hexafield.Cell;
import com.example.aleksander.hexafield.library.model.GameField;
import com.example.aleksander.hexafield.library.view.CellView;
import com.example.aleksander.hexafield.library.view.GameFieldView;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by aleksander on 30.10.15.
 */
public class LinesDetector {

    public static Collection<CellView> detectLines(GameFieldView containerView) {
        Collection<CellView> ret = new HashSet<CellView>();
        GameField gameField = containerView.getGameField();

        addSlashLines(containerView, gameField, ret);
        addHorizontalLines(containerView, gameField, ret);
        addBackSlashLines(containerView, gameField, ret);

        return ret;
    }


    private static void addSlashLines(GameFieldView containerView, final GameField gameField, Collection<CellView> ret) {
        final int dimension = gameField.getDimension();
        final int mapRadius = gameField.getMapRadius();

        Set<CellView> tmp = new HashSet<>(dimension);
        boolean addLine;

        for (int q = -mapRadius; q <= mapRadius; q++) {
            tmp.clear();
            addLine = true;
            for (int r = -mapRadius; r <= mapRadius; r++) {
                Cell cell = gameField.getCell(q, r);
                if (cell != null) {
                    if (cell.isEmpty()) {
                        addLine = false;
                        break;
                    } else if (cell.isFilled()) {
                        tmp.add(containerView.getCellView(cell));
                    }
                }
            }
            if (addLine) {
                ret.addAll(tmp);
            }
        }
    }

    private static void addHorizontalLines(GameFieldView containerView, final GameField gameField, Collection<CellView> ret) {
        final int dimension = gameField.getDimension();
        final int mapRadius = gameField.getMapRadius();

        Set<CellView> tmp = new HashSet<>(dimension);
        boolean addLine;

        for (int r = -mapRadius; r <= mapRadius; r++) {
            tmp.clear();
            addLine = true;
            for (int q = -mapRadius; q <= mapRadius; q++) {
                Cell cell = gameField.getCell(q, r);
                if (cell != null) {
                    if (cell.isEmpty()) {
                        addLine = false;
                        break;
                    } else if (cell.isFilled()) {
                        tmp.add(containerView.getCellView(cell));
                    }
                }
            }
            if (addLine) {
                ret.addAll(tmp);
            }
        }
    }

    private static void addBackSlashLines(GameFieldView containerView, final GameField grid, Collection<CellView> ret) {
        final int dimension = grid.getDimension();
        final int mapRadius = grid.getMapRadius();

        Set<CellView> tmp = new HashSet<>(dimension);
        boolean addLine;

        for (int sum = -mapRadius; sum <= mapRadius; sum++) {
            tmp.clear();
            addLine = true;
            for (int r = -mapRadius; r <= mapRadius; r++) {
                Cell cell = grid.getCell(sum - r, r);
                if (cell != null) {
                    if (cell.isEmpty()) {
                        addLine = false;
                        break;
                    } else if (cell.isFilled()) {
                        tmp.add(containerView.getCellView(cell));
                    }
                }
            }
            if (addLine) {
                ret.addAll(tmp);
            }
        }
    }

}
