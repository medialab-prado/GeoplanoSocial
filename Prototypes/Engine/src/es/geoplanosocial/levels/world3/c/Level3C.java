package es.geoplanosocial.levels.world3.c;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Color;

import java.util.ArrayList;

/**
 * World 3
 * Level C
 * Created by gbermejo on 27/05/17.
 */
public class Level3C extends Level {

    private static final String TITLE="Iron triangle";
    private static final int MAIN_COLOR= Color.LIGHTBLUE_A200;

    public Level3C() {
        super(TITLE, MAIN_COLOR);
    }


    @Override
    protected void setupLevel() {
        //TODO Level specific setup
    }

    @Override
    protected ArrayList<Player> setupPlayers() {

        ArrayList<Player> players=new ArrayList<>();

        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.RED, Level.players.get(0)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.GREEN, Level.players.get(1)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.BLUE, Level.players.get(2)));

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
