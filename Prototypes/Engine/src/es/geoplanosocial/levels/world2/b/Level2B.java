package es.geoplanosocial.levels.world2.b;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Color;
import es.geoplanosocial.util.Utils;

import java.util.ArrayList;

/**
 * World 2
 * Level C
 * Created by gbermejo on 27/05/17.
 */
public class Level2B extends Level {

    private static final String TITLE="Sinergia";
    public static final int MAIN_COLOR= Color.W2_B_BG;

    public Level2B() {
        super(TITLE, MAIN_COLOR);
    }


    @Override
    protected void setupLevel() {
        //TODO Level specific setup
    }

    @Override
    protected ArrayList<Player> setupPlayers() {

        ArrayList<Player> players=new ArrayList<>();

        Utils.log( Level.players.get(0).getBoundingBox().width+", "+Level.players.get(1).getBoundingBox().width);


        Node2B node1= (Node2B)Player.Factory.getPlayer(Player.Type.NODE2B, Color.W2_BLACK_NODE, Level.players.get(0));

        node1.setEnergy(node1.getEnergy());
        players.add(node1);

        Node2B node2= (Node2B)Player.Factory.getPlayer(Player.Type.NODE2B, Color.W2_WHITE_NODE, Level.players.get(1));
        node2.setEnergy(node2.getEnergy()*1.5f);
        node2.setGiving(true);
        players.add(node2);


        Utils.log(node1.getEnergy()+", "+node2.getEnergy());
        Utils.log(node1.getBoundingBox().width+", "+node2.getBoundingBox().width);

        return players;
    }



    @Override
    public void update() {
        //TODO Update level elements
        Player p1= Level.players.get(0);
        Player p2= Level.players.get(1);

        if(p1 instanceof Node2B && p2 instanceof Node2B){

            Node2B node1 = (Node2B)p1;
            Node2B node2 = (Node2B)p2;

            node1.checkTransference(node2);

        }

    }


    @Override
    protected void drawLevel() {
        //TODO Draw level elements
    }


}
