package es.geoplanosocial.levels.world1.a;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.ExtendedNode;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Color;
import es.geoplanosocial.util.Types;
import es.geoplanosocial.util.Utils;
import processing.core.PApplet;

import java.util.ArrayList;

/**
 * World 1
 * Level A
 * Created by gbermejo on 15/05/17.
 */
public class Level1A extends Level {

    private static final String TITLE="Mimetismo";
    public static final int MAIN_COLOR= Color.W1_A_BG;


    private float prevX, prevY;
    public Level1A() {
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
            //players.add(Player.Factory.getPlayer(Player.Type.NODE1A, Color.W1_BLACK_NODE, p));

            ExtendedNode pl =(ExtendedNode)Player.Factory.getPlayer(Player.Type.EXTENDED_NODE, Color.W1_BLACK_NODE, p);
            pl.setAlpha(0.0f);
            players.add(pl);
        }
        refreshPreviousPosition();
        return players;
    }

    private void refreshPreviousPosition() {
        Player player = players.get(0);
        if(player!=null) {
            prevX = player.getBoundingBox().x;
            prevY = player.getBoundingBox().y;
        }
    }


    @Override
    public void update() {
        //Update level elements
        Player player = players.get(0);

        if(player!=null) {
            ExtendedNode p = ((ExtendedNode) players.get(0));

            float d = PApplet.dist(prevX, prevY, p.getBoundingBox().x,p.getBoundingBox().y);
            d=d>=1.0?1.0f:-0.333f;
            p.setAlpha(p.getAlpha() + d*0.01f);

            if(p.getAlpha()>=1.0f)System.out.println("Completed!");
        }

        refreshPreviousPosition();
    }


    @Override
    protected void drawLevel() {
        //Draw level elements
    }

    @Override
    public void addPlayers(ArrayList<Player> newPlayers) {
        for (Player p : newPlayers){
            Level.players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.BLACK_ALPHA, p));
        }
    }

}
