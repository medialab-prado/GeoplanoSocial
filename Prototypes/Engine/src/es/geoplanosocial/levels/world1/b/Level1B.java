package es.geoplanosocial.levels.world1.b;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Color;
import es.geoplanosocial.util.Types;

import java.util.ArrayList;

/**
 * World 1
 * Level A
 * Created by gbermejo on 15/05/17.
 */
public class Level1B extends Level {

    private static final String TITLE="Rastro";
    private static final int MAIN_COLOR= Color.WHITE;

    public Level1B() {
        super(TITLE, MAIN_COLOR);
    }

    @Override
    protected void setupLevel() {
        //Level specific setup
    }

    @Override
    protected ArrayList<Player> setupPlayers() {

        //Init specific players
        ArrayList<Player> players=new ArrayList<>();

        for (Player p :Level.players){
            players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.BLACK, p));
        }
        return players;
    }



    @Override
    public void update() {
        //Update level elements

    }


    @Override
    protected void drawLevel() {
        //Draw level elements
    }

    @Override
    public void addPlayers(ArrayList<Player> newPlayers) {
        for (Player p : newPlayers){
            Level.players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.RED_ALPHA, p));
        }
    }
}
