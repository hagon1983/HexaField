package com.example.aleksander.hexafield.library.model;

import java.util.Random;

/**
 * Created by aleksander on 30.10.15.
 */
public class FigureFactory {

    public static GameFigure createFigure(final int figureRadius) {
        GameFigure figure = new GameFigure(figureRadius);
        switch (new Random().nextInt(GameFigure.NUM_TYPES)) {
            case GameFigure.TYPE_SINGLE_DOT:
                figure.addItem(0, 0);
                break;

            case GameFigure.TYPE_HORIZONTAL_LINE:
                for (int q = -figureRadius; q < figureRadius; q++) {
                    figure.addItem(q, 0);
                }
                break;

            case GameFigure.TYPE_SLASH_LINE:
                for (int r = -figureRadius; r < figureRadius; r++) {
                    figure.addItem(0, r);
                }
                break;

            case GameFigure.TYPE_BACKSLASH_LINE:
                for (int r = -figureRadius; r < figureRadius; r++) {
                    figure.addItem(-r, r);
                }
                break;

            case GameFigure.TYPE_LETTER_C:
                figure.addItem(0, 1);
                figure.addItem(-1, 1);
                figure.addItem(-1, 0);
                figure.addItem(0, -1);
                figure.addItem(1, -1);
                break;

            case GameFigure.TYPE_LETTER_C_FLIPPED:
                figure.addItem(0, -1);
                figure.addItem(1, -1);
                figure.addItem(1, 0);
                figure.addItem(0, 1);
                figure.addItem(-1, 1);
                break;

            case GameFigure.TYPE_CROSS:
                figure.addItem(0, 0);
                figure.addItem(-1, 1);
                figure.addItem(-1, 0);
                figure.addItem(0, -1);
                break;

        }
        return figure;

    }
}
