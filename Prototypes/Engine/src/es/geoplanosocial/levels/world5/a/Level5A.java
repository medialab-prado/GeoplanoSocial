package es.geoplanosocial.levels.world5.a;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Color;
import es.geoplanosocial.util.RandomShape;

import java.util.ArrayList;

/**
 * World 4
 * Level A
 * Created by gbermejo on 27/05/17.
 */
public class Level5A extends Level {

    private static final String TITLE="Imita-y-voltea";
    private static final int MAIN_COLOR= Color.RED_A700;

    private RandomShape poli;

    private long timer = System.currentTimeMillis();
    private final int MAX_INTERVAL = 3000;//In milliseconds

    public Level5A() {
        super(TITLE, MAIN_COLOR);
    }


    @Override
    protected void setupLevel() {
        poli = new RandomShape(5);
    }

    @Override
    protected ArrayList<Player> setupPlayers() {

        ArrayList<Player> players=new ArrayList<>();

        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.RED, Level.players.get(0)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.GREEN, Level.players.get(1)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.BLUE, Level.players.get(2)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.BLACK, Level.players.get(3)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.BLACK, Level.players.get(4)));

        return players;
    }

    @Override
    public void update() {
        if (poli.playersAnyMatch(players)) {
            if (System.currentTimeMillis() - timer >= MAX_INTERVAL) {
                poli.updateRamdomShape();
                timer = System.currentTimeMillis();
            }
        }
        else {
            timer = System.currentTimeMillis();
        }
    }

    @Override
    protected void drawLevel() {
        pg.beginDraw();
        pg.noStroke();
        pg.fill(Color.WHITE);
        pg.beginShape();
        for (int i = 0; i < poli.getVertexNumber(); i++) {
            pg.vertex(poli.getVertex(i).x, poli.getVertex(i).y);
        }
        pg.endShape();
        pg.endDraw();
    }

}
