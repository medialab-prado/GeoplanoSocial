package es.geoplanosocial.levels.world3.a;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.players.VisiblePlayer;
import es.geoplanosocial.util.Color;
import es.geoplanosocial.util.RandomShape;
import java.util.ArrayList;

/**
 * World 3
 * Level A
 * Created by josué on 08/06/17.
 */
public class Level3A extends Level {

    private static final String TITLE="Mimic Libre";
    public static final int MAIN_COLOR= Color.W3_A_BG;

    private RandomShape triangle;

    private long timerColor = System.currentTimeMillis();
    private long timerNext = System.currentTimeMillis();
    private final int MAX_INTERVAL_COLOR = 1500;//In milliseconds
    private final int MAX_INTERVAL_NEXT = 3000;//In milliseconds
    private int color;

    public Level3A() {
        super(TITLE, MAIN_COLOR);
    }

    @Override
    protected void setupLevel() {
        triangle = new RandomShape(3);
        setDrawPlayersFront(true);
        this.color = Color.WHITE;
    }

    @Override
    protected ArrayList<Player> setupPlayers() {

        ArrayList<Player> players=new ArrayList<>();

        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W3_RED_NODE, Level.players.get(0)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W3_GREEN_NODE, Level.players.get(1)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W3_BLUE_NODE, Level.players.get(2)));

        return players;
    }

    @Override
    public void update() {
        if (triangle.playersAnyMatch(players)) {
            if (System.currentTimeMillis() - timerColor >= MAX_INTERVAL_COLOR) {
                this.color = Color.BLACK;
                // timerColor = System.currentTimeMillis();
                if (System.currentTimeMillis() - timerNext >= MAX_INTERVAL_NEXT) {
                    this.color = Color.WHITE;
                    triangle.updateRamdomShape();
                    timerNext = System.currentTimeMillis();
                    timerColor = System.currentTimeMillis();
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
        for (int i = 0; i < triangle.getVertexNumber(); i++) {
            pg.vertex(triangle.getVertex(i).x, triangle.getVertex(i).y);
        }
        pg.endShape();

        // todo just for debugging (for drawing centroid)
        // pg.fill(Color.LIGHT_GREY);
        // pg.ellipse(triangle.centroid.x, triangle.centroid.y, 10, 10);

        pg.endDraw();
    }
}
