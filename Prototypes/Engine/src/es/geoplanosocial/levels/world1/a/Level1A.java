package es.geoplanosocial.levels.world1.a;

import es.geoplanosocial.factories.PlayerFactory;
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
public class Level1A extends Level {

    private static final String TITLE="Huella";
    private static final int MAIN_COLOR= Color.RED;

    public Level1A() {
        super(Level1A.TITLE, Level1A.MAIN_COLOR);
    }

    @Override
    protected void setup() {
        //Init specific players
        ArrayList<Player> players=new ArrayList<>();

        for (Player p :Level.players){
            players.add(PlayerFactory.getPlayer(Types.Player.NODE, Color.GREY, p));
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
