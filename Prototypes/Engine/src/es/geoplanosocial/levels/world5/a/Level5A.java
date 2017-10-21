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

    private long timerColor = System.currentTimeMillis();
    private long timerNext = System.currentTimeMillis();
    private final int MAX_INTERVAL_COLOR = 1500;//In milliseconds
    private final int MAX_INTERVAL_NEXT = 3000;//In milliseconds
    private int color;

    public Level5A() {
        super(TITLE, MAIN_COLOR);
    }


    @Override
    protected void setupLevel() {
        poli = new RandomShape(5);
        setDrawPlayersFront(true);
        this.color = Color.WHITE;
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
            if (System.currentTimeMillis() - timerColor >= MAX_INTERVAL_COLOR) {
                this.color = Color.BLACK;
                // timerColor = System.currentTimeMillis();
                if (System.currentTimeMillis() - timerNext >= MAX_INTERVAL_NEXT) {
                    this.color = Color.WHITE;
                    poli.updateRamdomShape();
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
        for (int i = 0; i < poli.getVertexNumber(); i++) {
            pg.vertex(poli.getVertex(i).x, poli.getVertex(i).y);
        }
        pg.endShape();


        // todo just for debugging (for drawing corners)
        // pg.fill(Color.LIGHT_GREY);
        // for (int i = 0; i < poli.getVertexNumber(); i++) {
        //     pg.ellipse(poli.getVertex(i).x, poli.getVertex(i).y, 5, 5);
        // }

        pg.endDraw();
    }
}
