package es.geoplanosocial.levels;

import es.geoplanosocial.players.Player;
import es.geoplanosocial.players.VisiblePlayer;
import es.geoplanosocial.util.Color;
import es.geoplanosocial.util.Types;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.ArrayList;

import static es.geoplanosocial.util.Constants.*;

/**
 * Abstract generic level
 * Created by gbermejo on 15/05/17.
 */
public abstract class Level {

    public enum Type {
        A(0),
        B(1),
        C(2);
        private final int number;

        Type(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }
    }


    private final String id;
    private final String title;
    private final int mainColor;


    protected final PGraphics pg;
    protected final long startTime;


    private boolean doFrameClear =true;
    private boolean doDrawPlayers=true;
    private boolean drawPlayersFront=false;


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
    protected static LevelCallback levelCallback;


    public static void init(ArrayList<Player> players,PApplet processing, LevelCallback levelCallback){
        Level.players=players;
        Level.processing=processing;
        Level.levelCallback=levelCallback;
    }

    protected void nextLevel(){
        levelCallback.nextLevel();
    }

    protected boolean isCompleted(){
        return levelCallback.getCurrentLevelCompletion();
    }


    public void draw(){
        if(doFrameClear) {
            //Set plain background
            pg.beginDraw();
            //pg.background(mainColor);
            pg.clear();
            pg.endDraw();
        }

        if(doDrawPlayers && !drawPlayersFront) {
            drawPlayers();
        }

        drawLevel();

        if(doDrawPlayers && drawPlayersFront) {
            drawPlayers();
        }

        //processing.image(pg, START_WORLD_X, START_WORLD_Y);
    }


    public PGraphics getGraphics() {
        return pg;
    }

    private void drawPlayers(){
        for(Player p : players){
            if(p.isVisible() && p instanceof VisiblePlayer)((VisiblePlayer) p).draw(pg);
        }
    }

    private void setup(){
        refreshPlayers(setupPlayers());
        setupLevel();
    }

    public abstract void update();

    protected abstract void drawLevel();

    protected abstract void setupLevel();
    protected abstract ArrayList<Player> setupPlayers();


    public void setDoFrameClear(boolean doFrameClear) {
        this.doFrameClear = doFrameClear;
    }

    public void setDoDrawPlayers(boolean doDrawPlayers) {
        this.doDrawPlayers = doDrawPlayers;
    }

    public void setDrawPlayersFront(boolean drawPlayersFront) {
        this.drawPlayersFront = drawPlayersFront;
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


    public void addPlayers(ArrayList<Player> newPlayers){
        //Default implementation
        for (Player p : newPlayers){
            Level.players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.WHITE_ALPHA, p));
        }
    }


    protected static void refreshPlayers(ArrayList<Player> players) {
        Level.players.clear();
        Level.players.addAll(players);
    }




    /**
     * Factory for creating levels.
     * Created by gbermejo on 20/05/17.
     */
    public static class Factory {

        private static String getLevelClassName(int players, Level.Type level){

            String levelName = level.name();

            return String.format(LEVEL_CLASS_FULLY_QUALIFIED_FORMAT,players,levelName.toLowerCase(),players,levelName);
        }

        public static Class getLevelClass(int players, Level.Type level){

            Class levelClass;

            try {
                levelClass = Class.forName(getLevelClassName(players, level));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                levelClass = DEFAULT_LEVEL_CLASS;
            }

            return levelClass;
        }

        public static Level getLevel(int players, Level.Type level){

            Class levelClass= getLevelClass(players,level);
            Level l=null;

            try {
                l=(Level)levelClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }

            return l;
        }
    }
}
