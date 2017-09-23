package es.geoplanosocial.engine;

import controlP5.ControlP5;
import controlP5.Textfield;
import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.simulation.MouseSelectionProvider;
import es.geoplanosocial.tracker.CameraProvider;
import es.geoplanosocial.tracker.Tracker;
import es.geoplanosocial.tracker.TrackerCallback;
import es.geoplanosocial.util.Configuration;
import es.geoplanosocial.util.Types;
import es.geoplanosocial.util.Utils;
import processing.core.*;

import java.util.ArrayList;

import static es.geoplanosocial.util.Color.*;
import static es.geoplanosocial.util.Constants.*;
import static es.geoplanosocial.util.Configuration.*;
import static es.geoplanosocial.util.Utils.getWorldColors;

/**
 * World engine
 * Created by gbermejo on 19/04/17.
 */
public class Engine extends PApplet implements TrackerCallback {

    //Can be changed
    private static final boolean DRAW_FACADE_OUTLINE = true;

    //Background related
    private static PGraphics BG;

    //Cube
    private static Cube worldCube;

    //Black Hole
    private static final boolean DRAW_BLACKHOLE = false;
    private static BlackHole blackHole;

    //Players
    private static final ArrayList<Player> players=new ArrayList<>();

    //BlobsProvider
    private static MouseSelectionProvider blobsProviderSimulation;
    private static CameraProvider blobsProvider;


    //Tracker
    private static final Tracker tracker=Tracker.getInstance();

    //Current Level
    private static Level currentLevel;

    // Configuration
    ControlP5 cp5;

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
        setTracking();
    }

    public void setup() {

        //Set global parameters
        frameRate(FPS);
        // noCursor();//Ugly


        BG = generateFacadeBackground(width, height, SCREEN_RENDERER, DRAW_FACADE_OUTLINE);

        worldCube = new Cube(this, LEVEL_WIDTH, LEVEL_HEIGHT);

        if (DRAW_BLACKHOLE) blackHole = new BlackHole(this, LEVEL_WIDTH, LEVEL_HEIGHT);

        Level.init(players,this);

        setWorld();

        writeConfigurationInfo();
    }

    public void writeConfigurationInfo() {
        cp5 = new ControlP5(this);
        cp5.addTextfield("tamaño nodo").setPosition(420, 100).setSize(200, 40).setAutoClear(false);
        cp5.addTextfield("otra cosa").setPosition(420, 170).setSize(200, 40).setAutoClear(false);
        cp5.addBang("Submit").setPosition(640, 170).setSize(80, 40);
    }


    public void Submit() {
        Utils.log("the following text was submitted :");
        String tNodo = cp5.get(Textfield.class,"tamaño nodo").getText();
        Utils.log(" tamaño nodo = " + tNodo);

        Configuration.PLAYER_SIZE = Integer.parseInt(cp5.get(Textfield.class,"tamaño nodo").getText());
    }

    public void draw() {
        //long startTime = System.nanoTime();


        //Update elements
        update();

        //Draw elements

        drawBackground(BG);//Background

        drawWorld();//Draw the world

        if (DRAW_BLACKHOLE) drawBlackHole();//Draw the black hole

        drawLevel();//Draw current level

        drawTopInfo();

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
        //Get CV info
        tracker.update();

        //Rotation animation of the current world
        worldCube.update();

        //Black hole animation
        if (DRAW_BLACKHOLE) blackHole.update();

        //Update elements of the current world
        currentLevel.update();

    }

    private void drawBackground(PGraphics pg) {
        //Draw the bg in the pg
        image(pg, 0, 0);
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

    private void drawTopInfo() {

        //Draw thumbnail of world
        image(worldCube.getThumbnailGraphics(), START_THUMBNAIL_X, START_THUMBNAIL_Y);
    }


    private void drawDebug() {
        textSize(32);
        textAlign(LEFT, TOP);
        text(currentLevel.getId()+"("+currentLevel.getTitle()+")", 0, 0);
    }

    /*OTHER FUNCTIONS*/


    private void setTracking(){
        if(DEBUG){
            blobsProviderSimulation =new MouseSelectionProvider(this,1,PLAYER_SIZE);
            Tracker.init(players,this, blobsProviderSimulation);
            //blobsProvider =new CameraProvider();
            //Tracker.init(players,this, blobsProvider);
        }else{
            blobsProvider =new CameraProvider();
            Tracker.init(players,this, blobsProvider);
        }
    }

    private void setWorld() {

        worldCube.setWorldColors(getWorldColors(players.size()));
        setLevel();
    }

    private void setLevel() {
        resetPlayerSizes();

        Level l= Level.Factory.getLevel(players.size(), worldCube.getCurrentLevel());
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
            p.getBoundingBox().setSize(PLAYER_SIZE, PLAYER_SIZE);
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
        currentLevel.addPlayers(newPlayers);
    }


    @Override
    public void changeWorld() {
        Utils.log("Change world! Players: "+players.size());
        setWorld();
    }



    @Override
    public void changeLevel(Types.Direction direction) {
        resetPlayerSizes();
        worldCube.move(direction);
        setLevel();
    }


}
