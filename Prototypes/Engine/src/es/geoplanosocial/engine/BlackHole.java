//package es.geoplanosocial.engine;
//
//import processing.core.PApplet;
//import processing.core.PGraphics;
//
//import static es.geoplanosocial.util.Color.*;
//import static es.geoplanosocial.util.Constants.*;
//
//import static processing.core.PApplet.cos;
//import static processing.core.PApplet.sin;
//
//import static processing.core.PConstants.ADD;
//import static processing.core.PConstants.CENTER;
//import static processing.core.PConstants.BLUR;
//import static processing.core.PConstants.INVERT;
//
//
///**
// * Created by gbermejo on 5/08/17.
// */
//public class BlackHole {
//
//    private final PGraphics ringPg[]= new PGraphics[3];
//    private final int ringColor[] = {RED, GREEN, BLUE};
//
//    private final PGraphics backgroundPg, intermediatePg, resultPg;
//
//    private float angle = 0;
//    private final float offsetAngle = (float)(2.0*Math.PI/ringPg.length);
//    private final float limit = BLACK_HOLE_THICKNESS_RING*BLACK_HOLE_MOVE_PERCENTAGE;
//
//    private final PGraphics maskPg;
//
//
//    public BlackHole(PApplet processing, int width, int height) {
//
//        //Rings
//        for(int i=0;i<ringPg.length;i++){
//            //Init
//            ringPg[i] = processing.createGraphics(width, height, SCREEN_RENDERER);
//            ringPg[i].smooth(ANTI_ALIASING_LEVEL);
//
//            //Draw ring
//            ringPg[i].beginDraw();
//
//            ringPg[i].clear();
//
//            ringPg[i].noFill();
//            ringPg[i].stroke(ringColor[i]);
//            ringPg[i].strokeWeight(BLACK_HOLE_THICKNESS_RING);
//
//            ringPg[i].ellipse (ringPg[i].width/2,ringPg[i].height/2,ringPg[i].width*BLACK_HOLE_SIZE_PERCENTAGE,ringPg[i].height*BLACK_HOLE_SIZE_PERCENTAGE);
//
//            ringPg[i].endDraw();
//        }
//
//        //Mask
//        maskPg = processing.createGraphics(width, height, SCREEN_RENDERER);
//        maskPg.smooth(ANTI_ALIASING_LEVEL);
//        maskPg.beginDraw();
//        maskPg.background(WHITE);
//        maskPg.fill(BLACK);
//        maskPg.ellipse (maskPg.width/2,maskPg.height/2,
//                maskPg.width*BLACK_HOLE_SIZE_PERCENTAGE-BLACK_HOLE_THICKNESS_RING,
//                maskPg.height*BLACK_HOLE_SIZE_PERCENTAGE-BLACK_HOLE_THICKNESS_RING);
//        maskPg.filter(INVERT);
//        maskPg.endDraw();
//
//
//        //Background oval
//        backgroundPg = processing.createGraphics(width, height, SCREEN_RENDERER);
//        backgroundPg.smooth(ANTI_ALIASING_LEVEL);
//        backgroundPg.beginDraw();
//        backgroundPg.fill(BLACK_HOLE_COLOR);
//        backgroundPg.ellipse (backgroundPg.width/2,backgroundPg.height/2,backgroundPg.width*BLACK_HOLE_SIZE_PERCENTAGE,backgroundPg.height*BLACK_HOLE_SIZE_PERCENTAGE);
//        backgroundPg.endDraw();
//
//        //Blending of rings
//        intermediatePg = processing.createGraphics(width, height, SCREEN_RENDERER);
//        intermediatePg.smooth(ANTI_ALIASING_LEVEL);
//        intermediatePg.beginDraw();
//        intermediatePg.blendMode(ADD);
//        intermediatePg.imageMode(CENTER);
//        intermediatePg.filter(BLUR,1.0f);
//        intermediatePg.endDraw();
//
//        //Output to engine
//        resultPg = processing.createGraphics(width, height, SCREEN_RENDERER);
//        resultPg.smooth(ANTI_ALIASING_LEVEL);
//        resultPg.beginDraw();
//        resultPg.imageMode(CENTER);
//        resultPg.filter(BLUR,1.0f);
//        resultPg.endDraw();
//
//    }
//
//    void update(){
//        angle+=BLACK_HOLE_MOVE_STEP;
//        draw();
//    }
//
//
//    private void draw() {
//        drawRings();
//        drawResult();
//    }
//
//    private void drawResult(){
//        resultPg.beginDraw();
//        resultPg.clear();
//        //resultPg.image(backgroundPg, resultPg.width/2,resultPg.height/2, resultPg.width, resultPg.height);
//        resultPg.image(intermediatePg, resultPg.width/2,resultPg.height/2, resultPg.width, resultPg.height);
//        resultPg.endDraw();
//    }
//
//    private void drawRings(){
//
//        intermediatePg.beginDraw();
//
//        intermediatePg.clear();
//
//
//        for(int i=0;i<ringPg.length;i++){
//            float a = angle+offsetAngle*i;
//            intermediatePg.image(ringPg[i], intermediatePg.width/2+cos(a)*limit,intermediatePg.height/2+sin(a)*limit, intermediatePg.width, intermediatePg.height);
//        }
//
//        intermediatePg.endDraw();
//    }
//
//    PGraphics getGraphics() {
//        return resultPg;
//    }
//
//    PGraphics getMask() {
//
//        return maskPg;
//    }
//
//}



