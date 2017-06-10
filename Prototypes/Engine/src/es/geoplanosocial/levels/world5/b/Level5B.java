package es.geoplanosocial.levels.world5.b;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Color;
import es.geoplanosocial.util.Utils;

import java.util.ArrayList;

/**
 * World 4
 * Level B
 * Created by gbermejo on 27/05/17.
 */
public class Level5B extends Level {

    private static final String TITLE="Rodea-la-minoría";
    private static final int MAIN_COLOR= Color.W5_B_BG;

    public Level5B() {
        super(TITLE, MAIN_COLOR);
    }


    @Override
    protected void setupLevel() {
        //TODO Level specific setup
    }

    @Override
    protected ArrayList<Player> setupPlayers() {

        ArrayList<Player> players=new ArrayList<>();

        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W5_PINK_NODE, Level.players.get(0)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W5_BLUE_NODE, Level.players.get(1)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W5_YELLOW_NODE, Level.players.get(2)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W5_RED_NODE, Level.players.get(3)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W5_GREEN_NODE, Level.players.get(4)));


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
