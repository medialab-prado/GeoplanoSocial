package es.geoplanosocial.levels.world2.c;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Color;

import java.util.ArrayList;

/**
 * World 2
 * Level C
 * Created by gbermejo on 27/05/17.
 */
public class Level2C extends Level {

    private static final String TITLE="P2P";
    private static final int MAIN_COLOR= Color.W2_B_BG;

    public Level2C() {
        super(TITLE, MAIN_COLOR);
    }


    @Override
    protected void setupLevel() {
        //TODO Level specific setup
    }

    @Override
    protected ArrayList<Player> setupPlayers() {

        ArrayList<Player> players=new ArrayList<>();

        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W2_BLACK_NODE, Level.players.get(0)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W2_WHITE_NODE, Level.players.get(1)));

        return players;
    }



    @Override
    public void update() {
        //TODO Update level elements

    }


    @Override
    protected void drawLevel() {
        //TODO Draw level elements
    }


}
