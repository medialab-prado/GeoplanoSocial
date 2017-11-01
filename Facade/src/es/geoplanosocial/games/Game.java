package es.geoplanosocial.games;

import es.geoplanosocial.players.Player;
import es.geoplanosocial.players.VisiblePlayer;
import es.geoplanosocial.util.Color;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.ArrayList;

import static es.geoplanosocial.util.Constants.*;

/**
 * Abstract generic level
 * Created by gbermejo on 15/05/17.
 */
public abstract class Game {


    private final String id;
    private final String title;


    protected final PGraphics pg;
    protected final long startTime;


    private boolean doFrameClear =true;
    private boolean doDrawPlayers=true;
    private boolean drawPlayersFront=false;


    protected Game(String title) {
        this.id = getClass().getSimpleName();
        this.title = title;
        this.pg = processing.createGraphics(LEVEL_WIDTH, LEVEL_HEIGHT, SCREEN_RENDERER);
        this.startTime=System.currentTimeMillis();

        setupGame();
    }

    protected static ArrayList<Player> players;
    protected static PApplet processing;
    protected static GameCallback gameCallback;


    public static void init(ArrayList<Player> players,PApplet processing, GameCallback gameCallback){
        Game.players=players;
        Game.processing=processing;
        Game.gameCallback = gameCallback;
    }

    protected void nextLevel(){
        gameCallback.onCompleted();
    }

    protected int getCurrentLevel(){
        return gameCallback.getCurrentLevel();
    }


    public void drawGame(){
        if(doFrameClear) {
            //Set plain background
            pg.beginDraw();
            pg.clear();
            pg.endDraw();
        }

        if(doDrawPlayers && !drawPlayersFront) {
            drawPlayers();
        }

        draw();

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

    private void setupGame(){
        refreshPlayers(setupPlayers());
        setup();
    }

    public abstract void update();

    protected abstract void draw();

    protected abstract void setup();
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


    public void addPlayers(ArrayList<Player> newPlayers){
        //Default implementation
        for (Player p : newPlayers){
            Game.players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.WHITE_ALPHA, p));
        }
    }


    protected static void refreshPlayers(ArrayList<Player> players) {
        Game.players.clear();
        Game.players.addAll(players);
    }




    /**
     * Factory for creating games.
     * Created by gbermejo on 20/05/17.
     */
    public static class Factory {

        private static String getGameClassName(int game){

            return String.format(GAME_CLASS_FULLY_QUALIFIED_FORMAT,game);
        }

        public static Class getGameClass(int game){

            Class gameClass;

            try {
                gameClass = Class.forName(getGameClassName(game));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                gameClass = DEFAULT_GAME_CLASS;
            }

            return gameClass;
        }

        public static Game getGame(int game){

            Class gameClass= getGameClass(game);
            Game l=null;

            try {
                l=(Game)gameClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }

            return l;
        }
    }
}
