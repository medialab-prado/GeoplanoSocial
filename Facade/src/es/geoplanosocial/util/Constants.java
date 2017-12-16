package es.geoplanosocial.util;

import es.geoplanosocial.games.Game;
import es.geoplanosocial.games.Game1;
import processing.core.PConstants;

import static es.geoplanosocial.util.Color.*;

/**
 * Global constants
 * Created by gbermejo on 19/04/17.
 */
public class Constants {

    //Customizable parameters
    public static final boolean DEBUG = true;
    public static final boolean FULLSCREEN = true;


    //Global engine parameters
    //Should not be changed

    //Screen size and settings
    public static final int SCREEN_WIDTH = 1024;
    public static final int SCREEN_HEIGHT = 768;
    public static final String SCREEN_RENDERER = PConstants.P3D;
    public static final int FPS = 60;
    public static final int ANTI_ALIASING_LEVEL = 8;

    //Camera size and settings
    public static final int CAMERA_WIDTH = 640;
    public static final int CAMERA_HEIGHT = 480;

    //Levels settings
    public static final String GAME_CLASS_FULLY_QUALIFIED_FORMAT = "es.geoplanosocial.games.Game%d";
    public static final Class DEFAULT_GAME_CLASS = Game1.class;


    //Facade sizes and positions
    public static final int LEVEL_WIDTH = 192;
    public static final int LEVEL_HEIGHT = 125;
    public static final int START_WORLD_X = 40;
    public static final int START_WORLD_Y = 72;
    public static final int START_THUMBNAIL_X = 122;
    public static final int START_THUMBNAIL_Y = 48;
    public static final int HAT_OFFSET=32;

    //BlackHole
    public static final float BLACK_HOLE_SIZE_PERCENTAGE = 0.72f;
    public static final float BLACK_HOLE_THICKNESS_RING = 5;
    public static final float BLACK_HOLE_MOVE_PERCENTAGE = 0.33f;
    public static final float DEGREE_IN_RAD = (float)(Math.PI/180.0);
    public static final float  BLACK_HOLE_MOVE_STEP = DEGREE_IN_RAD*1.5f;


    //Game change
    public static final int PLAY_AREA_OFFSET_X = 18;
    public static final int PLAY_AREA_OFFSET_Y = 12;

    //World change
    public static final int WORLD_CHANGE_TIMER_IN = 2 * 1000;//In milliseconds
    public static final int WORLD_CHANGE_TIMER_OUT = 7 * 1000;//In milliseconds


    //Player simulation
    //public static final int playerSize = 35;


    //OSC
    public static final int OSC_PORT = 12345;


    public static int [][] WORLD_COLORS = {
            {LIGHT_GREY, GREY, DARK_GREY},
            {W1_A_BG, W1_B_BG, W1_C_BG},
            {W2_A_BG, W2_B_BG, W2_C_BG},
            {W3_A_BG, W3_B_BG, W3_C_BG},
            {W4_A_BG, W4_B_BG, W4_C_BG},
            {W5_A_BG, W5_B_BG, W5_C_BG},
    };

    public static int WORLDS_NUMBER = WORLD_COLORS.length;

    //Visual Debug
    public static final int SAFE_VISUAL_X = START_WORLD_X+LEVEL_WIDTH+START_WORLD_X;
    public static final int SAFE_VISUAL_Y = START_WORLD_Y+LEVEL_HEIGHT+START_WORLD_Y;

    // Vertex
    public static final int VERTEX_NORMAL_RADIO = 10;
    public static final int VERTEX_SELECTED_RADIO = VERTEX_NORMAL_RADIO;


    public static final int MAX_PLAYERS = 7;


}
