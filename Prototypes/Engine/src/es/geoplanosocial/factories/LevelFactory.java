package es.geoplanosocial.factories;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.util.Types;

import static es.geoplanosocial.util.Constants.DEFAULT_LEVEL_CLASS;
import static es.geoplanosocial.util.Constants.LEVEL_CLASS_FULLY_QUALIFIED_FORMAT;

/**
 * Factory for creating levels.
 * Created by gbermejo on 20/05/17.
 */
public class LevelFactory {

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
            levelClass = DEFAULT_LEVEL_CLASS;
        }

        return levelClass;
    }

    public static Level getLevel(int players, Types.Level level){

        Class levelClass= getLevelClass(players,level);
        Level l=null;

        try {
            l=(Level)levelClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return l;
    }
}
