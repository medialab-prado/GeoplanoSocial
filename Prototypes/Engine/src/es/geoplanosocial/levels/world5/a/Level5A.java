package es.geoplanosocial.levels.world5.a;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Color;

import java.util.ArrayList;

/**
 * World 4
 * Level A
 * Created by gbermejo on 27/05/17.
 */
public class Level5A extends Level {

    private static final String TITLE="Imita-y-voltea";
    private static final int MAIN_COLOR= Color.RED_A700;

    public Level5A() {
        super(TITLE, MAIN_COLOR);
    }


    @Override
    protected void setupLevel() {
        //TODO Level specific setup
    }

    @Override
    protected ArrayList<Player> setupPlayers() {

        ArrayList<Player> players=new ArrayList<>();

        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.CYAN, Level.players.get(0)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.MAGENTA, Level.players.get(1)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.YELLOW, Level.players.get(2)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.BLACK, Level.players.get(3)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.BLACK, Level.players.get(4)));

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
