package es.geoplanosocial.levels.world2.c;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.ExtendedNode;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Color;
import es.geoplanosocial.util.Constants;
import es.geoplanosocial.util.Utils;
import processing.core.PApplet;

import java.util.ArrayList;

import static processing.core.PApplet.*;

/**
 * World 2
 * Level B
 * Created by gbermejo on 27/05/17.
 */
public class Level2C extends Level {

    private static final String TITLE="P2P";
    public static final int MAIN_COLOR= Color.W2_C_BG;
    private static final float STROKEWEIGHT_LEVEL2B = 10;
    public static final int MAX_DISTANCE = (int)round((float) (Constants.LEVEL_HEIGHT * 0.8));

    private float ancho;

    private long timer = System.currentTimeMillis();

    private final float FULLNESS_INCREASE = 0.01f;
    private final float FULLNESS_DECREASE = 0.002f;

    private final float MIN_DIST = 10.0f;

    float distanciaAnterior = 0;

    private final int MAX_INTERVAL = 500;//In milliseconds


    public Level2C() {
        super(TITLE, MAIN_COLOR);
    }


    @Override
    protected void setupLevel() {
        setDrawPlayersFront(true);
    }

    @Override
    protected ArrayList<Player> setupPlayers() {

        ArrayList<Player> players=new ArrayList<>();

        for (Player p : Level.players) {
            ExtendedNode pl = (ExtendedNode) Player.Factory.getPlayer(Player.Type.EXTENDED_NODE, Color.W1_WHITE_NODE, p);
            pl.setAlpha(1.0f);
            pl.setFullness(0.0f);

            players.add(pl);
        }

        return players;
    }



    @Override
    public void update() {
        float x1 = (float)players.get(0).getLocation().getX();
        float y1 = (float)players.get(0).getLocation().getY();
        float x2 = (float)players.get(1).getLocation().getX();
        float y2 = (float)players.get(1).getLocation().getY();
        float distancia = dist(x1, y1, x2, y2);
        ancho = map(distancia, 0, MAX_DISTANCE, 0, STROKEWEIGHT_LEVEL2B);
        ancho = constrain(ancho, 0, STROKEWEIGHT_LEVEL2B);

        if (System.currentTimeMillis() - timer >= MAX_INTERVAL) {
            distanciaAnterior = distancia;
            timer = System.currentTimeMillis();//Reset timer
        }

        float fIncrease = 0;
        float fullness = ((ExtendedNode) players.get(0)).getFullness();

        if (abs(distancia - distanciaAnterior) > MIN_DIST)
            fIncrease =+ FULLNESS_INCREASE;
        else
            fIncrease =- FULLNESS_DECREASE;


        ((ExtendedNode) players.get(0)).setFullness(PApplet.constrain(fullness + fIncrease,0,1));
        ((ExtendedNode) players.get(1)).setFullness(PApplet.constrain(fullness + fIncrease,0,1));

        if(((ExtendedNode) players.get(0)).getFullness()>=1.0f && !isCompleted())nextLevel();
    }


    @Override
    protected void drawLevel() {
        pg.beginDraw();
        pg.strokeWeight(STROKEWEIGHT_LEVEL2B - ancho);
        pg.stroke(Color.W2_WHITE_NODE);
        // pg.line(x1, y1, x2, y2);
        pg.line((float)players.get(0).getLocation().getX(),
                (float)players.get(0).getLocation().getY(),
                (float)players.get(1).getLocation().getX(),
                (float)players.get(1).getLocation().getY());
        pg.endDraw();
    }


}
