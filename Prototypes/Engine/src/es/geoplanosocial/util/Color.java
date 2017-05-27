package es.geoplanosocial.util;

/**
 * Handy colors
 * Created by gbermejo on 19/04/17.
 */
public class Color {

    //Prevent magic numbers
    public static final int WHITE = 255;
    public static final int BLACK = 0;

    public static final int LIGHT_GREY = 170;// 2/3
    public static final int GREY = 127;// 1/2
    public static final int DARK_GREY = 85;// 1/3

    public static final int RED = Utils.color(255, 0, 0);
    public static final int GREEN = Utils.color(0, 255, 0);
    public static final int BLUE = Utils.color(0, 0, 255);


    public static final int MAGENTA = Utils.color(255, 0, 255);
    public static final int YELLOW = Utils.color(255, 255, 0);
    public static final int CYAN = Utils.color(0, 255, 255);


    public static final int WHITE_ALPHA = Utils.color(255, 255, 255, 127);
    public static final int RED_ALPHA = Utils.color(255, 0, 0, 127);

}
