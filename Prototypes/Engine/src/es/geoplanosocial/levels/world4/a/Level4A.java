package es.geoplanosocial.levels.world4.a;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.Node;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.players.VisiblePlayer;
import es.geoplanosocial.util.Color;
import es.geoplanosocial.util.RandomShape;
import processing.core.PGraphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * World 4
 * Level A
 * Created by josué on 09/06/2017.
 */
public class Level4A extends Level {

    private static final String TITLE="Imita";
    public static final int MAIN_COLOR= Color.W4_A_BG;

    private RandomShape square;

    private long timer = System.currentTimeMillis();
    private final int MAX_INTERVAL = 3000;//In milliseconds

    public Level4A() {
        super(TITLE, MAIN_COLOR);
    }

    @Override
    protected void setupLevel() {
        square = new RandomShape(4);
        this.setDoDrawPlayers(false);
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
            // TODO en lugar de "i" sería un nº aleatorio de 0 a vertexNumber (sin repeticiones)
            pg.fill(((Node)players.get(i)).getColor());
            pg.vertex(square.getVertex(i).x, square.getVertex(i).y);
        }
        pg.endShape();
        for (Player p : players) {
            if(p.isVisible() && p instanceof VisiblePlayer)((VisiblePlayer) p).draw(pg);
        }
        pg.endDraw();
    }

}
