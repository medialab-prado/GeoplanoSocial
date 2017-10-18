package es.geoplanosocial.levels.world1.c;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.Node;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Color;
import es.geoplanosocial.util.Types;

import java.awt.*;
import java.util.ArrayList;

import static es.geoplanosocial.util.Constants.LEVEL_HEIGHT;
import static es.geoplanosocial.util.Constants.LEVEL_WIDTH;
import static es.geoplanosocial.util.Utils.randomInt;

/**
 * World 1
 * Level C
 * Created by gbermejo on 15/05/17.
 */
public class Level1C extends Level {

    private static final String TITLE="Rastro";
    public static final int MAIN_COLOR= Color.W1_C_BG;


    private final int MAX_INTERVAL = 250;//In milliseconds
    private final Line[] LINES = new Line[50];
    private final int LINE_WEIGHT_MIN = 1;
    private final int LINE_WEIGHT_MAX = 3;


    private int index = 0;
    private long timer = System.currentTimeMillis();

    public Level1C() {
        super(TITLE, MAIN_COLOR);
    }

    @Override
    protected void setupLevel() {
        //Level specific setup
    }

    @Override
    protected ArrayList<Player> setupPlayers() {

        //Init specific players
        ArrayList<Player> players=new ArrayList<>();

        for (Player p :Level.players){
            players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W1_WHITE_NODE, p));
        }
        return players;
    }



    @Override
    public void update() {
        //Update level elements
        if(System.currentTimeMillis()-timer>=MAX_INTERVAL){

            LINES[index] = generateLine();

            index = ++index % LINES.length; //Cyclic index

            timer = System.currentTimeMillis();//Reset timer
        }

    }


    @Override
    protected void drawLevel() {
        //Draw lines
        pg.beginDraw();
        pg.stroke(((Node)players.get(0)).getColor());

        for(int i=0; i<LINES.length;i++){
            if(LINES[i]!=null) {
                pg.strokeWeight(LINES[i].weight);
                pg.line(LINES[i].p1.x, LINES[i].p1.y, LINES[i].p2.x, LINES[i].p2.y);
            }
        }

        pg.noStroke();
        pg.endDraw();
    }


    //Generates a line
    private Line generateLine(){

        //Starting point
        Types.Direction side = Types.Direction.getRandom();

        int startingX=0, startingY=0;

        switch (side) {
            case UP:
                startingX = randomInt(0, LEVEL_WIDTH);
                startingY = 0;
                break;
            case DOWN:
                startingX = randomInt(0, LEVEL_WIDTH);
                startingY = LEVEL_HEIGHT;
                break;
            case LEFT:
                startingX = 0;
                startingY = randomInt(0, LEVEL_HEIGHT);
                break;
            case RIGHT:
                startingX = LEVEL_WIDTH;
                startingY = randomInt(0, LEVEL_HEIGHT);
                break;
        }

        Rectangle pBB  = players.get(0).getBoundingBox();

        float m = (pBB.y-startingY)/(pBB.x-startingX+0.0f);

        float n = startingY - m * startingX;

        int endingX=0, endingY=0;

        switch (side) {
            case UP:
                endingY = LEVEL_HEIGHT;
                endingX = (int)((endingY-n)/m);
                break;
            case DOWN:
                endingY = 0;
                endingX = (int)((endingY-n)/m);
                break;
            case LEFT:
                endingX = LEVEL_WIDTH;
                endingY = (int) (m*endingX+n);
                break;
            case RIGHT:
                endingX = 0;
                endingY = (int) (m*endingX+n);
                break;
        }

        int weight = randomInt(LINE_WEIGHT_MIN, LINE_WEIGHT_MAX);

        return new Line(new Point(startingX,startingY),
                new Point(endingX,endingY),
                weight);
    }


    private class Line{
        public Point p1, p2;
        public int weight;

        public Line(Point p1, Point p2, int weight) {
            this.p1 = p1;
            this.p2 = p2;
            this.weight = weight;
        }
    }
}
