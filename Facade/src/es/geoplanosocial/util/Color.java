package es.geoplanosocial.util;

/**
 * Handy colors
 * Created by gbermejo on 19/04/17.
 */
public class Color {


    //Prevent magic numbers
    // public static final int WHITE = 255;
    public static final int WHITE = Utils.color(255, 255, 255);
    public static final int BLACK = Utils.color(0, 0, 0);
    public static final int ALPHA = Utils.color(0, 0, 0, 0);


    public static final int LIGHT_GREY = Utils.color(170, 170, 170);// 2/3
    public static final int GREY = Utils.color(127, 127, 127);// 1/2
    public static final int DARK_GREY = Utils.color(85, 85, 85);// 1/3

    public static final int RED = Utils.color(255, 0, 0);
    public static final int GREEN = Utils.color(0, 255, 0);
    public static final int BLUE = Utils.color(0, 0, 255);


    public static final int MAGENTA = Utils.color(255, 0, 255);
    public static final int YELLOW = Utils.color(255, 255, 0);
    public static final int CYAN = Utils.color(0, 255, 255);

    public static final int ORANGE = Utils.color(255, 127, 0);


    public static final int WHITE_ALPHA = Utils.color(255, 255, 255, 127);
    public static final int BLACK_ALPHA = Utils.color(0, 0, 0, 127);


    public static final int BLACK_HOLE_COLOR = Utils.color(3, 12, 27);

    public static final int W1_ORANGE_NODE = Utils.color(249, 221, 40);
    public static final int W1_ORANGE_NODE_ALPHA = Utils.color(249, 221, 40, 85);



    /*********
     *World 1*
     *********/
    public static final int W1_BLACK_NODE = Utils.color(2, 12, 26);
    public static final int W1_WHITE_NODE = Utils.color(250, 253, 255);
    public static final int W1_WHITE_ALPHA_NODE = WHITE_ALPHA;

    //A
    public static final int W1_A_BG = Utils.color(5, 108, 104);
    //B
    public static final int W1_B_BG = Utils.color(1, 83, 79);
    //C
    public static final int W1_C_BG = Utils.color(1, 53, 49);

    /*********
     *World 2*
     *********/
    public static final int W2_BLACK_NODE = W1_BLACK_NODE;
    public static final int W2_WHITE_NODE = W1_WHITE_NODE;

    //A
    public static final int W2_A_BG = Utils.color(29, 34, 98);
    //B
    public static final int W2_B_BG = Utils.color(25, 25, 77);
    //C
    public static final int W2_C_BG = Utils.color(13, 4, 47);


    /*********
     *World 3*
     *********/
    //public static final int W3_RED_NODE = Utils.color(230, 76, 26);
    //public static final int W3_GREEN_NODE = Utils.color(88, 205, 118);
    //public static final int W3_BLUE_NODE = Utils.color(104, 194, 240);

    public static final int W3_RED_NODE = Utils.color(230, 76, 26);
    public static final int W3_GREEN_NODE = Utils.color(73, 183, 78);
    public static final int W3_BLUE_NODE = Utils.color(87, 79, 153);
    //A
    public static final int W3_A_BG = Utils.color(5, 108, 104);
    //B
    public static final int W3_B_BG = Utils.color(1, 83, 79);
    //C
    public static final int W3_C_BG = Utils.color(1, 53, 49);


    /*********
     *World 4*
     *********/
    public static final int W4_BLACK_NODE = Utils.color(80, 70, 66);
    public static final int W4_YELLOW_NODE = Utils.color(255, 187, 9);
    public static final int W4_BLUE_NODE = Utils.color(49, 141, 200);
    public static final int W4_RED_NODE = Utils.color(235, 105, 107);
    //A
    public static final int W4_A_BG = Utils.color(1, 112, 43);
    //B
    public static final int W4_B_BG = Utils.color(0, 85, 30);
    //C
    public static final int W4_C_BG = Utils.color(0, 57, 16);


    /*********
     *World 5*
     *********/
    public static final int W5_PINK_NODE = Utils.color(255, 119, 189);
    public static final int W5_BLUE_NODE = Utils.color(3, 169, 244);
    public static final int W5_YELLOW_NODE = Utils.color(250, 187, 9);
    public static final int W5_RED_NODE = Utils.color(244, 67, 54);
    public static final int W5_GREEN_NODE = Utils.color(76, 175, 80);

    //A
    public static final int W5_A_BG = Utils.color(103, 136, 23);
    //B
    public static final int W5_B_BG = Utils.color(78, 104, 15);
    //C
    public static final int W5_C_BG = Utils.color(46, 68, 4);

    // Vertex
    public static final int VERTEXT_SELECTED = WHITE;

}
