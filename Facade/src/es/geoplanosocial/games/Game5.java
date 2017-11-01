package es.geoplanosocial.games;

import es.geoplanosocial.players.Node;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Color;
import es.geoplanosocial.util.Utils;

import java.awt.*;
import java.util.ArrayList;

/**
 * Default game layout
 * Created by guzman on 01/11/2017.
 */
public class Game5 extends Game {
    private static final String TITLE="TODO";

    protected Game5() {
        super(TITLE);
    }

    @Override
    public void update() {

        //FIXME just as showcase, move automatically to next level
        for(int i=0;i<players.size();i++){

            Player p1 = players.get(i);
            if(p1.getState()!= Player.State.PLAYING)continue;

            for(int j=i+1;j<players.size();j++){

                Player p2 = players.get(j);
                if(p2.getState()!= Player.State.PLAYING)continue;

                Point center1 = p1.getBoundingBox().getLocation();
                float radius1 = p1.getBoundingBox().width/2.0f;

                Point center2 = p2.getBoundingBox().getLocation();
                float radius2 = p2.getBoundingBox().width/2.0f;

                if(Utils.isCircleCollision(center1,radius1,center2,radius2)){
                    nextLevel();
                }
            }
        }

    }

    @Override
    protected void draw() {

    }

    @Override
    protected void setup() {

    }

    @Override
    protected ArrayList<Player> setupPlayers() {
        ArrayList<Player> players=new ArrayList<>();
        for (Player p : Game.players){
            Node pl =(Node)Player.Factory.getPlayer(Player.Type.NODE, Color.WHITE, p);

            //FIXME just as showcase, act depending on level
            Rectangle bb = pl.getBoundingBox();
            bb.setSize(bb.width+getCurrentLevel(), bb.height+getCurrentLevel());
            pl.setBoundingBox(bb);

            players.add(pl);
        }
        return players;
    }
}
