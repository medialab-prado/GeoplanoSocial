package es.geoplanosocial.levels.world3.b;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.Node;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.players.VisiblePlayer;
import es.geoplanosocial.util.Color;
import es.geoplanosocial.util.Utils;
import processing.core.PGraphics;
import processing.core.PImage;

import java.awt.*;
import java.util.ArrayList;

import static es.geoplanosocial.util.Constants.*;
import static es.geoplanosocial.util.Configuration.*;
import static processing.core.PConstants.ADD;

/**
 * World 3
 * Level B
 * Created by gbermejo on 27/05/17.
 */
public class Level3B extends Level {

    private static final String TITLE="Venn a mi";
    public static final int MAIN_COLOR= Color.W3_B_BG;

    private  PGraphics pgIntersection;
    private  PGraphics pgTemp;

    private final float SIZE_FACTOR = 2.2f;

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
        Player p = Player.Factory.getPlayer(Player.Type.NODE, Color.W3_RED_NODE, Level.players.get(0));
        p.getBoundingBox().setSize((int)(playerSize *SIZE_FACTOR),(int)(playerSize *SIZE_FACTOR));
        players.add(p);

        p = Player.Factory.getPlayer(Player.Type.NODE, Color.W3_GREEN_NODE, Level.players.get(1));
        p.getBoundingBox().setSize((int)(playerSize *SIZE_FACTOR),(int)(playerSize *SIZE_FACTOR));
        players.add(p);

        p = Player.Factory.getPlayer(Player.Type.NODE, Color.W3_BLUE_NODE, Level.players.get(2));
        p.getBoundingBox().setSize((int)(playerSize *SIZE_FACTOR),(int)(playerSize *SIZE_FACTOR));
        players.add(p);

        return players;
    }



    @Override
    public void update() {
        //TODO Update level elements
        Node node1 =((Node)players.get(0));
        Node node2 =((Node)players.get(1));
        Node node3 =((Node)players.get(2));

        Point point1 = new Point(node1.getBoundingBox().x, node1.getBoundingBox().y);
        Point point2 = new Point(node2.getBoundingBox().x, node2.getBoundingBox().y);
        Point point3 = new Point(node3.getBoundingBox().x, node3.getBoundingBox().y);

        boolean c1 = Utils.isCircleCollision(point1,node1.getBoundingBox().width/2.0f, point2, node2.getBoundingBox().width/2.0f);
        boolean c2 = Utils.isCircleCollision(point1,node1.getBoundingBox().width/2.0f, point3, node3.getBoundingBox().width/2.0f);
        boolean c3 = Utils.isCircleCollision(point3,node3.getBoundingBox().width/2.0f, point2, node2.getBoundingBox().width/2.0f);

        if(c1&&c2&&c3&& !isCompleted()){
            nextLevel();
        }
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
