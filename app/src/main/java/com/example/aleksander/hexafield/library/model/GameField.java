package com.example.aleksander.hexafield.library.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;

import com.example.aleksander.hexafield.Cell;

/**
 * Created by aleksander on 27.10.15.
 */
public class GameField implements Parcelable{

    private SparseArray<SparseArray<Cell>> model;
    protected int mapRadius;

    public GameField(final int mapRadius) {
        this.model = new SparseArray<>(2 * mapRadius + 1);
        this.mapRadius = mapRadius;
        initGrid();
    }

    public Cell getCell(int q, int r) {
        SparseArray<Cell> tmp = model.get(q);
        if (tmp == null) {
            return null;
        }
        return tmp.get(r);
    }

    public void setCell(int q, int r, Cell cell) {
        SparseArray<Cell> tmp = model.get(q);
        if (tmp == null) {
            tmp = new SparseArray<Cell>();
            model.put(q, tmp);
        }
        tmp.put(r, cell);
    }

    public int getMapRadius() {
        return mapRadius;
    }

    public int getDimension() {
        return mapRadius * 2 + 1;
    }


    private void initGrid() {
        for (int q = -mapRadius; q <= mapRadius; q++) {
            model.put(q, new SparseArray<Cell>(2 * mapRadius + 1));
            int r1 = Math.max(-mapRadius, -q - mapRadius);
            int r2 = Math.min(mapRadius, -q + mapRadius);
            for (int r = r1; r <= r2; r++) {
                Cell cell = new Cell(Cell.CELL_EMPTY);
                model.get(q).put(r, cell);
            }

        }
    }


    protected GameField(Parcel in) {
        mapRadius = in.readInt();
        model = new SparseArray<>(getDimension());

        int numCells = in.dataAvail() / 3 ;
        for(int i=0; i < numCells; i++){
            int q = in.readInt();
            int r = in.readInt();
            int type = in.readInt();
            setCell(q,r, new Cell(type));
        }
    }

    public static final Creator<GameField> CREATOR = new Creator<GameField>() {
        @Override
        public GameField createFromParcel(Parcel in) {
            return new GameField(in);
        }

        @Override
        public GameField[] newArray(int size) {
            return new GameField[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mapRadius);
        for(int q = -mapRadius; q <=mapRadius; q++){
            int r1 = Math.max(-mapRadius, -q - mapRadius);
            int r2 = Math.min(mapRadius, -q + mapRadius);
            for (int r = r1; r <= r2; r++) {
                Cell cell = new Cell(Cell.CELL_EMPTY);
                dest.writeInt(q);
                dest.writeInt(r);
                dest.writeInt(cell.getType());
            }
        }

    }
}
