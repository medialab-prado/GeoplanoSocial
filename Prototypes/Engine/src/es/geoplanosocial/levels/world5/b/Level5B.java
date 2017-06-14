package es.geoplanosocial.levels.world5.b;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.players.Square;
import es.geoplanosocial.players.VisiblePlayer;
import es.geoplanosocial.util.Color;
import es.geoplanosocial.util.Utils;

import javax.rmi.CORBA.Util;
import java.util.ArrayList;

/**
 * World 5
 * Level B
 * Created by gbermejo on 27/05/17.
 */
public class Level5B extends Level {

    private static final String TITLE="Rodea-la-minoría";
    public static final int MAIN_COLOR= Color.W5_B_BG;

    private boolean surroundedMinority = false;

    private ArrayList<Player> surrounderers;
    private ArrayList<Player> surrounded;


    private final float MORPH_INTERVAL = 3000;//In milliseconds
    private long timer = System.currentTimeMillis();


    public Level5B() {
        super(TITLE, MAIN_COLOR);
    }


    @Override
    protected void setupLevel() {
        //TODO Level specific setup
        shuffleRoles();
    }

    @Override
    protected ArrayList<Player> setupPlayers() {

        ArrayList<Player> players=new ArrayList<>();

        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W5_PINK_NODE, Level.players.get(0)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W5_BLUE_NODE, Level.players.get(1)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W5_YELLOW_NODE, Level.players.get(2)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W5_RED_NODE, Level.players.get(3)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W5_GREEN_NODE, Level.players.get(4)));


        return players;
    }



    @Override
    public void update() {

        //By default no player is considered surrounded
        boolean currentSurroundedMinority = isMinoritySurrounded();

        //Change of state
        if(surroundedMinority!=currentSurroundedMinority){
            timer = System.currentTimeMillis();//Reset timer
        }

        //Set current state
        surroundedMinority = currentSurroundedMinority;

        long tempTimer=System.currentTimeMillis();
        if(tempTimer-timer>=MORPH_INTERVAL) {
            if(surroundedMinority) {
                //Change targets
                shuffleRoles();
            }

        }else{//Morph
            float percentage = (tempTimer-timer)/MORPH_INTERVAL;
            if(!surroundedMinority) {
                percentage=1-percentage;
            }
            morphSurrounded(percentage);

        }

    }

    private void shuffleRoles(){
        setPlayersRole();
        refreshPlayers();
    }

    private void refreshPlayers() {
        ArrayList<Player> players=new ArrayList<>();
        players.addAll(surrounded);
        players.addAll(surrounderers);
        super.refreshPlayers(players);
    }

        //FIXME update to general case
    private void setPlayersRole() {
        int a=0 , b=0;


        //Get two different
        while(a==b){
            a=Utils.randomInt(0, players.size()-1);
            b=Utils.randomInt(0, players.size()-1);
        }

        surrounderers = new ArrayList<>();
        surrounded = new ArrayList<>();

        for(int i=0; i<players.size();i++){
            Player p = players.get(i);
            if(p.isPlaying()){
                VisiblePlayer vp = (VisiblePlayer)p;
                if(i==a || i==b){
                    surrounded.add(Player.Factory.getPlayer(Player.Type.SQUARE, vp.getColor(), vp));
                }else{
                    surrounderers.add(Player.Factory.getPlayer(Player.Type.NODE, vp.getColor(), vp));
                }

            }
        }
    }

    private void morphSurrounded(float percentage) {
        Utils.log("Morphed: "+ percentage*100);
        for(Player p: surrounded){
            Square s = ((Square)p);
            s.setRadiiPercentage(percentage);
        }
    }

    private boolean isMinoritySurrounded() {
        boolean isSurrounded = false;

        int numberSurrounded = 0;

        //Check if surrounded are really surrounded
        for(Player target: surrounded){
            if(target.isPlaying() && target.isSurrounded(surrounderers)){
                numberSurrounded++;
            }
        }

        if (surrounded.size() == numberSurrounded)isSurrounded=true;

        return isSurrounded;
    }


    @Override
    protected void drawLevel() {
        if(surroundedMinority) {
            pg.beginDraw();
            pg.noStroke();
            pg.fill(Color.WHITE_ALPHA);
            pg.beginShape();
            for (Player p : surrounderers) {
                pg.vertex(p.getBoundingBox().x, p.getBoundingBox().y);
            }
            pg.endShape(pg.CLOSE);
            pg.endDraw();
        }
    }


}
