package es.geoplanosocial.engine;

import controlP5.ControlP5;
import controlP5.Textfield;
import es.geoplanosocial.levels.Level;
import es.geoplanosocial.levels.LevelCallback;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.simulation.MouseSelectionProvider;
import es.geoplanosocial.tracker.CameraProvider;
import es.geoplanosocial.tracker.Tracker;
import es.geoplanosocial.tracker.TrackerCallback;
import es.geoplanosocial.util.Configuration;
import es.geoplanosocial.util.Types;
import es.geoplanosocial.util.Utils;
import processing.core.*;
import processing.video.Movie;

import java.util.ArrayList;

import static es.geoplanosocial.util.Color.*;
import static es.geoplanosocial.util.Constants.*;
import static es.geoplanosocial.util.Configuration.*;
import static es.geoplanosocial.util.Utils.getWorldColors;

/**
 * World engine
 * Created by gbermejo on 19/04/17.
 */
public class Engine extends PApplet implements TrackerCallback, LevelCallback {

    //Can be changed
    private static final boolean DRAW_FACADE_OUTLINE = true;

    //Background related
    private static PGraphics BG;

    //Cube
    private static Cube worldCube;

    //Black Hole
    private static boolean DRAW_BLACKHOLE = false;
    private static BlackHole blackHole;

    //Zero
    private static Zero zero;

    //Players
    private static final ArrayList<Player> players=new ArrayList<>();

    //BlobsProvider
    private static MouseSelectionProvider blobsProviderSimulation;
    private static CameraProvider blobsProvider;
    //private static MediaLabCVProvider blobsProvider;



    //Tracker
    private static final Tracker tracker=Tracker.getInstance();

    //Current Level
    private static Level currentLevel;

    // Configuration
    private ControlP5 cp5;

    //More
    private Movie moreCompleted;

    /**********************
     *PROCESSING FUNCTIONS*
     **********************/

    public void settings() {
        if(FULLSCREEN){
            fullScreen(SCREEN_RENDERER);
        }else{
            size(SCREEN_WIDTH, SCREEN_HEIGHT, SCREEN_RENDERER);
        }

        smooth(ANTI_ALIASING_LEVEL);//No more aliasing
        //pixelDensity(displayDensity());
        //setTracking();
    }

    public void setup() {

        setTracking();

        //Set global parameters
        frameRate(FPS);
        if(DEBUG)noCursor();//Ugly


        BG = generateFacadeBackground(width, height, SCREEN_RENDERER, DRAW_FACADE_OUTLINE);

        worldCube = new Cube(this, LEVEL_WIDTH, LEVEL_HEIGHT);

        blackHole = new BlackHole(this, LEVEL_WIDTH, LEVEL_HEIGHT);

        zero = new Zero(this);

        Level.init(players,this, this);

        setWorld();

        writeConfigurationInfo();

        moreCompleted = new Movie(this, "more.mov");
        moreCompleted.loop();
    }

