package es.geoplanosocial.levels.world1.a;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.Node;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Color;
import es.geoplanosocial.util.Types;
import org.w3c.dom.css.Rect;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static es.geoplanosocial.util.Constants.LEVEL_HEIGHT;
import static es.geoplanosocial.util.Constants.LEVEL_WIDTH;
import static es.geoplanosocial.util.Utils.randomInt;

/**
 * World 1
 * Level A
 * Created by gbermejo on 15/05/17.
 */
public class Level1A extends Level {

    private static final String TITLE="Huella";
    private static final int MAIN_COLOR= Color.BLACK;

    private final int MAX_INTERVAL = 250;//In milliseconds
    private final Rectangle[] DUMMIES = new Rectangle[50];
    private final int DUMMY_SIZE_FACTOR = 2;
    private final int DUMMY_POSITION_OFFSET = 30;


    private int index = 0;
    private long timer = System.currentTimeMillis();




    public Level1A() {
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
            players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.WHITE, p));
        }
        return players;
    }



    @Override
    public void update() {
        //Update level elements
        if(System.currentTimeMillis()-timer>=MAX_INTERVAL){

            DUMMIES[index] = generateDummy();

            index = ++index % DUMMIES.length; //Cyclic index

            timer = System.currentTimeMillis();//Reset timer
        }

    }


    @Override
    protected void drawLevel() {

        //Draw dummies
        pg.beginDraw();
        pg.noStroke();
        pg.fill(((Node)players.get(0)).getColor());

        for(int i=0; i<DUMMIES.length;i++){
            if(DUMMIES[i]!=null)
                pg.ellipse(DUMMIES[i].x, DUMMIES[i].y, DUMMIES[i].width, DUMMIES[i].height);
        }

        pg.endDraw();
    }


    //Generates a point adjacent to the player
    private Rectangle generateDummy(){


        Rectangle pBB  = players.get(0).getBoundingBox();

        int size = randomInt(pBB.width/DUMMY_SIZE_FACTOR, pBB.width*DUMMY_SIZE_FACTOR);

        int x = randomInt(processing.constrain(pBB.x-DUMMY_POSITION_OFFSET, 0,LEVEL_WIDTH),
                processing.constrain(pBB.x+DUMMY_POSITION_OFFSET, 0,LEVEL_WIDTH));

        int y = randomInt(processing.constrain(pBB.y-DUMMY_POSITION_OFFSET, 0,LEVEL_HEIGHT),
                processing.constrain(pBB.y+DUMMY_POSITION_OFFSET, 0,LEVEL_HEIGHT));

        return new Rectangle(x, y, size, size);
    }

}
