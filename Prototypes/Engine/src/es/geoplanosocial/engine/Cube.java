package es.geoplanosocial.engine;

import es.geoplanosocial.util.Types.Level;
import es.geoplanosocial.util.Types.Axis;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PShape;

import static es.geoplanosocial.util.Color.BLACK;
import static es.geoplanosocial.util.Color.WHITE;
import static processing.core.PConstants.*;

/**
 * 3D cube representing the world
 * Created by gbermejo on 19/04/17.
 */
class Cube {

    private final PApplet parent;

    private final PGraphics pg;
    private final PGraphics thumbnailPg;

    private final PShape cube;

    private final int[] worldColors;//MUST BE OF SIZE 3 (A, B and C level bg color)

    private boolean onRotation;
    private Axis rotationAxis;
    private int rotationDirection;//clockwise -> 1  || anti-clockwise -> -1
    private float rotationCurrent;


    private Level front = Level.A;//The current level
    private Level right = Level.B;//Level at right and left
    private Level top = Level.C;//Level at top and bottom

    private static final float THUMBNAIL_SCALE = 1 / 7.0f;
    private static final float ROTATION_LIMIT = PConstants.HALF_PI; //90 degrees
    private static final float ROTATION_SPEED = 2 * PConstants.PI / 180.0f;//1 degree each step


    private static final float MAIN_TRANSLATE_COEFF = 2.0f;
    private static final float MAIN_SCALE_COEFF = 2.0f;

    private final float MAIN_TRANSLATE_X;
    private final float MAIN_TRANSLATE_Y;
    private final float MAIN_SCALE_X;
    private final float MAIN_SCALE_Y;

    private static final float THUMBNAIL_TRANSLATE_COEFF = 2.0f;
    private static final float THUMBNAIL_SCALE_COEFF = 3.5f;

    private final float THUMBNAIL_TRANSLATE_X;
    private final float THUMBNAIL_TRANSLATE_Y;
    private final float THUMBNAIL_SCALE_X;
    private final float THUMBNAIL_SCALE_Y;


    //Constructor
    Cube(PApplet parent, int width, int height, int[] worldColors) {

        if (worldColors.length != 3)
            worldColors = new int[]{BLACK, BLACK, BLACK};//BLACK should determine that something is not right.

        this.parent = parent;
        this.pg = parent.createGraphics(width, height, P3D);
        this.thumbnailPg = parent.createGraphics(Math.round(width * THUMBNAIL_SCALE), Math.round(height * THUMBNAIL_SCALE), P3D);
        this.worldColors = worldColors;


        MAIN_TRANSLATE_X = pg.width / MAIN_TRANSLATE_COEFF;
        MAIN_TRANSLATE_Y = pg.height / MAIN_TRANSLATE_COEFF;
        MAIN_SCALE_X = pg.width / MAIN_SCALE_COEFF;
        MAIN_SCALE_Y = pg.height / MAIN_SCALE_COEFF;

        THUMBNAIL_TRANSLATE_X = thumbnailPg.width / THUMBNAIL_TRANSLATE_COEFF;
        THUMBNAIL_TRANSLATE_Y = thumbnailPg.height / THUMBNAIL_TRANSLATE_COEFF;
        THUMBNAIL_SCALE_X = thumbnailPg.width / THUMBNAIL_SCALE_COEFF;
        THUMBNAIL_SCALE_Y = thumbnailPg.height / THUMBNAIL_SCALE_COEFF;

        resetRotation();

        cube = createCube();

        this.draw();//Initial state
    }