    public void writeConfigurationInfo() {
        int hOrigin = 10;
        final int hOrigin_offset = 70;

        int vTextBox = 420;
        final int vSubmitButtom = 650;

        cp5 = new ControlP5(this);

        // text labels
        cp5.addTextfield("tamaño nodo").setPosition(vTextBox, hOrigin).setSize(200, 40).setAutoClear(false);
        cp5.addBang("Submit_tNodo").setPosition(vSubmitButtom, hOrigin).setSize(80, 40);

        hOrigin = hOrigin + hOrigin_offset;
        cp5.addTextfield("label1").setPosition(vTextBox, hOrigin).setSize(200, 40).setAutoClear(false);
        cp5.addBang("Submit_label1").setPosition(vSubmitButtom, hOrigin).setSize(80, 40);

        hOrigin = hOrigin + hOrigin_offset;
        cp5.addTextfield("setROIx").setPosition(vTextBox, hOrigin).setSize(200, 40).setAutoClear(false);

        hOrigin = hOrigin + hOrigin_offset;
        cp5.addTextfield("setROIy").setPosition(vTextBox, hOrigin).setSize(200, 40).setAutoClear(false);

        hOrigin = hOrigin + hOrigin_offset;
        cp5.addTextfield("setROIw").setPosition(vTextBox, hOrigin).setSize(200, 40).setAutoClear(false);

        hOrigin = hOrigin + hOrigin_offset;
        cp5.addTextfield("setROIh").setPosition(vTextBox, hOrigin).setSize(200, 40).setAutoClear(false);
        // last
        hOrigin = hOrigin + hOrigin_offset;
        cp5.addBang("Submit_all").setPosition(vSubmitButtom, hOrigin).setSize(80, 40);

        // radio buttons
        hOrigin = 800;
        vTextBox = 100;
        cp5.addRadioButton("Submit_radioButton_1")
                .setPosition(hOrigin, vTextBox)
                .setSize(40,20)
                .setColorForeground(color(120))
                .setColorActive(color(255))
                .setColorLabel(color(255))
                .addItem("elipseMortal",1);

        vTextBox = vTextBox + hOrigin_offset;
        cp5.addRadioButton("Submit_radioButton_2")
                .setPosition(hOrigin, vTextBox)
                .setSize(40,20)
                .setColorForeground(color(120))
                .setColorActive(color(255))
                .setColorLabel(color(255))
                .addItem("buttom_2",1);
    }

    public void Submit_radioButton_1(int i) {
        Utils.log("botón pultsado:" + i);
        if (i == 1) DRAW_BLACKHOLE = true;
        else if (i == -1) DRAW_BLACKHOLE = false;
    }

    public void Submit_tNodo() {
        Configuration.playerSize = Integer.parseInt(cp5.get(Textfield.class,"tamaño nodo").getText());
        resetPlayerSizes();
    }

    public void Submit_label1() {

    }

    public void Submit_all() {
        Configuration.roi_originX = Integer.parseInt(cp5.get(Textfield.class,"setROIx").getText());
        Configuration.roi_originY = Integer.parseInt(cp5.get(Textfield.class,"setROIy").getText());
        Configuration.roi_width = Integer.parseInt(cp5.get(Textfield.class,"setROIw").getText());
        Configuration.roi_height = Integer.parseInt(cp5.get(Textfield.class,"setROIh").getText());

        System.out.println("OJO: " + Configuration.roi_originX + " - " + Configuration.roi_originX + " - " + Configuration.roi_originX + " - " + Configuration.roi_originX);

        //blobsProvider.initMediaLabCV();
    }

    public void draw() {
        //long startTime = System.nanoTime();


        //Update elements
        update();

        //Draw elements

        drawBackground(BG);//Background

        if(currentLevel!=null && !zero.isOnTransition()){
            drawWorld();//Draw the world

            if (DRAW_BLACKHOLE) drawBlackHole();//Draw the black hole
            drawLevel();//Draw current level
            drawTopInfo();
        }else {
            drawZero();
        }

        if (DEBUG) {
            drawDebug();
        }

        //println("Duration\t" + (System.nanoTime() - startTime));
        //saveFrame("./frames/frame-######.png");//Imagemagick -> convert -delay 60,1000  -loop 0 *.png World1.gif
    }



