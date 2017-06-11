package es.geoplanosocial.levels.world4.b;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Color;
import es.geoplanosocial.util.Utils;

import javax.rmi.CORBA.Util;
import java.awt.*;
import java.util.ArrayList;

/**
 * World 4
 * Level B
 * Created by gbermejo on 27/05/17.
 */
public class Level4B extends Level {

    private static final String TITLE="Acorralado";
    public static final int MAIN_COLOR= Color.W4_B_BG;

    private Player surroundedPlayer = null;

    public Level4B() {
        super(TITLE, MAIN_COLOR);
    }


    @Override
    protected void setupLevel() {
        //TODO Level specific setup
    }

    @Override
    protected ArrayList<Player> setupPlayers() {

        ArrayList<Player> players=new ArrayList<>();

        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W4_BLACK_NODE, Level.players.get(0)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W4_YELLOW_NODE, Level.players.get(1)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W4_RED_NODE, Level.players.get(2)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W4_BLUE_NODE, Level.players.get(3)));

        return players;
    }



    @Override
    public void update() {

        surroundedPlayer = null;
        //Check if any player is surrounded
        for(Player target: players){
            if(target.isPlaying() && target.isSurrounded(players)){
                surroundedPlayer = target;
                break;
            }
        }
    }



    @Override
    protected void drawLevel() {
        //Utils.log("Surrounded: "+surroundedPlayer);

        if(surroundedPlayer!=null) {
            pg.beginDraw();
            pg.noStroke();
            pg.fill(Color.WHITE_ALPHA);
            pg.beginShape();

            for (Player p : players) {
                if (p.isPlaying() && p != surroundedPlayer) {
                    pg.vertex(p.getBoundingBox().x, p.getBoundingBox().y);
                }
            }
            pg.endShape(pg.CLOSE);
            pg.endDraw();
        }
    }


}
