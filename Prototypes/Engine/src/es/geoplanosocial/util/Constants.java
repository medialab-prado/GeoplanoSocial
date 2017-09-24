package es.geoplanosocial.util;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.levels.world1.a.Level1A;
import processing.core.PConstants;

/**
 * Global constants
 * Created by gbermejo on 19/04/17.
 */
public class Constants {

    //Customizable parameters
    public static final boolean DEBUG = false;
    public static final boolean FULLSCREEN = true;


    //Global engine parameters
    //Should not be changed

    //Screen size and settings
    public static final int SCREEN_WIDTH = 1024;
    public static final int SCREEN_HEIGHT = 768;
    public static final String SCREEN_RENDERER = PConstants.P3D;
    public static final int FPS = 60;
    public static final int ANTI_ALIASING_LEVEL = 8;

    //Levels settings
    public static final String LEVEL_CLASS_FULLY_QUALIFIED_FORMAT = "es.geoplanosocial.levels.world%d.%s.Level%d%s";
    public static final Class DEFAULT_LEVEL_CLASS = Level1A.class;//FIXME with home screen one when done;
    public static final Class ROOT_LEVEL_CLASS = Level.class;


    //Facade sizes and positions
    public static final int LEVEL_WIDTH = 192;
    public static final int LEVEL_HEIGHT = 125;
    public static final int START_WORLD_X = 40;
    public static final int START_WORLD_Y = 72;
    public static final int START_THUMBNAIL_X = 122;
    public static final int START_THUMBNAIL_Y = 48;

    //BlackHole
    public static final float BLACK_HOLE_SIZE_PERCENTAGE = 0.72f;
    public static final float BLACK_HOLE_THICKNESS_RING = 5;
    public static final float BLACK_HOLE_MOVE_PERCENTAGE = 0.33f;
    public static final float DEGREE_IN_RAD = (float)(Math.PI/180.0);
    public static final float  BLACK_HOLE_MOVE_STEP = DEGREE_IN_RAD*1.5f;


    //Level change
    public static final int PLAY_AREA_OFFSET = 15;
    public static final int PLAY_AREA_TIMER = 5 * 1000;//In milliseconds
    public static final float PLAY_AREA_MIN_PERCENTAGE = 0.5f;

    //World change
    public static final int WORLD_CHANGE_TIMER_IN = 5 * 1000;//In milliseconds
    public static final int WORLD_CHANGE_TIMER_OUT = 5 * 1000;//In milliseconds


    //Player simulation
    public static final int PLAYER_SIZE = 15;


    //OSC
    public static final int OSC_PORT = 12345;

    //MediaLabCV
    public static final String MLCV_TEST_VIDEO = "geoplano2.mp4";

    public static final  int MLCV_HISTORY = 500;
    public static final  double MLCV_DIST_TO_THRESHOLD = 400;
    public static final  boolean MLCV_DETECT_SHADOWS = true;

    public static final  int MLCV_ERODE_SIZE = 3;
    public static final  int MLCV_ERODE_ITERATIONS = 1;
    public static final  int MLCV_DILATE_SIZE = 18;
    public static final  int MLCV_DILATE_ITERATIONS = 1;

    public static final  boolean MLCV_ADJACENT_MERGE = true;
    public static final  boolean MLCV_SHADOW_REMOVAL = true;
    public static final  double MLCV_THRESHOLD = 10;

    public static final  double MLCV_BB_MIN_WIDTH_PERCENTAGE = 0.05;
    public static final  double MLCV_BB_MAX_WIDTH_PERCENTAGE = 0.75;
    public static final  double MLCV_BB_MIN_HEIGHT_PERCENTAGE = 0.05;
    public static final  double MLCV_BB_MAX_HEIGHT_PERCENTAGE = 0.75;




    //Visual Debug
    public static final int SAFE_VISUAL_X = START_WORLD_X+LEVEL_WIDTH+START_WORLD_X;
    public static final int SAFE_VISUAL_Y = START_WORLD_Y+LEVEL_HEIGHT+START_WORLD_Y;


}
