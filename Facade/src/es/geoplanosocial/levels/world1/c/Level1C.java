package es.geoplanosocial.levels.world1.c;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.ExtendedNode;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Color;
import es.geoplanosocial.util.Types;
import es.geoplanosocial.util.Utils;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

import java.awt.*;
import java.util.ArrayList;

import static es.geoplanosocial.util.Constants.LEVEL_HEIGHT;
import static es.geoplanosocial.util.Constants.LEVEL_WIDTH;
import static es.geoplanosocial.util.Constants.SCREEN_RENDERER;
import static es.geoplanosocial.util.Utils.randomInt;

/**
 * World 1
 * Level C
 * Created by gbermejo on 15/05/17.
 */
public class Level1C extends Level {

    private static final String TITLE = "Rastro";
    public static final int MAIN_COLOR = Color.W1_C_BG;


    private final int MAX_INTERVAL = 1000;//In milliseconds
    // private final Line[] LINES = new Line[50];
    private Line line = null;
    private final int LINE_WEIGHT_MIN = 1;
    private final int LINE_WEIGHT_MAX = 3;

    private PGraphics acetato;
    private PGraphics mask;
    private final int ERASER_EXTRA_SIZE = 5;

    private int index = 0;
    private long timer = System.currentTimeMillis();

    private final float FULLNESS_INCREASE = 0.005f;
    private final float FULLNESS_DECREASE = 0.002f;

    public Level1C() {
        super(TITLE, MAIN_COLOR);
    }

    @Override
    protected void setupLevel() {
        //Level specific setup
        acetato = processing.createGraphics(LEVEL_WIDTH, LEVEL_HEIGHT, SCREEN_RENDERER);
//        acetato.beginDraw();
//        acetato.background(0, 0);
//        acetato.endDraw();
        mask = processing.createGraphics(LEVEL_WIDTH, LEVEL_HEIGHT, SCREEN_RENDERER);
        mask.beginDraw();
        mask.background(Color.BLACK);
        mask.endDraw();
    }

    @Override
    protected ArrayList<Player> setupPlayers() {

        //Init specific players
        ArrayList<Player> players = new ArrayList<>();

        for (Player p : Level.players) {
            ExtendedNode pl = (ExtendedNode) Player.Factory.getPlayer(Player.Type.EXTENDED_NODE, Color.W1_WHITE_NODE, p);

            // TODO
            pl.setAlpha(1.0f);
            pl.setFullness(0.0f);

            players.add(pl);
        }
        return players;
    }


    @Override
    public void update() {
        //Update level elements

        if (System.currentTimeMillis() - timer >= MAX_INTERVAL) {

            line = generateLine();


            timer = System.currentTimeMillis();//Reset timer

            acetato.beginDraw();
            acetato.stroke(Color.WHITE);
            acetato.strokeWeight(line.weight);
            acetato.line(line.p1.x, line.p1.y, line.p2.x, line.p2.y);
            acetato.endDraw();

            mask.beginDraw();
            mask.stroke(Color.WHITE);
            mask.strokeWeight(line.weight);
            mask.line(line.p1.x, line.p1.y, line.p2.x, line.p2.y);
            mask.endDraw();

        }

        // change node according level accomplishment?
        PImage nodePixelColors = acetato.get(players.get(0).getLocation().x,
                players.get(0).getLocation().y,
                players.get(0).getBoundingBox().width,
                players.get(0).getBoundingBox().height);

        // int a = ((ExtendedNode) players.get(0)).getColor();
        int a = Color.WHITE;

        // Utils.log("a = " + a);

        float fullness = ((ExtendedNode) players.get(0)).getFullness();
        float fIncrease = 0;

        boolean match = false;

        for (int i = 0; i < players.get(0).getBoundingBox().width * players.get(0).getBoundingBox().height; i++) {
            // Utils.log("p" + nodePixelColors.pixels[i]);
            if (nodePixelColors.pixels[i] == a ) {
                fIncrease =+ FULLNESS_INCREASE;
                match = true;
                break;
            }
        }

        if (!match) fIncrease =- FULLNESS_DECREASE;

        ((ExtendedNode) players.get(0)).setFullness(PApplet.constrain(fullness + fIncrease,0,1));


        mask.beginDraw();
        mask.fill(Color.BLACK);
        mask.noStroke();
        mask.ellipse(players.get(0).getLocation().x,
                players.get(0).getLocation().y,
                players.get(0).getBoundingBox().width + ERASER_EXTRA_SIZE,
                players.get(0).getBoundingBox().height + ERASER_EXTRA_SIZE);
        mask.endDraw();

        acetato.mask(mask);

        if(((ExtendedNode) players.get(0)).getFullness()>=1.0f && !isCompleted())nextLevel();
    }

    @Override
    protected void drawLevel() {
        //Draw lines
        pg.beginDraw();
        pg.image(acetato, 0, 0);
        pg.endDraw();
    }


    //Generates a line
    private Line generateLine() {

        Line line;

        do {
            //Starting point
            Types.Direction startSide = Types.Direction.getRandom();

            int startingX = 0, startingY = 0;

            switch (startSide) {
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

            //Ending point
            Types.Direction endSide;

            do {
                endSide = Types.Direction.getRandom();
            }
            while (startSide.equals(endSide));

            int endingX = 0, endingY = 0;

            switch (endSide) {
                case UP:
                    endingX = randomInt(0, LEVEL_WIDTH);
                    endingY = 0;
                    break;
                case DOWN:
                    endingX = randomInt(0, LEVEL_WIDTH);
                    endingY = LEVEL_HEIGHT;
                    break;
                case LEFT:
                    endingX = 0;
                    endingY = randomInt(0, LEVEL_HEIGHT);
                    break;
                case RIGHT:
                    endingX = LEVEL_WIDTH;
                    endingY = randomInt(0, LEVEL_HEIGHT);
                    break;
            }

            int weight = randomInt(LINE_WEIGHT_MIN, LINE_WEIGHT_MAX);

            line = new Line(new Point(startingX, startingY),
                    new Point(endingX, endingY),
                    weight);
        }
        while (Utils.circleLineIntersect(line.p1.x,
                line.p1.y,
                line.p2.x,
                line.p2.y,
                players.get(0).getLocation().x,
                players.get(0).getLocation().y,
                players.get(0).getBoundingBox().width));

        return line;
    }

    private class Line {
        public Point p1, p2;
        public int weight;

        public Line(Point p1, Point p2, int weight) {
            this.p1 = p1;
            this.p2 = p2;
            this.weight = weight;
        }
    }
}
