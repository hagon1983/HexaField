package com.example.aleksander.hexafield.library.controller;

import com.example.aleksander.hexafield.Cell;
import com.example.aleksander.hexafield.library.Hex;
import com.example.aleksander.hexafield.library.model.GameField;
import com.example.aleksander.hexafield.library.model.GameFigure;

import java.util.Collection;

/**
 * Created by aleksander on 31.10.15.
 */
public class CanPutDetecdor {

    /**
     * Check if field have the suitable space for the figure
     * */
    public static boolean hasRoomForFigure(GameField field, GameFigure figure){
        final int mapRadius = field.getMapRadius();
        for(int q = -mapRadius; q<= mapRadius; q++){
            final int r1 = Math.max(-mapRadius, -q - mapRadius);
            final int r2 = Math.min(mapRadius, -q + mapRadius);
            for(int r = r1; r <= r2; r++){
                boolean bSuitable = true;
                for(Hex h : figure.getItems()){
                    Cell cell = field.getCell(q + h.q , r + h.r);
                    if(cell == null || ! cell.isEmpty()){
                        // can't fit, look another )
                        bSuitable = false;
                        break;
                    }
                }
                if(bSuitable){
                    return true;
                }
            }
        }

        return false;
    };


}
