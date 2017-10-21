package es.geoplanosocial.levels.world4.a;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.ExtendedNode;
import es.geoplanosocial.players.Node;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.players.VisiblePlayer;
import es.geoplanosocial.util.Color;
import es.geoplanosocial.util.RandomShape;
import processing.core.PGraphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static java.lang.Math.round;
import static processing.core.PApplet.sqrt;

/**
 * World 4
 * Level A
 * Created by josué on 09/06/2017.
 */
public class Level4A extends Level {

    private static final String TITLE="Mimic fijo";
    public static final int MAIN_COLOR= Color.W4_A_BG;
    private static final float MINI_TRIANGLE_DIST = (float) 10;

    private RandomShape square;

    private long timerColor = System.currentTimeMillis();
    private long timerNext = System.currentTimeMillis();
    private final int MAX_INTERVAL_COLOR = 1500;//In milliseconds
    private final int MAX_INTERVAL_NEXT = 3000;//In milliseconds
    private int color;


    public Level4A() {
        super(TITLE, MAIN_COLOR);
    }

    @Override
    protected void setupLevel() {
        square = new RandomShape(4);
        setDrawPlayersFront(true);
        this.color = Color.WHITE;

    }

    @Override
    protected ArrayList<Player> setupPlayers() {

        ArrayList<Player> players=new ArrayList<>();

        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W4_BLACK_NODE, Level.players.get(0)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W4_BLUE_NODE, Level.players.get(1)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W4_RED_NODE, Level.players.get(2)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W4_YELLOW_NODE, Level.players.get(3)));

        return players;
    }

    @Override
    public void update() {
        if (square.playersColorMatch(players)) {
            if (System.currentTimeMillis() - timerColor >= MAX_INTERVAL_COLOR) {
                this.color = Color.BLACK;
                // timerColor = System.currentTimeMillis();
                if (System.currentTimeMillis() - timerNext >= MAX_INTERVAL_NEXT) {
                    this.color = Color.WHITE;
                    square.updateRamdomShape();
                    timerNext = System.currentTimeMillis();
                    timerColor = System.currentTimeMillis();

                    if(!isCompleted()) nextLevel();

                }
            }
        }
        else {
            this.color = Color.WHITE;
            timerColor = System.currentTimeMillis();
            timerNext = System.currentTimeMillis();
        }
    }

    @Override
    protected void drawLevel() {
        pg.beginDraw();
        pg.noStroke();
        pg.fill(color);
        pg.beginShape();
        for (int i = 0; i < square.getVertexNumber(); i++) {
            // TODO en lugar de "i" sería un nº aleatorio de 0 a vertexNumber (sin repeticiones)
            // pg.fill(((Node)players.get(i)).getColor());
            pg.vertex(square.getVertex(i).x, square.getVertex(i).y);
        }
        pg.endShape();

        drawVertexMiniTriangles();

        // todo just for debugging (for drawing centroid)
        // pg.fill(Color.LIGHT_GREY);
        // pg.ellipse(square.centroid.x, square.centroid.y, 10, 10);

        pg.endDraw();
    }

    private void drawVertexMiniTriangles() {
        for (int i = 0; i < square.getVertexNumber(); i++) {
            ArrayList<Point> trianglePoints = new ArrayList<>();
            trianglePoints.add(new Point(square.getVertex(i).x, square.getVertex(i).y));
            trianglePoints.add(pointInLineAtDistance(trianglePoints.get(0), square.getVertex((i == square.getVertexNumber() - 1) ? 0 : i+1), MINI_TRIANGLE_DIST));
            trianglePoints.add(pointInLineAtDistance(trianglePoints.get(0), square.getVertex((i == 0) ? square.getVertexNumber() -1 : i-1), MINI_TRIANGLE_DIST));

            pg.beginShape();
            pg.fill(((Node)players.get(square.getUserVertexAssignment()[i])).getColor());
            for (int j = 0; j < 3; j++) {
                pg.vertex(trianglePoints.get(j).x, trianglePoints.get(j).y);
            }
            pg.endShape();
        }
    }

    // adapted from https://math.stackexchange.com/questions/175896/finding-a-point-along-a-line-a-certain-distance-away-from-another-point
    private Point pointInLineAtDistance(Point p1, Point p2, float dist) {
        // The distance between Start and End point is given by d
        float d = sqrt((float) (Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2)));
        // Let the ratio of distances, t
        float t = dist / d;

        return new Point(round(((1 - t) * p1.x) + (t * p2.x)), round(((1 - t) * p1.y) + (t * p2.y)));
    }

}
