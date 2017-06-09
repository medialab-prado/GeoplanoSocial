package es.geoplanosocial.levels.world4.a;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.Node;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Color;
import es.geoplanosocial.util.RandomShape;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * World 4
 * Level A
 * Created by josué on 09/06/2017.
 */
public class Level4A extends Level {

    private static final String TITLE="Acorralado";
    private static final int MAIN_COLOR= Color.RED_A700;

    private RandomShape square;

    private long timer = System.currentTimeMillis();
    private final int MAX_INTERVAL = 3000;//In milliseconds

    public Level4A() {
        super(TITLE, MAIN_COLOR);
    }

    @Override
    protected void setupLevel() {
        square = new RandomShape(4);
    }

    @Override
    protected ArrayList<Player> setupPlayers() {

        ArrayList<Player> players=new ArrayList<>();

        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.RED, Level.players.get(0)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.GREEN, Level.players.get(1)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.BLUE, Level.players.get(2)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.BLACK, Level.players.get(3)));

        return players;
    }

    @Override
    public void update() {
        if (square.playersAnyMatch(players)) {
            if (System.currentTimeMillis() - timer >= MAX_INTERVAL) {
                square.updateRamdomShape();
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
        for (int i = 0; i < square.getVertexNumber(); i++) {
            if (i == 0) pg.fill(Color.GREEN);
            if (i == 1) pg.fill(Color.GREY);
            if (i == 2) pg.fill(Color.YELLOW);
            if (i == 3) pg.fill(Color.BLUE);
            pg.vertex(square.getVertex(i).x, square.getVertex(i).y);
        }
        pg.endShape();
        pg.endDraw();
    }

}
