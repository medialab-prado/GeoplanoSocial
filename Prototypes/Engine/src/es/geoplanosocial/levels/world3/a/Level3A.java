package es.geoplanosocial.levels.world3.a;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Color;
import es.geoplanosocial.util.Constants;
import es.geoplanosocial.util.RandomShape;
import es.geoplanosocial.util.Utils;

import java.awt.*;
import java.util.ArrayList;

import static es.geoplanosocial.util.Constants.LEVEL_HEIGHT;
import static es.geoplanosocial.util.Constants.LEVEL_WIDTH;
import static java.lang.Math.round;
import static processing.core.PApplet.dist;

/**
 * World 3
 * Level A
 * Created by gbermejo on 27/05/17.
 */
public class Level3A extends Level {

    private static final String TITLE="Mimic";
    private static final int MAIN_COLOR= Color.LIGHTBLUE_A700;

    private final float DIST_MIN = 300;
    private RandomShape triangle;

    private long timer = System.currentTimeMillis();
    private final int MAX_INTERVAL = 3000;//In milliseconds



    public Level3A() {
        super(TITLE, MAIN_COLOR);
    }


    @Override
    protected void setupLevel() {
        triangle = new RandomShape(3);
    }

    @Override
    protected ArrayList<Player> setupPlayers() {

        ArrayList<Player> players=new ArrayList<>();

        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.RED, Level.players.get(0)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.GREEN, Level.players.get(1)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.BLUE, Level.players.get(2)));

        return players;
    }



    @Override
    public void update() {
        // TODO decidir si se ha imitdo bien la forma (mirar W5C)
        // TODO si sí --> timer --> cambiar forma (puntos; se dibuja luego)

        if (triangle.playersMatch(players)) {
            if (System.currentTimeMillis() - timer >= MAX_INTERVAL) {
                triangle.updateRamdomShape();
                timer = System.currentTimeMillis();
            }
        }
    }


    @Override
    protected void drawLevel() {
        pg.beginDraw();
        pg.noStroke();
        pg.fill(Color.WHITE);
        pg.beginShape();
        for (int i = 0; i < triangle.getVertexNumber(); i++) {
            pg.vertex(triangle.getVertex(i).x, triangle.getVertex(i).y);
        }
        pg.endShape();
        pg.endDraw();
    }

//    void createRandomTriangle(boolean doRandom) {
//        float distance1, distance2, distance3;
//        do {
//            if (doRandom) {
//                //triangle[0] = random(0, LEVEL_WIDTH);
//                //triangle[1] = random(0, LEVEL_HEIGHT);
//                //triangle[2] = random(0, LEVEL_WIDTH);
//                //triangle[3] = random(0, LEVEL_HEIGHT);
//                //triangle[4] = random(0, LEVEL_WIDTH);
//                //triangle[5] = random(0, LEVEL_HEIGHT);
//                float r = Utils.randomInt(20, round(LEVEL_WIDTH /2));
//                float rr = Utils.randomInt(0, round(LEVEL_HEIGHT / 2));
//
//                triangle[0] = (LEVEL_WIDTH /2) - r;
//                triangle[1] = LEVEL_HEIGHT - rr;
//                triangle[2] = (LEVEL_WIDTH /2) + r;
//                triangle[3] = LEVEL_HEIGHT - rr;
//                triangle[4] = (LEVEL_WIDTH /2);
//                triangle[5] = rr;
//
//                //triangle[0] = LEVEL_WIDTH /2;
//                //triangle[1] = rr;
//                //triangle[2] = (LEVEL_WIDTH /2) + r;
//                //triangle[3] = (LEVEL_HEIGHT /2) -rr;
//                //triangle[4] = (LEVEL_WIDTH /2) - r;
//                //triangle[5] = (LEVEL_HEIGHT /2) - rr;
//            }
//
//            distance1 = dist(triangle[0], triangle[1], triangle[2], triangle[3]);
//            distance2 = dist(triangle[0], triangle[1], triangle[4], triangle[5]);
//            distance3 = dist(triangle[4], triangle[5], triangle[2], triangle[3]);
//        } while ((distance1 < DIST_MIN) && (distance2 < DIST_MIN) && (distance3 < DIST_MIN));
//    }




}
