package es.geoplanosocial.levels;

import es.geoplanosocial.players.Player;
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
                if(p.isVisible())p.draw(pg);
            }
        }
        drawLevel();
        processing.image(pg, START_WORLD_X, START_WORLD_Y);
    }

    private void setup(){
        setupLevel();
        refreshPlayers(setupPlayers());
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
            Level.players.add(Player.Factory.getPlayer(Types.Player.NODE, Color.WHITE_ALPHA, p));
        }
    }


    private static void refreshPlayers(ArrayList<Player> players) {
        Level.players.clear();
        Level.players.addAll(players);
    }




    /**
     * Factory for creating levels.
     * Created by gbermejo on 20/05/17.
     */
    public static class Factory {

        private static String getLevelClassName(int players, Types.Level level){

            String levelName = level.name();

            return String.format(LEVEL_CLASS_FULLY_QUALIFIED_FORMAT,players,levelName.toLowerCase(),players,levelName);
        }

        private static Class getLevelClass(int players, Types.Level level){

            Class levelClass;

            try {
                levelClass = Class.forName(getLevelClassName(players, level));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                levelClass = DEFAULT_LEVEL_CLASS;
            }

            return levelClass;
        }

        public static Level getLevel(int players, Types.Level level){

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
