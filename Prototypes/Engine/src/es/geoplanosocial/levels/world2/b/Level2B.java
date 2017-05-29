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
    private static final int MAIN_COLOR= Color.GREY;
    private static final float STROKEWEIGHT_LEVEL2B = 10;

    private float ancho;

    public Level2B() {
        super(TITLE, MAIN_COLOR);
    }


    @Override
    protected void setupLevel() {
        //TODO Level specific setup
    }

    @Override
    protected ArrayList<Player> setupPlayers() {

        ArrayList<Player> players=new ArrayList<>();

        for (int i=0;i<Level.players.size();i++){
            Player p = Level.players.get(i);
            int nodeColor;
            if(i%2==0){
                nodeColor=Color.BLACK;
            }else{
                nodeColor=Color.WHITE;
            }

            players.add(Player.Factory.getPlayer(Player.Type.NODE2B, nodeColor, p));
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
        pg.stroke(Color.WHITE);
        // pg.line(x1, y1, x2, y2);
        pg.line((float)players.get(0).getLocation().getX(),
                (float)players.get(0).getLocation().getY(),
                (float)players.get(1).getLocation().getX(),
                (float)players.get(1).getLocation().getY());
        pg.endDraw();
    }


}