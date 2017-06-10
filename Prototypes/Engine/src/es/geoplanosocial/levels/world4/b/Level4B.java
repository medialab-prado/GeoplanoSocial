package es.geoplanosocial.levels.world4.b;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Color;
import es.geoplanosocial.util.Utils;

import java.awt.*;
import java.util.ArrayList;

/**
 * World 4
 * Level B
 * Created by gbermejo on 27/05/17.
 */
public class Level4B extends Level {

    private static final String TITLE="Acorralado";
    private static final int MAIN_COLOR= Color.RED_A400;

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

        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.CYAN, Level.players.get(0)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.MAGENTA, Level.players.get(1)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.YELLOW, Level.players.get(2)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.BLACK, Level.players.get(3)));

        return players;
    }



    @Override
    public void update() {

        surroundedPlayer = null;
        //Check if any player is surrounded
        for(Player target: players){
            if(target.isVisible()){
                if(isSurrounded(target)){
                    surroundedPlayer = target;
                    break;
                }
            }
        }
    }

    private boolean isSurrounded(Player target) {
        Polygon enclosure = new Polygon();
        for(Player p: players){
            if(p.isVisible() && p!=target){
                enclosure.addPoint(p.getBoundingBox().x, p.getBoundingBox().y);
            }
        }
        return enclosure.contains(target.getBoundingBox());
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
                if (p.isVisible() && p != surroundedPlayer) {
                    pg.vertex(p.getBoundingBox().x, p.getBoundingBox().y);
                }
            }
            pg.endShape(pg.CLOSE);
            pg.endDraw();
        }
    }


}
