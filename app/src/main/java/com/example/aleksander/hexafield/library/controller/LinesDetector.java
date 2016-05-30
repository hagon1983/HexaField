package com.example.aleksander.hexafield.library.controller;

import com.example.aleksander.hexafield.Cell;
import com.example.aleksander.hexafield.library.model.GameField;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by aleksander on 30.10.15.
 */
public class LinesDetector {

    Set<Cell> tmp;

    public LinesDetector() {
        this.tmp = new HashSet<>();
    }

    public Collection<Cell> detectLines(GameField gameField) {
        Collection<Cell> ret = new HashSet<>();

        addSlashLines(gameField, ret);
        addHorizontalLines(gameField, ret);
        addBackSlashLines(gameField, ret);

        return ret;
    }


    private void addSlashLines(final GameField gameField, Collection<Cell> ret) {
        final int mapRadius = gameField.getMapRadius();

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
                        tmp.add(cell);
                    }
                }
            }
            if (addLine) {
                ret.addAll(tmp);
            }
        }
    }

    private void addHorizontalLines(final GameField gameField, Collection<Cell> ret) {
        final int mapRadius = gameField.getMapRadius();

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
                        tmp.add(cell);
                    }
                }
            }
            if (addLine) {
                ret.addAll(tmp);
            }
        }
    }

    private void addBackSlashLines(final GameField grid, Collection<Cell> ret) {
        final int mapRadius = grid.getMapRadius();

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
                        tmp.add(cell);
                    }
                }
            }
            if (addLine) {
                ret.addAll(tmp);
            }
        }
    }

}
