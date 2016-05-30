package com.example.aleksander.hexafield.library;

/**
 * Created by aleksander on 26.10.15.
 */
public class FractionalHex {
    public FractionalHex(double q, double r, double s) {
        this.q = q;
        this.r = r;
        this.s = s;
    }

    public final double q;
    public final double r;
    public final double s;

    static public Hex hexRound(FractionalHex h) {
        int q = (int) (Math.round(h.q));
        int r = (int) (Math.round(h.r));
        int s = (int) (Math.round(h.s));
        double q_diff = Math.abs(q - h.q);
        double r_diff = Math.abs(r - h.r);
        double s_diff = Math.abs(s - h.s);
        if (q_diff > r_diff && q_diff > s_diff) {
            q = -r - s;
        } else if (r_diff > s_diff) {
            r = -q - s;
        } else {
            s = -q - r;
        }
        return new Hex(q, r, s);
    }
}
