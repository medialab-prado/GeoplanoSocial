package es.geoplanosocial.util;

import es.geoplanosocial.levels.Level;

import static es.geoplanosocial.util.Constants.DEAULT_LEVEL_CLASS;
import static es.geoplanosocial.util.Constants.LEVEL_CLASS_FULLY_QUALIFIED_FORMAT;

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

    private static String getLevelClassName(int players, Types.Level level){

        String levelName = level.name();

        return String.format(LEVEL_CLASS_FULLY_QUALIFIED_FORMAT,players,levelName.toLowerCase(),players,levelName);
    }

    private static Class getLevelClass(int players, Types.Level level){

        Class levelClass;

        try {
            levelClass = Class.forName(getLevelClassName(players, level));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            levelClass = DEAULT_LEVEL_CLASS;
        }

        return levelClass;
    }

    public static Level getLevel(int players, Types.Level level){

        Class levelClass=Utils.getLevelClass(players,level);
        Level l=null;

        try {
            l=(Level)levelClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return l;
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
