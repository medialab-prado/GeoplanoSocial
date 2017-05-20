package es.geoplanosocial.levels;

import es.geoplanosocial.players.Player;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.ArrayList;

import static es.geoplanosocial.util.Constants.*;

/**
 * Abstract generic level
 * Created by gbermejo on 15/05/17.
 */
public abstract class Level {

    private final String id;
    private final String title;
    private final int mainColor;

    protected final PGraphics pg;
    protected final long startTime;

    private boolean doFrameClear =true;
    private boolean doDrawPlayers=true;

    protected Level(String title, int mainColor) {
        this.id = getClass().getSimpleName();
        this.title = title;
        this.mainColor = mainColor;
        this.pg = processing.createGraphics(LEVEL_WIDTH, LEVEL_HEIGHT, SCREEN_RENDERER);
        this.startTime=System.currentTimeMillis();

        setup();
    }

    protected static ArrayList<Player> players;
    protected static PApplet processing;


    public static void init(ArrayList<Player> players,PApplet processing){
        Level.players=players;
        Level.processing=processing;
    }



    public void draw(){
        if(doFrameClear) {
            //Set plain background
            pg.beginDraw();
            pg.background(mainColor);
            pg.endDraw();
        }
        if(doDrawPlayers) {
            //Draw players
            for(Player p : players){
                p.draw(pg);
            }
        }
        drawLevel();
        processing.image(pg, START_WORLD_X, START_WORLD_Y);
    }

    public abstract void update();

    protected abstract void drawLevel();
    protected abstract void setup();


    public void setDoFrameClear(boolean doFrameClear) {
        this.doFrameClear = doFrameClear;
    }

    public void setDoDrawPlayers(boolean doDrawPlayers) {
        this.doDrawPlayers = doDrawPlayers;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getMainColor() {
        return mainColor;
    }

    protected static void refreshPlayers(ArrayList<Player> players) {
        Level.players.clear();
        Level.players.addAll(players);
    }

}
