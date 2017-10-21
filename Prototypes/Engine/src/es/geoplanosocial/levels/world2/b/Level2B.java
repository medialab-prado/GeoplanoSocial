package es.geoplanosocial.levels.world2.b;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.ExtendedNode;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Color;
import es.geoplanosocial.util.Utils;
import processing.core.PApplet;

import java.awt.*;
import java.util.ArrayList;

/**
 * World 2
 * Level B
 * Created by gbermejo on 27/05/17.
 */
public class Level2B extends Level {

    private static final String TITLE="Sinergia";
    public static final int MAIN_COLOR= Color.W2_B_BG;

    public Level2B() {
        super(TITLE, MAIN_COLOR);
    }

    private float prevX, prevY;


    @Override
    protected void setupLevel() {
        //TODO Level specific setup
        refreshPreviousPosition();
    }

    @Override
    protected ArrayList<Player> setupPlayers() {

        ArrayList<Player> players=new ArrayList<>();

        Utils.log( Level.players.get(0).getBoundingBox().width+", "+Level.players.get(1).getBoundingBox().width);

        //Charges and gives
        ExtendedNode node1= (ExtendedNode)Player.Factory.getPlayer(Player.Type.EXTENDED_NODE, Color.W2_WHITE_NODE, Level.players.get(0));
        node1.setFullness(0);
        players.add(node1);

        //Receives
        ExtendedNode node2= (ExtendedNode)Player.Factory.getPlayer(Player.Type.EXTENDED_NODE, Color.W2_WHITE_NODE, Level.players.get(1));
        node2.setFullness(0);
        Rectangle bb = node2.getBoundingBox();
        bb.width*=2;
        bb.height*=2;
        node2.setBoundingBox(bb);
        players.add(node2);


        return players;
    }


    private void refreshPreviousPosition() {
        Player player = players.get(0);
        if(player!=null) {
            prevX = player.getBoundingBox().x;
            prevY = player.getBoundingBox().y;
        }
    }

    @Override
    public void update() {
        Player p1= Level.players.get(0);
        Player p2= Level.players.get(1);

        if(p1 instanceof ExtendedNode && p2 instanceof ExtendedNode){

            ExtendedNode node1 = (ExtendedNode)p1;
            ExtendedNode node2 = (ExtendedNode)p2;

            float d = PApplet.dist(prevX, prevY, node1.getBoundingBox().x,node1.getBoundingBox().y);
            d=d>=1.0?1.0f:-0.333f;
            node1.setFullness(node1.getFullness() + d*0.01f);

            float fullness = node1.getFullness();

            Point point1 = new Point(node1.getBoundingBox().x, node1.getBoundingBox().y);
            Point point2 = new Point(node2.getBoundingBox().x, node2.getBoundingBox().y);

            if(fullness>0 && Utils.isCircleCollision(point1, node1.getBoundingBox().width/2.0f,point2,node2.getBoundingBox().width/2.0f)){
                fullness-=0.01f;
                node1.setFullness(fullness);
                node2.setFullness(node2.getFullness()+(0.01f/1.9f));
            }

            if(node1.getFullness()>=1.0f && node2.getFullness()>=1.0f && !isCompleted()){
                nextLevel();
            }

        }

        refreshPreviousPosition();
    }


    @Override
    protected void drawLevel() {
        //TODO Draw level elements
    }


}
