package es.geoplanosocial.levels.world4.c;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Color;
import es.geoplanosocial.util.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;


/**
 * World 4
 * Level C
 * Created by gbermejo on 27/05/17.
 */
public class Level4C extends Level {

    private static final String TITLE = "Rompelíneas";
    public static final int MAIN_COLOR = Color.W4_C_BG;

    private int[][] groups;
    private int activeGroup;

    private final int LINE_COLOR = Color.WHITE;
    private final int LINE_WIDTH = 2;

    private final int MIDDLE_RADIUS = 10;
    private final int MIDDLE_FILL_COLOR = Color.WHITE;
    private final int MIDDLE_STROKE_COLOR = Color.BLACK;

    private Point middle;

    public Level4C() {
        super(TITLE, MAIN_COLOR);
    }


    @Override
    protected void setupLevel() {
        setDrawPlayersFront(true);
        makeGroups();
        activeGroup = 0;
        middle =  new Point();
    }

    private void makeGroups() {
        groups = new int[2][2];

        ArrayList<Integer> temp = new ArrayList<>(4);
        for(int i=0;i<4;i++){
            temp.add(i);
        }
        Collections.shuffle(temp);

        //Convert to 2d array
        for(int i=0;i<2;i++){
            for(int j=0;j<2;j++) {
                groups [i][j] = temp.get(i*2+j);
            }
        }
    }


    @Override
    protected ArrayList<Player> setupPlayers() {

        ArrayList<Player> players = new ArrayList<>();

        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W4_BLACK_NODE, Level.players.get(0)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W4_YELLOW_NODE, Level.players.get(1)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W4_RED_NODE, Level.players.get(2)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W4_BLUE_NODE, Level.players.get(3)));

        return players;
    }


    @Override
    public void update() {
        int passiveGroup = (activeGroup+1)%2;

        //Initial node
        Point p1 = Level.players.get(groups[passiveGroup][0]).getLocation();

        //Ending node
        Point p2 = Level.players.get(groups[passiveGroup][1]).getLocation();


        //Middle point
        //middle.move(Math.abs(p2.x-p1.x)/2 + Math.min(p1.x,p2.x),Math.abs(p2.y-p1.y)/2 + Math.min(p1.y,p2.y));
        middle.move((p2.x+p1.x)/2 ,(p2.y+p1.y)/2);


        //Check collision
        for(int i = 0;i<groups[activeGroup].length;i++){
            Point p = Level.players.get(groups[activeGroup][i]).getLocation();
            float r = Level.players.get(groups[activeGroup][i]).getBoundingBox().width/2.0f;

            if(Utils.isCircleCollision(p, r, middle, MIDDLE_RADIUS)){
                changeActiveGroup();
            }
        }


    }

    private void changeActiveGroup() {
        activeGroup = (activeGroup+1)%2;
    }


    @Override
    protected void drawLevel() {

        int passiveGroup = (activeGroup+1)%2;

        //Initial node
        Point p1 = Level.players.get(groups[passiveGroup][0]).getLocation();

        //Ending node
        Point p2 = Level.players.get(groups[passiveGroup][1]).getLocation();


        //Draw line between nodes
        pg.beginDraw();
        pg.stroke(LINE_COLOR);
        pg.strokeWeight(LINE_WIDTH);

        pg.line(p1.x,p1.y,p2.x,p2.y);

        pg.fill(MIDDLE_FILL_COLOR);
        pg.stroke(MIDDLE_STROKE_COLOR);
        pg.ellipse(middle.x, middle.y, MIDDLE_RADIUS, MIDDLE_RADIUS);

        pg.endDraw();
    }
}
