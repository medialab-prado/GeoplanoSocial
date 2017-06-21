package es.geoplanosocial.levels.world5.a;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.Node;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.players.VisiblePlayer;
import es.geoplanosocial.util.Color;
import es.geoplanosocial.util.RandomShape;

import java.awt.*;
import java.util.ArrayList;

/**
 * World 4
 * Level A
 * Created by gbermejo on 27/05/17.
 */
public class Level5A extends Level {

    private static final String TITLE="Mimic (D)espejado";
    public static final int MAIN_COLOR= Color.W5_A_BG;

    private RandomShape poli;

    private long timer = System.currentTimeMillis();
    private final int MAX_INTERVAL = 3000;//In milliseconds

    public Level5A() {
        super(TITLE, MAIN_COLOR);
    }


    @Override
    protected void setupLevel() {
        poli = new RandomShape(5);
        setDrawPlayersFront(true);
    }

    @Override
    protected ArrayList<Player> setupPlayers() {

        ArrayList<Player> players=new ArrayList<>();

        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W5_PINK_NODE, Level.players.get(0)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W5_BLUE_NODE, Level.players.get(1)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W5_YELLOW_NODE, Level.players.get(2)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W5_RED_NODE, Level.players.get(3)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W5_GREEN_NODE, Level.players.get(4)));

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
        pg.beginShape();
        for (int i = 0; i < poli.getVertexNumber(); i++) {
            pg.fill(((Node)players.get(i)).getColor());
            pg.vertex(poli.getVertex(i).x, poli.getVertex(i).y);
            Point[] p = calculateTriangleColorIndicator(new Point(poli.getVertex(i).x, poli.getVertex(i).y));
            pg.vertex(p[0].x, p[0].y);
            pg.vertex(p[1].x, p[1].y);
        }
        pg.endShape();

        // todo just for debugging (for drawing centroid)
        // pg.fill(Color.LIGHT_GREY);
        // pg.ellipse(poli.centroid.x, poli.centroid.y, 10, 10);

        pg.endDraw();
    }

    private Point[] calculateTriangleColorIndicator(Point point) {
        Point[] p = new Point[2];
        p[0] = new Point(0,0);
        p[1] = new Point(0,0);
        return p;
    }

}
