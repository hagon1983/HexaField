package com.example.aleksander.hexafield.library.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.aleksander.hexafield.library.Hex;

import java.util.HashSet;

/**
 * Created by aleksander on 01.11.15.
 */
public class GameFigure implements Parcelable {

    final public static int TYPE_SINGLE_DOT = 0;
    final public static int TYPE_HORIZONTAL_LINE = 1;
    final public static int TYPE_SLASH_LINE = 2;
    final public static int TYPE_BACKSLASH_LINE = 3;
    final public static int TYPE_LETTER_C = 4;
    final public static int TYPE_LETTER_C_FLIPPED = 5;
    final public static int TYPE_CROSS = 6;
    final public static int NUM_TYPES = TYPE_CROSS + 1;

    protected HashSet<Hex> items;
    private int mapRadius;

    public GameFigure(int mapRadius) {
        this.mapRadius = mapRadius;
        this.items = new HashSet<>();
    }

    protected void addItem(int q, int r) {
        items.add(new Hex(q, r, -q - r));
    }

    public HashSet<Hex> getItems() {
        return items;
    }

    public int getDimension() {
        return mapRadius * 2 + 1;
    }

    protected GameFigure(Parcel in) {
        mapRadius = in.readInt();
        int numItems = in.readInt();
        items = new HashSet<>(numItems);
        int q, r;
        for (int i = 0; i < numItems; i++) {
            q = in.readInt();
            r = in.readInt();
            items.add(new Hex(q, r, -q - r));
        }
    }

    public static final Creator<GameFigure> CREATOR = new Creator<GameFigure>() {
        @Override
        public GameFigure createFromParcel(Parcel in) {
            return new GameFigure(in);
        }

        @Override
        public GameFigure[] newArray(int size) {
            return new GameFigure[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        final int numItems = items.size();

        dest.writeInt(mapRadius);
        dest.writeInt(numItems);
        for (Hex h : items) {
            dest.writeInt(h.q);
            dest.writeInt(h.r);
        }

    }
}