    //FIXME remove on release
    public void keyPressed() {

        if(DEBUG) {
            if (key == CODED) {
                Types.Direction d;

                switch (keyCode) {
                    case UP:
                        d = Types.Direction.UP;
                        break;
                    case DOWN:
                        d = Types.Direction.DOWN;
                        break;
                    case LEFT:
                        d = Types.Direction.LEFT;
                        break;
                    case RIGHT:
                        d = Types.Direction.RIGHT;
                        break;
                    default:
                        d = Types.Direction.RIGHT;
                }

                worldCube.move(d);
                setLevel();
            }else{
                switch (key) {
                    case '+':
                        blobsProviderSimulation.setNumberOfPlayers(blobsProviderSimulation.getNumberOfPlayers()+1);
                        break;
                    case '-':
                        blobsProviderSimulation.setNumberOfPlayers(blobsProviderSimulation.getNumberOfPlayers()-1);
                        break;
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                        blobsProviderSimulation.setSelectedPlayer(Character.getNumericValue(key));
                        break;
                    default:
                }
                Utils.log("Players: "+ blobsProviderSimulation.getNumberOfPlayers());
            }
        }
    }

    /***************
     *OWN FUNCTIONS*
     ***************/


    /*LOOP FUNCTIONS*/

    //Updates elements and properties
    private void update() {
        //blobsProvider.spoutEvent();

        //Get CV info
        tracker.update();

        //Rotation animation of the current world
        worldCube.update();

        //Black hole animation
        if (DRAW_BLACKHOLE) blackHole.update();

        //Zero zoom
        zero.update();

        //Update elements of the current world
        if(currentLevel!=null)currentLevel.update();

    }

    private void drawBackground(PGraphics pg) {
        //Draw the bg stored in the pg
        image(pg, 0, 0);

        //Top hat bg
        fill(BLACK);
        noStroke();
        beginShape();
        vertex(40, 72);
        vertex(75, 72);
        vertex(75, 56);
        vertex(111, 56);
        vertex(111, 40);
        vertex(160, 40);
        vertex(160, 56);
        vertex(196, 56);
        vertex(196, 72);
        vertex(231, 72);
        endShape(CLOSE);
    }

    private void drawWorld() {

        /*if(worldCube.isOnRotation()){
            image(worldCube.getMainGraphics(), START_WORLD_X, START_WORLD_Y);
        }else{
            drawLevel();
        }*/

        image(worldCube.getMainGraphics(), START_WORLD_X, START_WORLD_Y);

    }

    private void drawBlackHole(){
        image(blackHole.getGraphics(), START_WORLD_X, START_WORLD_Y);
    }

    private void drawLevel() {
        currentLevel.draw();
        PGraphics p = currentLevel.getGraphics();

        if (DRAW_BLACKHOLE) p.mask(blackHole.getMask());

        image(p, START_WORLD_X, START_WORLD_Y);
    }

    private  void drawZero(){
        zero.draw();
        image(zero.getResult(), START_WORLD_X, START_WORLD_Y-HAT_OFFSET);
    }

    private void drawTopInfo() {

        if(worldCube.isWorldCompleted()){
            PImage p = moreCompleted.get();
            p.resize(moreCompleted.width/4,moreCompleted.height/4);
            image(p, START_THUMBNAIL_X-6, START_THUMBNAIL_Y-5);
        }
        else
            image(worldCube.getThumbnailGraphics(), START_THUMBNAIL_X, START_THUMBNAIL_Y);//Draw thumbnail of world

        //FIXME
        /*
        if(blobsProvider.isTracking()) {
            PImage s = blobsProvider.getSource();

            int xOffset = width-s.width;
            int yOffset = height-s.height;


            image(blobsProvider.getMediaLabCVInputFrame(),0,yOffset);

            image(blobsProvider.getMediaLabCVOutputFrame(), xOffset, yOffset);

            for (Player p : players) {
                Rectangle bb = p.getBoundingBox();
                noStroke();
                int color;
                switch (p.getState()) {
                    case PLAYING:
                        color = GREEN;
                        break;
                    case GHOST:
                        color = ORANGE;
                        break;
                    case MISSING:
                    default:
                        color = RED;
                        break;
                }
                fill(color);
                ellipse((bb.x + 0.0f) / LEVEL_WIDTH * s.width,  yOffset+(bb.y + 0.0f) / LEVEL_HEIGHT * s.height, bb.width, bb.height);
                ellipse(xOffset+(bb.x + 0.0f) / LEVEL_WIDTH * s.width, yOffset + (bb.y + 0.0f) / LEVEL_HEIGHT * s.height, bb.width, bb.height);
            }

        }*/


    }


