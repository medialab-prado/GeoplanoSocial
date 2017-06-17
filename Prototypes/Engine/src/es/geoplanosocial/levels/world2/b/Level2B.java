package es.geoplanosocial.levels.world2.b;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Color;
import es.geoplanosocial.util.Constants;
import es.geoplanosocial.util.Utils;

import java.util.ArrayList;

import static processing.core.PApplet.constrain;
import static processing.core.PApplet.dist;
import static processing.core.PApplet.map;

/**
 * World 2
 * Level B
 * Created by gbermejo on 27/05/17.
 */
public class Level2B extends Level {

    private static final String TITLE="Sinergia";
    public static final int MAIN_COLOR= Color.W2_C_BG;
    private static final float STROKEWEIGHT_LEVEL2B = 10;

    private float ancho;

    public Level2B() {
        super(TITLE, MAIN_COLOR);
    }


    @Override
    protected void setupLevel() {
        setDrawPlayersFront(true);
    }

    @Override
    protected ArrayList<Player> setupPlayers() {

        ArrayList<Player> players=new ArrayList<>();

        for (Player p :Level.players){
            players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W2_WHITE_NODE, p));
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
        ancho = map(distancia, 0, Constants.LEVEL_WIDTH, 0, STROKEWEIGHT_LEVEL2B);
        ancho = constrain(ancho, 0, STROKEWEIGHT_LEVEL2B);
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
