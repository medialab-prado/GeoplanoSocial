package es.geoplanosocial.levels.world2.b;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Color;
import es.geoplanosocial.util.Utils;

import java.util.ArrayList;

/**
 * World 2
 * Level B
 * Created by gbermejo on 27/05/17.
 */
public class Level2B extends Level {

    private static final String TITLE="Sinergia";
    private static final int MAIN_COLOR= Color.GREY;

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

            players.add(Player.Factory.getPlayer(Player.Type.NODE, nodeColor, p));
        }

        return players;
    }



    @Override
    public void update() {
        //TODO Update level elements

    }


    @Override
    protected void drawLevel() {
        //TODO Draw level elements
    }


}
