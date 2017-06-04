package es.geoplanosocial.util;

import es.geoplanosocial.levels.Level;

import java.text.SimpleDateFormat;
import java.util.Random;


/**
 * Utility methods
 * Created by gbermejo on 19/04/17.
 */
public class Utils {

    static int color(int r, int g, int b) {
        return color(r, g, b, 255);
    }

    static int color(int r, int g, int b, int a) {
        if (r > 255) r = 255;
        else if (r < 0) r = 0;
        if (g > 255) g = 255;
        else if (g < 0) g = 0;
        if (b > 255) b = 255;
        else if (b < 0) b = 0;
        if (a > 255) a = 255;
        else if (a < 0) a = 0;

        return 0x00000000 | (a << 24) | (r << 16) | (g << 8) | b;
    }

    private static SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");


    public static void log(String text){
        if (Constants.DEBUG)
            System.out.println("["+formatter.format(System.currentTimeMillis())+"]"+text);
    }


    public static int[] getWorldColors(int players){
        int[] worldColors=new int[Level.Type.values().length];

        int index=0;
        for (Level.Type level : Level.Type.values()) {

            Level l=Level.Factory.getLevel(players, level);
            if(l!=null){
                worldColors[index++]=l.getMainColor();
            }else{
                worldColors[index++]=Color.MAGENTA;
            }
        }

        return worldColors;
    }

    private static Random random = new Random();

    public static int randomInt(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }
}
