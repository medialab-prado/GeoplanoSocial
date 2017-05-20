package es.geoplanosocial.util;

import es.geoplanosocial.levels.Level;

import static es.geoplanosocial.factories.LevelFactory.getLevel;

/**
 * Utility methods
 * Created by gbermejo on 19/04/17.
 */
public class Utils {


    static int color(int r, int g, int b) {
        if (r > 255) r = 255;
        else if (r < 0) r = 0;
        if (g > 255) g = 255;
        else if (g < 0) g = 0;
        if (b > 255) b = 255;
        else if (b < 0) b = 0;

        return 0xff000000 | (r << 16) | (g << 8) | b;
    }

    public static void log(String text){
        if (Constants.DEBUG)
            System.out.println(text);
    }


    public static int[] getWorldColors(int players){
        int[] worldColors=new int[Types.Level.values().length];

        int index=0;
        for (Types.Level level : Types.Level.values()) {

            Level l=getLevel(players, level);
            if(l!=null){
                worldColors[index++]=l.getMainColor();
            }else{
                worldColors[index++]=Color.MAGENTA;
            }
        }

        return worldColors;
    }

}
