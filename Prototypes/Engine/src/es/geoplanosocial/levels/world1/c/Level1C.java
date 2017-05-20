package es.geoplanosocial.levels.world1.c;

import es.geoplanosocial.factories.PlayerFactory;
import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.Node;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Color;
import es.geoplanosocial.util.Types;

import java.util.ArrayList;

/**
 * World 1
 * Level C
 * Created by gbermejo on 15/05/17.
 */
public class Level1C extends Level {

    private static final String TITLE="Desaparecer";
    private static final int MAIN_COLOR= Color.BLUE;

    public Level1C() {
        super(Level1C.TITLE, Level1C.MAIN_COLOR);
    }

    @Override
    protected void setup() {
        //Init specific players
        ArrayList<Player> players=new ArrayList<>();

        for (Player p :Level.players){
            players.add(PlayerFactory.getPlayer(Types.Player.NODE, Color.WHITE, p));
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
