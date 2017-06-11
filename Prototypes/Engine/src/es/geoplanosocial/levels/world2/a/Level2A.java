package es.geoplanosocial.levels.world2.a;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Color;

import java.util.ArrayList;

/**
 * World 2
 * Level A
 * Created by gbermejo on 27/05/17.
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

        for (Player p :Level.players){
            players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W2_BLACK_NODE, p));
        }

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
