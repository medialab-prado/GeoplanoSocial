package es.geoplanosocial.engine;

import es.geoplanosocial.util.Color;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.video.Movie;

import java.awt.*;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import static es.geoplanosocial.util.Constants.*;
import static es.geoplanosocial.util.Utils.getResourcePath;
import static es.geoplanosocial.util.Utils.isRunningFromJar;
import static es.geoplanosocial.util.Utils.randomInt;
import static processing.core.PApplet.constrain;
import static processing.core.PApplet.lerp;
import static processing.core.PConstants.CLOSE;
import static processing.core.PConstants.ROUND;

/**
 * Created by guzman on 17/10/2017.
 */
public class Zero {
    private PApplet parent;

    private Movie movie;

    private PGraphics pg;
    private PGraphics pgTangle;
    private PImage result;

    private PGraphics maskPg;

    private float amount = 0.0f;

    private final float SCALE_MIN = 1.0f;
    private final float SCALE_MAX = 14.0f;

    private final float ZOOM_TIME_IN = 10.0f;//Seconds
    private final float ZOOM_TIME_OUT = 14.0f;//Seconds


    private final float SCALE_FACTOR_IN = (SCALE_MAX-SCALE_MIN)/(ZOOM_TIME_IN*FPS);
    private final float SCALE_FACTOR_OUT = (SCALE_MAX-SCALE_MIN)/(ZOOM_TIME_OUT*FPS);


    private final int MAX_TANGLE_OFFSET = 5;//Pixels

    private final int MIN_STROKE = 1;//Pixels
    private final int MAX_STROKE = 3;//Pixels

    private final String VIDEO_NAME = "zero.mov";

    private boolean onTransition = false;

    private boolean isZoomed = false;

    private int currentWorld=0;
    private int destinationWorld=0;
    private int targetWorld=0;

    private Point lastTangle;

    private int strokeWeight=0;


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

        this.result=new PImage(1,1);

        if(isRunningFromJar()) {
            File root = new File(".");
            File video = new File(root, "video");
            File plugins = new File(video, "plugins");
            System.setProperty("gstreamer.library.path", video.getPath());
            System.setProperty("gstreamer.plugin.path", plugins.getPath());
        }

        movie = new Movie(parent, getResourcePath(VIDEO_NAME));
        movie.loop();

        setZoomed(false);
        //changeWord(1);

        createMask();
    }

    private static File[] getResourceFolderFiles (String folder) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(folder);
        String path = url.getPath();
        return new File(path).listFiles();
    }


    public void changeWord(int destinationWorld){

        this.destinationWorld = destinationWorld<zoomPoints.length?destinationWorld:zoomPoints.length-1;
        onTransition = true;
        setZoomed(isZoomed);
        if(currentWorld>0 && destinationWorld>0){
            drawTangleLine();
            strokeWeight=0;
        }else{
            lastTangle = null;
        }
    }

    public void setZoomed(boolean zoomed) {
        isZoomed = zoomed;
        currentWorld = isZoomed?targetWorld:0;
        targetWorld = isZoomed?currentWorld:destinationWorld;
    }

    public void update(){
        if(onTransition) {
            amount += isZoomed ? -SCALE_FACTOR_OUT : SCALE_FACTOR_IN;
            amount = parent.constrain(amount, 0.0f, 1.0f);

            if(amount<=0){
                setZoomed(false);
            }else if(amount>=1){
                setZoomed(true);
            }

            if(currentWorld==destinationWorld){
                onTransition=false;
                //int to = (destinationWorld % 5)+1;
                /*int to;
                do {
                    to = randomInt(1,5);
                }while (to==currentWorld);
                changeWord(to);
                */
            }
        }
    }

    public void draw(){
        if(movie.available()) {

            if(pg==null){
                pg = parent.createGraphics(movie.width, movie.height, SCREEN_RENDERER);
                pgTangle = parent.createGraphics(movie.width, movie.height, SCREEN_RENDERER);
            }


            Point p = zoomPoints[targetWorld];

            float easeInOut = (float) (1 + Math.sin(Math.PI * amount - Math.PI / 2)) / 2;

            //eInEout = amount<.5 ? 2*amount*amount : -1+(4-2*amount)*amount;

            float s = lerp(SCALE_MIN, SCALE_MAX, easeInOut);
            float scaleChange = s - SCALE_MIN;


            pg.beginDraw();
            pg.background(Color.BLACK);
            pg.image(pgTangle,0,0);
            pg.image(movie.copy(), 0, 0);
            pg.endDraw();

            PImage r =pg.get();
            r.resize(PApplet.round(s * LEVEL_WIDTH),
                    PApplet.round(s * LEVEL_HEIGHT));


            result = r.get(
                    PApplet.round(p.x * scaleChange),
                    PApplet.round(p.y * scaleChange)-HAT_OFFSET,
                    LEVEL_WIDTH,
                    LEVEL_HEIGHT+HAT_OFFSET);

            result.mask(maskPg);
        }

    }


    public PImage getResult() {
        return result;
    }
    public boolean isOnTransition() {
        return onTransition;
    }

    private Point randomPoint(int world){

        double widthCoeff= (pgTangle.width+0.0)/LEVEL_WIDTH;
        double heightCoeff= (pgTangle.height+0.0)/LEVEL_HEIGHT;
        Point p = zoomPoints[world];

        int xMaxOffset = (int)(widthCoeff*MAX_TANGLE_OFFSET);
        int yMaxOffset = (int)(heightCoeff*MAX_TANGLE_OFFSET);

        int xOffset = randomInt(-xMaxOffset,xMaxOffset);
        int yOffset = randomInt(-yMaxOffset,yMaxOffset);

        return new Point((int)(p.getX()*widthCoeff+xOffset), (int)(p.getY()*heightCoeff+yOffset));
    }


    private void drawTangleLine(){
        pgTangle.beginDraw();

        pgTangle.stroke(Color.WHITE);
        pgTangle.strokeCap(ROUND);
        pgTangle.strokeWeight(strokeWeight);


        if(lastTangle==null)lastTangle=randomPoint(currentWorld);
        Point dest = randomPoint(destinationWorld);

        pgTangle.line((float)lastTangle.getX(),
                (float)lastTangle.getY(),
                (float)dest.getX(),
                (float)dest.getY());

        lastTangle = dest;
        pgTangle.endDraw();
    }


    private void createMask(){
        maskPg = parent.createGraphics(LEVEL_WIDTH, LEVEL_HEIGHT+HAT_OFFSET, SCREEN_RENDERER);
        maskPg.smooth(ANTI_ALIASING_LEVEL);
        maskPg.beginDraw();
        maskPg.background(Color.BLACK);
        maskPg.translate(-START_WORLD_X,-START_WORLD_X);
        maskPg.fill(Color.WHITE);
        maskPg.noStroke();
        maskPg.beginShape();
        maskPg.vertex(232, 198);
        maskPg.vertex(40, 198);
        maskPg.vertex(40, 72);
        maskPg.vertex(75, 72);
        maskPg.vertex(75, 56);
        maskPg.vertex(111, 56);
        maskPg.vertex(111, 40);
        maskPg.vertex(160, 40);
        maskPg.vertex(160, 56);
        maskPg.vertex(196, 56);
        maskPg.vertex(196, 72);
        maskPg.vertex(232, 72);
        maskPg.endShape(CLOSE);
        maskPg.endDraw();
    }

    public void setStrokeWeight(int strokeWeight) {
        this.strokeWeight = constrain(strokeWeight,MIN_STROKE,MAX_STROKE);
    }
}
