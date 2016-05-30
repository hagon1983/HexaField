package com.example.aleksander.hexafield;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aleksander on 23.10.15.
 */
public class Cell implements Parcelable{
    public static  final int CELL_PLACEHOLDER = 0;
    public static  final int CELL_EMPTY = 1;
    public static  final int CELL_FILLED = 2;

    public static  final int EFFECT_NONE = 0;
    public static  final int EFFECT_AVAILABLE = 1;
    public static  final int EFFECT_UNAVAILABLE = 2;

    private int type;
    private int effect;

    public Cell( int type){
        this.type = type;
        this.effect = 0;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getEffect() {
        return effect;
    }

    public void setEffect(int effect) {
        this.effect = effect;
    }

    public boolean isEmpty(){
        return getType() == CELL_EMPTY;
    }

    public boolean isPlaceholder(){
        return getType() == CELL_PLACEHOLDER;
    }

    public boolean isFilled(){
        return getType() == CELL_FILLED;
    }

    public int getColor(){
        switch (type){
            case CELL_PLACEHOLDER:
                return Color.TRANSPARENT;
            case CELL_EMPTY:
                return Color.GRAY;
        }
        return Color.CYAN;

    }

    public int getBackgroundColor(){
        if(effect == EFFECT_UNAVAILABLE){
            return Color.RED;
        }
        if(effect == EFFECT_AVAILABLE){
            return Color.GREEN;
        }
        return Color.TRANSPARENT;
    }


    @Override
    public String toString() {
        return "t: " + type + ", e: " + effect;
    }


    /*
    * For Parcelable interface
    * */

    public Cell(Parcel in){
        type = in.readInt();
        effect = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeInt(effect);
    }

    public static final Creator<Cell> CREATOR = new Creator<Cell>() {
        @Override
        public Cell createFromParcel(Parcel in) {
            return new Cell(in);
        }

        @Override
        public Cell[] newArray(int size) {
            return new Cell[size];
        }
    };


}