package es.geoplanosocial.engine;

import es.geoplanosocial.util.Utils;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.*;

import static es.geoplanosocial.util.Color.*;
import static es.geoplanosocial.util.Constants.*;

import static es.geoplanosocial.util.Utils.randomInt;
import static processing.core.PApplet.cos;
import static processing.core.PApplet.min;
import static processing.core.PApplet.sin;

import static processing.core.PConstants.ADD;
import static processing.core.PConstants.CENTER;
import static processing.core.PConstants.BLUR;
import static processing.core.PConstants.INVERT;


/**
 * Created by gbermejo on 5/08/17.
 */
public class BlackHole {



    private final PGraphics resultPg;

    private float angle = 0;
    private final float limit = 0;

    private final PGraphics maskPg;


    private final int DUMMIES_NUMBER = 32;
    private Rectangle[] DUMMIES ;


    private final float DUMMY_SPEED = 0.01f;
    private final int DUMMY_POSITION_OFFSET;

    private long timer = System.currentTimeMillis();


    public BlackHole(PApplet processing, int width, int height) {



        //Mask
        maskPg = processing.createGraphics(width, height, SCREEN_RENDERER);
        maskPg.smooth(ANTI_ALIASING_LEVEL);
        maskPg.beginDraw();
        maskPg.background(WHITE);
        maskPg.fill(BLACK);
        maskPg.ellipse (maskPg.width/2,maskPg.height/2,
                maskPg.width*BLACK_HOLE_SIZE_PERCENTAGE,
                maskPg.height*BLACK_HOLE_SIZE_PERCENTAGE);
        maskPg.filter(INVERT);
        maskPg.endDraw();


        //Output to engine
        resultPg = processing.createGraphics(width, height, SCREEN_RENDERER);
        resultPg.smooth(ANTI_ALIASING_LEVEL);
        resultPg.beginDraw();
        resultPg.imageMode(CENTER);
        resultPg.filter(BLUR,1.0f);
        resultPg.noStroke();
        resultPg.fill(BLACK_HOLE_COLOR, 32);
        resultPg.endDraw();

        DUMMY_POSITION_OFFSET = min(width,height)/2;

        generateDummies();

    }

    private void generateDummies() {
        //Generate all dummies at random places
        DUMMIES = new Rectangle[DUMMIES_NUMBER];
        for(int i=0; i<DUMMIES.length;i++){
            DUMMIES[i] = generateDummy();
        }
    }


    private Rectangle generateDummy(){

        int width = (int)(resultPg.width*BLACK_HOLE_SIZE_PERCENTAGE);
        int height = (int)(resultPg.height*BLACK_HOLE_SIZE_PERCENTAGE);
        int x = resultPg.width/2;
        int y = resultPg.height/2;

        return new Rectangle(x, y, width, height);
    }


    void update(){
        angle+=BLACK_HOLE_MOVE_STEP;
        moveDots();
        draw();
    }

    private void moveDots() {
        long currentTime = System.currentTimeMillis();
        long interval = currentTime -timer;

        float targetX = resultPg.width/2+cos(angle)*limit;
        float targetY = resultPg.height/2+sin(angle)*limit;

        float distanceInInterval = DUMMY_SPEED * interval;

        //Move dummies towards player position
        for(int i=0; i<DUMMIES.length;i++){

            PVector v = new PVector(targetX-DUMMIES[i].x+ Utils.randomInt(-DUMMY_POSITION_OFFSET,DUMMY_POSITION_OFFSET),
                    targetY-DUMMIES[i].y+Utils.randomInt(-DUMMY_POSITION_OFFSET,DUMMY_POSITION_OFFSET));

            //TODO maybe Verlet integration and circles collisions?
            float sizeFactor = 1;
            float distancePerSize = distanceInInterval * sizeFactor;
            v.normalize();
            v.mult(distancePerSize);

            DUMMIES[i].x += v.x > 0 ? Math.ceil(v.x):Math.floor(v.x);
            DUMMIES[i].y += v.y > 0 ? Math.ceil(v.y):Math.floor(v.y);

        }

        timer = currentTime;
    }

    private void draw() {
        drawResult();
    }

    private void drawResult(){
        resultPg.beginDraw();
        resultPg.clear();
        for(int i=0; i<DUMMIES.length;i++){
            if(DUMMIES[i]!=null)
                resultPg.ellipse(DUMMIES[i].x, DUMMIES[i].y, DUMMIES[i].width, DUMMIES[i].height);
        }
        resultPg.endDraw();
    }

    PGraphics getGraphics() {
        return resultPg;
    }

    PGraphics getMask() {

        return maskPg;
    }

}