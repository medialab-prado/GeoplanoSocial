package es.geoplanosocial.tracker;

import gbr.medialabcv.MediaLabCV;
import processing.core.PApplet;
import processing.core.PImage;
import processing.video.Capture;
import processing.video.Movie;
import spout.Spout;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static es.geoplanosocial.util.Constants.*;
import static processing.core.PConstants.ARGB;

/**
 * Gets tracking info by library (MediaLabCV).
 * Created by gbermejo on 21/09/2017.
 */
public class MediaLabCVProvider implements BlobsProvider {

    private  PApplet parent;

    private ArrayList<Blob> players;

    private boolean isTracking;

    private MediaLabCV mediacv;


    private PImage source;

    private PImage lastFrame;

    private Spout spout;

    private int minBBWidth, maxBBWidth, minBBHeight, maxBBHeight;

    public MediaLabCVProvider(PApplet parent) {

        this.parent = parent;

        this.isTracking = false;


        this.players = new ArrayList<>();

        this.mediacv = new MediaLabCV(parent);


    }

    public void initVideo(){
        Movie video = new Movie(parent, MLCV_TEST_VIDEO);
        video.loop();
        video.play();

        source = video;
    }

    public void initCamera(){
        String[] cameras = Capture.list();
        System.out.println(Arrays.toString(cameras));

        Capture capture = new Capture(parent, cameras[0]);
        capture.start();

        source = capture;
    }

    public void initSpout(){
        spout = new Spout(parent);
        source=parent.createImage(1, 1, ARGB);//Initial size
        spout.receivePixels(source);//Init
    }

    public void spoutEvent(){
        spout.receivePixels(source);
        sendFrame(source.get());
    }

    public void initMediaLabCV(){
        this.mediacv.init(source.width, source.height);
        this.mediacv.disableRoi();
        this.mediacv.createBackgroundSubtractorKNN(MLCV_HISTORY,MLCV_DIST_TO_THRESHOLD, MLCV_DETECT_SHADOWS);
        this.isTracking =true;
    }


    public void sendFrame(PImage frame){
        if(frame.width>0 && frame.height>0) {
            if (!isTracking()) {
                System.out.println("Screen: " + parent.width + "x" + parent.height);
                System.out.println("Frame: " + frame.width + "x" + frame.height);

                initMediaLabCV();
                setBBSizes(frame.width, frame.height);
            }
            onNewFrame(frame);
        }
    }


    public void onNewFrame(PImage frame){
        lastFrame=frame;
        if(isTracking) {
            mediacv.setInput(frame);
            mediacv.applyBackgroundSubtractorKNN();//Auto

            mediacv.erode(MLCV_ERODE_SIZE,MLCV_ERODE_ITERATIONS);//Noise removal

            mediacv.dilate(MLCV_DILATE_SIZE,MLCV_DILATE_ITERATIONS);//Dilate
            Rectangle[] boundingBoxes = mediacv.findBoundingBoxes(
                    MLCV_ADJACENT_MERGE,
                    MLCV_SHADOW_REMOVAL,
                    MLCV_THRESHOLD,
                    minBBWidth,
                    maxBBWidth,
                    minBBHeight,
                    maxBBHeight);

            boundingBoxes = mediacv.track(boundingBoxes);
            Rectangle [] nodes = mediacv.getNodes(boundingBoxes);

            players.clear();
            for(int i=0;i<nodes.length;i++){
                Blob b = createBlob(i, nodes[i].x, nodes[i].y);
                players.add(b);
            }
            /*for(Rectangle rect : boundingBoxes) {
                System.out.println("new Rectangle("+rect.x+","+rect.y+","+rect.width+","+rect.height+"),");
            }
            System.out.println("-----------------------");*/
        }
    }


    @Override
    public Blob[] fetchPositions() {
        return players.toArray(new Blob[players.size()]);
    }



    private Blob createBlob(int id, float x, float y) {
        return new Blob(String.valueOf(id), new Rectangle(PApplet.round(x/source.width*LEVEL_WIDTH), PApplet.round(y/source.height*LEVEL_HEIGHT), PLAYER_SIZE, PLAYER_SIZE));
    }

    /*******************
     *GETTERS & SETTERS*
     ******************
     * @param width
     * @param height*/


    private void setBBSizes(int width, int height) {
        minBBWidth=(int)Math.round(MLCV_BB_MIN_WIDTH_PERCENTAGE*width);
        maxBBWidth=(int)Math.round(MLCV_BB_MAX_WIDTH_PERCENTAGE*width);

        minBBHeight=(int)Math.round(MLCV_BB_MIN_HEIGHT_PERCENTAGE*height);
        maxBBHeight=(int)Math.round(MLCV_BB_MAX_HEIGHT_PERCENTAGE*height);

    }

    public PImage getSource() {
        return source;
    }

    public boolean isTracking() {
        return isTracking;
    }

    public PImage getMediaLabCVOutputFrame(){
        return mediacv.getOutput();
    }

    public PImage getMediaLabCVInputFrame(){
        //return mediacv.getInput();
        return lastFrame;
    }
}
