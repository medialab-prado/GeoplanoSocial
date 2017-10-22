package es.geoplanosocial.levels.world2.a;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Color;
import es.geoplanosocial.util.Utils;

import java.awt.*;
import java.util.ArrayList;

/**
 * World 2
 * Level A
 * Created by josué on 19/06/2017.
 */
public class Level2A extends Level {

    private static final String TITLE="Los amantes de Teruel";
    public static final int MAIN_COLOR= Color.W2_A_BG;

    public Level2A() {
        super(TITLE, MAIN_COLOR);
    }


    @Override
    protected void setupLevel() {
        //TODO Level specific setup
    }

    @Override
    protected ArrayList<Player> setupPlayers() {

        ArrayList<Player> players=new ArrayList<>();

        players.add(Player.Factory.getPlayer(Player.Type.NODE2A, Color.W2_BLACK_NODE, Level.players.get(0)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE2A, Color.W2_WHITE_NODE, Level.players.get(1)));
        
        return players;
    }



    @Override
    public void update() {
        Node2A node1 =((Node2A)players.get(0));
        node1.targetPos(players.get(1).getLocation());
        Node2A node2=((Node2A)players.get(1));
        node2.targetPos(players.get(0).getLocation());


        Point point1 = new Point(node1.getBoundingBox().x, node1.getBoundingBox().y);
        Point point2 = new Point(node2.getBoundingBox().x, node2.getBoundingBox().y);

        if(Utils.isCircleCollision(point1,node1.getBoundingBox().width/2.0f, point2, node2.getBoundingBox().width/2.0f)&& !isCompleted()){
            nextLevel();
        }
    }


    @Override
    protected void drawLevel() {
        //TODO Draw level elements
    }


}
