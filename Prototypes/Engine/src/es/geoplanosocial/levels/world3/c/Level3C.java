package es.geoplanosocial.levels.world3.c;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Color;
import es.geoplanosocial.util.Constants;

import java.util.ArrayList;

import static processing.core.PApplet.*;

/**
 * World 3
 * Level C
 * Created by gbermejo on 27/05/17.
 */
public class Level3C extends Level {

    private static final String TITLE="P3P";
    public static final int MAIN_COLOR= Color.W3_C_BG;
    private static final float STROKEWEIGHT_LEVEL3B = 10;
    public static final int MAX_DISTANCE = (int)round((float) (Constants.LEVEL_HEIGHT * 0.8));




    private float[][] ancho;

    public Level3C() {
        super(TITLE, MAIN_COLOR);
        setDrawPlayersFront(true);
    }


    @Override
    protected void setupLevel() {
        //TODO Level specific setup
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
        ancho = new float[this.players.size()][this.players.size()];
        for (int i = 0; i < this.players.size(); i++)
            for (int j = 0; j < this.players.size(); j++)
                if (i != j) {
                    float x1 = (float)players.get(i).getLocation().getX();
                    float y1 = (float)players.get(i).getLocation().getY();
                    float x2 = (float)players.get(j).getLocation().getX();
                    float y2 = (float)players.get(j).getLocation().getY();
                    float distancia = dist(x1, y1, x2, y2);
                    ancho[i][j] = map(distancia, 0, MAX_DISTANCE, 0, STROKEWEIGHT_LEVEL3B);
                    ancho[i][j] = constrain(ancho[i][j], 0, STROKEWEIGHT_LEVEL3B);
                }
    }


    @Override
    protected void drawLevel() {
        pg.beginDraw();
        pg.stroke(Color.W2_WHITE_NODE);
        for (int i = 0; i < this.players.size(); i++)
            for (int j = 0; j < this.players.size(); j++)
                if (i != j) {
                    pg.strokeWeight(STROKEWEIGHT_LEVEL3B - ancho[i][j]);
                    pg.line((float)players.get(i).getLocation().getX(),
                            (float)players.get(i).getLocation().getY(),
                            (float)players.get(j).getLocation().getX(),
                            (float)players.get(j).getLocation().getY());
                }
        pg.endDraw();
    }


}