    //Create an OpenGL cube
    private PShape createCube() {
        PShape cube = parent.createShape();

        cube.beginShape(PConstants.QUADS);

        cube.fill(worldColors[0]);

        // +Z "front" face
        cube.vertex(-1, -1, 1, 0, 0);
        cube.vertex(1, -1, 1, 1, 0);
        cube.vertex(1, 1, 1, 1, 1);
        cube.vertex(-1, 1, 1, 0, 1);

        // -Z "back" face
        cube.vertex(1, -1, -1, 0, 0);
        cube.vertex(-1, -1, -1, 1, 0);
        cube.vertex(-1, 1, -1, 1, 1);
        cube.vertex(1, 1, -1, 0, 1);

        cube.fill(worldColors[1]);

        // +X "right" face
        cube.vertex(1, -1, 1, 0, 0);
        cube.vertex(1, -1, -1, 1, 0);
        cube.vertex(1, 1, -1, 1, 1);
        cube.vertex(1, 1, 1, 0, 1);

        // -X "left" face
        cube.vertex(-1, -1, -1, 0, 0);
        cube.vertex(-1, -1, 1, 1, 0);
        cube.vertex(-1, 1, 1, 1, 1);
        cube.vertex(-1, 1, -1, 0, 1);

        cube.fill(worldColors[2]);

        // +Y "bottom" face
        cube.vertex(-1, 1, 1, 0, 0);
        cube.vertex(1, 1, 1, 1, 0);
        cube.vertex(1, 1, -1, 1, 1);
        cube.vertex(-1, 1, -1, 0, 1);

        // -Y "top" face
        cube.vertex(-1, -1, -1, 0, 0);
        cube.vertex(1, -1, -1, 1, 0);
        cube.vertex(1, -1, 1, 1, 1);
        cube.vertex(-1, -1, 1, 0, 1);

        cube.endShape();
        return cube;
    }


    private void resetRotation() {
        onRotation = false;
        rotationAxis = Axis.Z;//Impossible axis
        rotationDirection = 0;//Impossible direction
        rotationCurrent = 0;//No rotation
    }


    //Moves cube
    void move(int inputKey) {
        if (!onRotation) {
            rotationDirection = 1;
            switch (inputKey) {
                case PConstants.UP:
                    rotationDirection = -1;
                case DOWN:
                    rotationAxis = Axis.X;
                    onRotation = true;
                    break;
                case LEFT:
                    rotationDirection = -1;
                case RIGHT:
                    //cube.rotateY(HALF_PI*dir);
                    rotationAxis = Axis.Y;
                    onRotation = true;
                    break;
            }
            computeLevel();
        }
    }

    //Set variables to indicate the current level and adjacent ones
    private void computeLevel() {
        Level temp;
        switch (rotationAxis) {
            case X:
                temp = front;
                front = top;
                top = temp;
                break;
            case Y:
                temp = front;
                front = right;
                right = temp;
                break;
            case Z:
            default:
                break;
        }
    }


    void update() {

        rotateCube();
    }

    private void rotateCube() {
        if (onRotation) {
            rotationCurrent += ROTATION_SPEED;

            switch (rotationAxis) {
                case X:
                    cube.rotateX(ROTATION_SPEED * rotationDirection);
                    break;
                case Y:
                    cube.rotateY(ROTATION_SPEED * rotationDirection);
                    break;
                case Z:
                default:
                    break;
            }

            if (rotationCurrent >= ROTATION_LIMIT) resetRotation();

            this.draw();//Just draw if needed to buffers
        }
    }

    //Draw cube and miniature
    private void draw() {
        drawMain();
        drawThumbnail();
    }

    //Draw main
    private void drawMain() {
        pg.beginDraw();

        pg.background(BLACK);

        //cube.camera(width/2.0, height/2.0, 3, width/2.0, height/2.0, 0, 0, 1, 0);
        //cube.perspective(PI/3.0, width/height, 1, 100);
        //pg.ortho();

        //pg.lights();
        //pg.noStroke();

        setTransformationCube();

        pg.shape(cube, 0, 0);

        pg.endDraw();
    }


    //Draw miniature
    private void drawThumbnail() {
        thumbnailPg.beginDraw();
        thumbnailPg.background(BLACK);
        //thumbnailPg.background(0, 1.0f);//Transparent bg

        thumbnailPg.translate(THUMBNAIL_TRANSLATE_X, THUMBNAIL_TRANSLATE_Y);
        thumbnailPg.scale(THUMBNAIL_SCALE_X, THUMBNAIL_SCALE_Y);

        thumbnailPg.shape(cube, 0, 0);

        thumbnailPg.endDraw();
    }

    private void setTransformationCube() {

        //Inside magic
        pg.translate(MAIN_TRANSLATE_X, MAIN_TRANSLATE_Y, MAIN_TRANSLATE_X);
        pg.scale(MAIN_SCALE_X, MAIN_SCALE_Y, MAIN_SCALE_X);


        /*//Outside magic
         pg.translate(pg.width/2.0, pg.height/2.0);
         pg.scale(pg.width/2.9,pg.height/2.9);*/
    }


    //Getters and setters
    PGraphics getMainGraphics() {
        return pg;
    }

    PGraphics getThumbnailGraphics() {
        return thumbnailPg;
    }

    Level getCurrentLevel() {
        return front;
    }
}
