package com.example.aleksander.hexafield.library;

/**
 * Created by aleksander on 26.10.15.
 */
public class Orientation {
    public Orientation(double f0, double f1, double f2, double f3, double b0, double b1, double b2, double b3, double start_angle)
    {
        this.f0 = (float) f0;
        this.f1 = (float) f1;
        this.f2 = (float) f2;
        this.f3 = (float) f3;
        this.b0 = (float) b0;
        this.b1 = (float) b1;
        this.b2 = (float) b2;
        this.b3 = (float) b3;
        this.start_angle = (float) start_angle;
    }
    public final float f0;
    public final float f1;
    public final float f2;
    public final float f3;
    public final float b0;
    public final float b1;
    public final float b2;
    public final float b3;
    public final float start_angle;

}
