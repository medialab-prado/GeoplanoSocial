package es.geoplanosocial.engine;

import processing.core.*;

import static es.geoplanosocial.util.Color.*;
import static es.geoplanosocial.util.Constants.*;

/**
 * World engine
 * Created by gbermejo on 19/04/17.
 */
public class Engine extends PApplet {

    //Can be changed
    private static final boolean DRAW_FACADE_OUTLINE = true;

    //Background related
    private static PGraphics BG;

    //Cube
    private static Cube worldCube;

    /**********************
     *PROCESSING FUNCTIONS*
     **********************/

    public void settings() {
        size(SCREEN_WIDTH, SCREEN_HEIGHT, SCREEN_RENDERER);
        smooth(ANTI_ALIASING_LEVEL);//No more aliasing
    }

    public void setup() {

        //Set global parameters
        frameRate(FPS);
        noCursor();//Ugly


        BG = generateFacadeBackground(width, height, SCREEN_RENDERER, DRAW_FACADE_OUTLINE);

        worldCube = new Cube(this, LEVEL_WIDTH, LEVEL_HEIGHT, new int[]{RED, GREEN, BLUE});

    }

    public void draw() {
        //long startTime = System.nanoTime();


        //Update elements
        update();

        //Draw elements

        drawBackground(BG);//Background
        drawWorld();//Draw the world
        drawLevel();
        drawExtra();
        drawDebug();

        //println("Duration\t" + (System.nanoTime() - startTime));

        //saveFrame("./frames/frame-######.png");//Imagemagick -> convert -delay 60,1000  -loop 0 *.png World1.gif
    }

    public void keyPressed() {
        if (key == CODED) {
            worldCube.move(keyCode);
        }
    }

    /***************
     *OWN FUNCTIONS*
     ***************/


    /*LOOP FUNCTIONS*/

    //Updates elements and properties
    private void update() {

        worldCube.update();//Rotation animation of the current world

    }

    private void drawBackground(PGraphics pg) {
        //Draw the bg in the pg
        image(pg, 0, 0);
    }

    private void drawWorld() {
        //Draw the world in the pg
        //Updates copy
        image(worldCube.getMainGraphics(), START_WORLD_X, START_WORLD_Y);
    }

    private void drawLevel() {
        //TODO
    }

    private void drawExtra() {

        //Draw thumbnail of world
        image(worldCube.getThumbnailGraphics(), START_THUMBNAIL_X, START_THUMBNAIL_Y);
    }


    private void drawDebug() {
        textSize(32);
        textAlign(LEFT, TOP);
        text("Level: " + worldCube.getCurrentLevel().name(), 0, 0);
    }


    /*DRAW FUNCTIONS*/

    private PGraphics generateFacadeBackground(int _width, int _height, String renderer, boolean outlineEnabled) {

        //Generate the graphics buffer and initialize it
        PGraphics pg = createGraphics(_width, _height, renderer);
        pg.beginDraw();

        //Draw the corresponding background on the buffer

        //Set plain background
        pg.background(LIGHT_GREY);

        //Draw outline if required
        if (outlineEnabled) {
            pg.strokeWeight(1);//1 pixel
            pg.stroke(MAGENTA);
            drawFacadeOutline(pg);
        }

        //End rendering on graphics buffer
        pg.endDraw();

        return pg;
    }

    //Draw facade outline. From template provided in Medialab-Prado.
    //More info -> http://medialab-prado.es/article/fachada_digital_informacion_tecnica

    //FIXME: outline external to game area?
    private void drawFacadeOutline(PGraphics pg) {

        //Big lines

        //Right side
        pg.line(231, 72, 231, 196);

        //Bottom
        pg.line(231, 196, 40, 196);

        //Left side
        pg.line(40, 196, 40, 72);

        //Steps

        //1 h
        pg.line(40, 72, 75, 72);

        //2 v
        pg.line(75, 72, 75, 56);

        //2 h
        pg.line(75, 56, 111, 56);

        //3 v
        pg.line(111, 56, 111, 40);

        //3 h
        pg.line(111, 40, 159, 40);

        //3 v
        pg.line(159, 40, 159, 56);

        //4 h
        pg.line(159, 56, 195, 56);

        //4 v
        pg.line(195, 56, 195, 72);

        //5 h
        pg.line(195, 72, 231, 72);
    }

}
