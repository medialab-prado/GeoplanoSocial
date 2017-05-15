package es.geoplanosocial.levels.world1.b;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.Node;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Color;

import java.util.ArrayList;

/**
 * World 1
 * Level A
 * Created by gbermejo on 15/05/17.
 */
public class Level1B extends Level {

    private static final String TITLE="Rastro";
    private static final int MAIN_COLOR= Color.GREEN;

    public Level1B() {
        super(Level1B.TITLE, Level1B.MAIN_COLOR);
    }

    @Override
    protected void setup() {
        //Init specific players
        ArrayList<Player> players=new ArrayList<>();

        for (Player p :Level.players){
            players.add(new Node(Color.GREY,p));
        }
        refreshPlayers(players);
    }

    @Override
    public void update() {
        //Update level elements
    }


    @Override
    protected void drawLevel() {
        //Draw level elements
    }
}
