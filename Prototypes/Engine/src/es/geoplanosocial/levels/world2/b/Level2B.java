package es.geoplanosocial.levels.world2.b;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.ExtendedNode;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Color;
import es.geoplanosocial.util.Utils;

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


    @Override
    protected void setupLevel() {
        //TODO Level specific setup
    }

    @Override
    protected ArrayList<Player> setupPlayers() {

        ArrayList<Player> players=new ArrayList<>();

        Utils.log( Level.players.get(0).getBoundingBox().width+", "+Level.players.get(1).getBoundingBox().width);

        //Charges and gives
        ExtendedNode node1= (ExtendedNode)Player.Factory.getPlayer(Player.Type.EXTENDED_NODE, Color.DARK_GREY, Level.players.get(0));
        node1.setFullness(0);
        players.add(node1);

        //Receives
        ExtendedNode node2= (ExtendedNode)Player.Factory.getPlayer(Player.Type.EXTENDED_NODE, Color.W2_WHITE_NODE, Level.players.get(1));
        node2.setFullness(0);
        players.add(node2);

        return players;
    }



    @Override
    public void update() {
        Player p1= Level.players.get(0);
        Player p2= Level.players.get(1);

        if(p1 instanceof ExtendedNode && p2 instanceof ExtendedNode){

            ExtendedNode node1 = (ExtendedNode)p1;
            ExtendedNode node2 = (ExtendedNode)p2;

        }

    }


    @Override
    protected void drawLevel() {
        //TODO Draw level elements
    }


}