    private void drawDebug() {
        fill(MAGENTA);
        textSize(32);
        textAlign(LEFT, TOP);
        String text = currentLevel==null?"Zero":currentLevel.getId()+"("+currentLevel.getTitle()+")";
        text(text, 0, 0);

    }

    /*OTHER FUNCTIONS*/


    private void setTracking(){
        if(DEBUG){
            blobsProviderSimulation =new MouseSelectionProvider(this,0, playerSize);
            Tracker.init(players,this, blobsProviderSimulation);
            //blobsProvider =new CameraProvider();
            //Tracker.init(players,this, blobsProvider);
        }else{
            blobsProvider =new CameraProvider();
            //blobsProvider =new MediaLabCVProvider(this);
            //blobsProvider.initVideo();
            //blobsProvider.initCamera();
            //blobsProvider.initSpout();
            Tracker.init(players,this, blobsProvider);
        }
    }

    private void setWorld() {
        currentLevel=null;
        if(players.size()>0) {
            int w = players.size()<=5?players.size():5;
            worldCube.setWorldColors(getWorldColors(w));
            worldCube.resetLevelCompletion();
            setLevel();
        }
    }

    private void setLevel() {
        resetPlayerSizes();

        int w = players.size()<=5?players.size():5;
        Level l= Level.Factory.getLevel(w, worldCube.getCurrentLevel());
        if(l!=null){
            currentLevel=l;
            Utils.log("Init: "+currentLevel.getId());
        }else{
            Utils.log("Error setting new level");
        }


    }

    //FIXME this may disappear when camera attached
    private void resetPlayerSizes() {
        for(Player p : players){
            p.getBoundingBox().setSize(playerSize, playerSize);
        }
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




    /*******************
     *TRACKER FUNCTIONS*
     *******************/

    @Override
    public void newPlayers(ArrayList<Player> newPlayers) {
        if(currentLevel!=null){
            currentLevel.addPlayers(newPlayers);
        }else{//Zero
            for (Player p : newPlayers){
                players.add(Player.Factory.getPlayer(Player.Type.NODE, ALPHA, p));
            }
        }
    }


    @Override
    public void changeWorld() {
        Utils.log("Change world! Players: "+players.size());

        int strokeWeight = 0;
        boolean[] c = worldCube.getCompletion();
        for(int i =0;i<c.length;i++){
            if(c[i]){
                strokeWeight++;
            }
        }
        setWorld();
        zero.setStrokeWeight(strokeWeight);
        zero.changeWord(players.size());
    }



    @Override
    public void changeLevel(Types.Direction direction) {
        resetPlayerSizes();
        worldCube.move(direction);
        setLevel();
    }

    /*
    public void movieEvent(Movie m) {
        m.read();
        blobsProvider.sendFrame(m.get());

    }

    public void captureEvent(Capture c) {
        c.read();
        blobsProvider.sendFrame(c.get());
    }
    */

    /****************
     *ZERO FUNCTIONS*
     ****************/
    public void movieEvent(Movie m) {
        m.read();
    }



    /*****************
     *LEVEL FUNCTIONS*
     *****************/
    @Override
    public void nextLevel() {
        worldCube.setCurrentComplete();
        boolean[] c = worldCube.getCompletion();
        Types.Direction d = null;
        for(int i =1;i<c.length;i++){
            if(!c[i]){
                if(i==1)d= Types.Direction.RIGHT;
                else d=Types.Direction.UP;
                break;
            }
        }
        if(d!=null)changeLevel(d);
    }

    @Override
    public boolean getCurrentLevelCompletion() {
        return worldCube.isCurrentComplete();
    }
}

