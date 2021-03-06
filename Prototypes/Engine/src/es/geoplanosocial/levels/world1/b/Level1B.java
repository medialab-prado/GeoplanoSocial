package es.geoplanosocial.levels.world1.b;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.ExtendedNode;
import es.geoplanosocial.players.Node;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Color;
import es.geoplanosocial.util.Utils;
import processing.core.PVector;

import java.awt.*;
import java.util.ArrayList;

import static es.geoplanosocial.util.Constants.LEVEL_HEIGHT;
import static es.geoplanosocial.util.Constants.LEVEL_WIDTH;
import static es.geoplanosocial.util.Configuration.playerSize;
import static es.geoplanosocial.util.Utils.randomInt;

/**
 * World 1
 * Level B
 * Created by gbermejo on 15/05/17.
 */
public class Level1B extends Level {

    private static final String TITLE="Rebaño";
    public static final int MAIN_COLOR= Color.W1_B_BG;

    private final int DUMMIES_NUMBER = 10;
    private Rectangle[] DUMMIES ;

    private final int DUMMY_SIZE_FACTOR = 2;
    private int DUMMY_SIZE_MAX;
    private int DUMMY_SIZE_MIN;

    private final float DUMMY_SPEED = 0.0001f;
    private final int DUMMY_POSITION_OFFSET = 50;


    private long timer = System.currentTimeMillis();

    private final float TRANSFERENCE = 0.0025f;


    public Level1B() {
        super(TITLE, MAIN_COLOR);
    }


    @Override
    protected void setupLevel() {
        generateDummies();
    }

    @Override
    protected ArrayList<Player> setupPlayers() {

        //Init specific players
        ArrayList<Player> players=new ArrayList<>();

        for (Player p :Level.players){
            ExtendedNode pl =(ExtendedNode)Player.Factory.getPlayer(Player.Type.EXTENDED_NODE, Color.W1_WHITE_ALPHA_NODE, p);
            pl.setFullness(0.0f);
            players.add(pl);
        }


        return players;
    }

    private void generateDummies() {
        //Generate all dummies at random places
        DUMMIES = new Rectangle[DUMMIES_NUMBER];
        DUMMY_SIZE_MIN = playerSize /DUMMY_SIZE_FACTOR;
        DUMMY_SIZE_MAX = playerSize *DUMMY_SIZE_FACTOR;
        for(int i=0; i<DUMMIES.length;i++){
            DUMMIES[i] = generateDummy();
        }
    }


    @Override
    public void update() {
        //Update level elements

        Player player = players.get(0);

        if(player!=null){

            ExtendedNode p = ((ExtendedNode)players.get(0));

            long currentTime = System.currentTimeMillis();
            long interval = currentTime -timer;

            Rectangle playerBB = player.getBoundingBox();

            float distanceInInterval = DUMMY_SPEED * interval;

            //Move dummies towards player position
            for(int i=0; i<DUMMIES.length;i++){

                PVector v = new PVector(playerBB.x-DUMMIES[i].x+Utils.randomInt(-DUMMY_POSITION_OFFSET,DUMMY_POSITION_OFFSET),
                        playerBB.y-DUMMIES[i].y+Utils.randomInt(-DUMMY_POSITION_OFFSET,DUMMY_POSITION_OFFSET));

                //TODO maybe Verlet integration and circles collisions?
                float sizeFactor = (DUMMY_SIZE_MAX-DUMMIES[i].getSize().width + 1.0f)/(DUMMY_SIZE_MAX-DUMMY_SIZE_MIN);
                float distancePerSize = distanceInInterval * sizeFactor;
                v.normalize();
                v.mult(distancePerSize);

                //int prevX= DUMMIES[i].x;
                //int prevY= DUMMIES[i].y;

                DUMMIES[i].x += v.x > 0 ? Math.ceil(v.x):Math.floor(v.x);
                DUMMIES[i].y += v.y > 0 ? Math.ceil(v.y):Math.floor(v.y);

                //if(prevX ==DUMMIES[i].x && prevY ==DUMMIES[i].y) Utils.log(""+v);
            }

            Point center = new Point();
            boolean isCollision = false;
            //Check collisions with players
            for(int i=0; i<DUMMIES.length;i++){
                center.move(DUMMIES[i].x,DUMMIES[i].y);
                if(Utils.isCircleCollision(p.getLocation(),p.getBoundingBox().width/2.0f,center,DUMMIES[i].width/2.0f)){
                    isCollision=true;
                    break;
                }
            }

            if(isCollision){
                p.setFullness(p.getFullness()-TRANSFERENCE);

            }else{
                p.setFullness(p.getFullness()+TRANSFERENCE);
            }

            if(p.getFullness()>=1.0f && !isCompleted())nextLevel();

            timer = currentTime;
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


    @Override
    public void addPlayers(ArrayList<Player> newPlayers) {
        for (Player p : newPlayers){
            Level.players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.BLACK_ALPHA, p));
        }
    }

    //Generates a point adjacent to the player
    private Rectangle generateDummy(){

        int size = randomInt(DUMMY_SIZE_MIN, DUMMY_SIZE_MAX);

        int x = randomInt(0, LEVEL_WIDTH);

        int y = randomInt(0, LEVEL_HEIGHT);

        return new Rectangle(x, y, size, size);
    }

}
