package es.geoplanosocial.engine;

import es.geoplanosocial.util.Color;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.video.Movie;

import java.awt.*;

import static es.geoplanosocial.util.Constants.*;
import static processing.core.PApplet.lerp;
import static processing.core.PApplet.sqrt;

/**
 * Created by guzman on 17/10/2017.
 */
public class Zero {
    private PApplet parent;

    private Movie movie;

    private PGraphics pg;


    private float amount = 0.0f;

    private final float SCALE_MIN = 1.0f;
    private final float SCALE_MAX = 14.0f;

    private final float ZOOM_TIME = 15.0f;//Seconds

    private final float SCALE_FACTOR = (SCALE_MAX-SCALE_MIN)/(ZOOM_TIME*FPS);


    private boolean onTransition = false;

    private boolean isZoomed = false;

    private int currentWorld=0;
    private int destinationWorld=0;
    private int targetWorld=0;


    private final Point[] zoomPoints = new Point[]{
            new Point(0,0),//World 0
            new Point(96,35),//World 1
            new Point(160,33),//World 2
            new Point(162,95),//World 3
            new Point(94,97),//World 4
            new Point(28,63),//World 5
    };

    public Zero(PApplet parent) {
        this.parent = parent;

        pg = parent.createGraphics(LEVEL_WIDTH, LEVEL_HEIGHT, SCREEN_RENDERER);

        movie = new Movie(parent, "zero.mp4");
        movie.loop();

        setZoomed(false);
        //changeWord(5);
    }

    public void changeWord(int destinationWorld){
        this.destinationWorld = destinationWorld;
        onTransition = true;
        setZoomed(isZoomed);
    }

    public void setZoomed(boolean zoomed) {
        isZoomed = zoomed;
        currentWorld = isZoomed?targetWorld:0;
        targetWorld = isZoomed?currentWorld:destinationWorld;
    }

    public void update(){
        if(onTransition) {
            amount += isZoomed ? -SCALE_FACTOR : SCALE_FACTOR;
            amount = parent.constrain(amount, 0.0f, 1.0f);

            if(amount<=0){
                setZoomed(false);
            }else if(amount>=1){
                setZoomed(true);
            }

            if(currentWorld==destinationWorld){
                onTransition=false;
                //int to = (destinationWorld % 5)+1;
                //changeWord(to);
            }
        }
    }

    public void draw(){
        Point p = zoomPoints[targetWorld];

        float easeInOut = (float) (1 + Math.sin(Math.PI * amount - Math.PI / 2)) / 2;

        //eInEout = amount<.5 ? 2*amount*amount : -1+(4-2*amount)*amount;

        float s = lerp(SCALE_MIN, SCALE_MAX, easeInOut);
        float scaleChange = s - SCALE_MIN;

        PImage frame = movie.copy();
        frame.resize(PApplet.round(
                s*LEVEL_WIDTH),
                PApplet.round(s*LEVEL_HEIGHT));
        PImage r = frame.get(
                PApplet.round(p.x*scaleChange),
                PApplet.round(p.y*scaleChange),
                LEVEL_WIDTH,
                LEVEL_HEIGHT);

        pg.beginDraw();
        pg.background(Color.BLACK);
        pg.image(r,0,0);
        pg.endDraw();

    }


    public PGraphics getGraphics() {
        return pg;
    }
    public boolean isOnTransition() {
        return onTransition;
    }
}
