package es.geoplanosocial.levels.world3.b;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.Node;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.players.VisiblePlayer;
import es.geoplanosocial.util.Color;
import es.geoplanosocial.util.Utils;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

import java.util.ArrayList;

import static es.geoplanosocial.util.Constants.*;
import static processing.core.PConstants.ADD;

/**
 * World 3
 * Level B
 * Created by gbermejo on 27/05/17.
 */
public class Level3B extends Level {

    private static final String TITLE="Light";
    public static final int MAIN_COLOR= Color.W3_B_BG;

    private static PGraphics pgIntersection;

    private static PGraphics pgTemp;


    public Level3B() {
        super(TITLE, MAIN_COLOR);
    }


    @Override
    protected void setupLevel() {
        //setDoFrameClear(false);
        setDoDrawPlayers(false);
        pgIntersection = processing.createGraphics(LEVEL_WIDTH, LEVEL_HEIGHT, SCREEN_RENDERER);
        //pgIntersection.smooth(ANTI_ALIASING_LEVEL);

        pgTemp = processing.createGraphics(LEVEL_WIDTH, LEVEL_HEIGHT, SCREEN_RENDERER);
        //pgTemp.smooth(ANTI_ALIASING_LEVEL);
    }

    @Override
    protected ArrayList<Player> setupPlayers() {

        ArrayList<Player> players=new ArrayList<>();

        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W3_RED_NODE, Level.players.get(0)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W3_GREEN_NODE, Level.players.get(1)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W3_BLUE_NODE, Level.players.get(2)));

        return players;
    }



    @Override
    public void update() {
        //TODO Update level elements

    }


    @Override
    protected void drawLevel() {

        //Clear pg
        pgIntersection.beginDraw();
        pgIntersection.clear();

        //Blend players colors
        for(Player p : players){

            if(p.isPlaying()){//Actual players

                //Clear temp
                pgTemp.beginDraw();
                pgTemp.clear();
                pgTemp.endDraw();

                //Clear draw in temp
                ((Node) p).draw(pgTemp);
                PImage n = pgTemp.get();

                //Blend temp and pg
                pgIntersection.blend(n, 0, 0, n.width, n.height, 0, 0, pgIntersection.width, pgIntersection.height, ADD);
            }else if(p.isVisible() && p instanceof VisiblePlayer){//Ghosts
                ((VisiblePlayer) p).draw(pg);
            }
        }

        pgIntersection.endDraw();

        pg.beginDraw();
        pg.image(pgIntersection, 0, 0);
        pg.endDraw();
    }


}
