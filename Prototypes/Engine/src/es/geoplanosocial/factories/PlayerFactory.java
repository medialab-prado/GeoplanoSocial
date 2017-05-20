package es.geoplanosocial.factories;

import es.geoplanosocial.players.Node;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.tracker.Blob;

import es.geoplanosocial.util.Types;
import processing.core.PGraphics;

/**
 * Factory for generating players.
 * Created by gbermejo on 20/05/17.
 */
public class PlayerFactory {
    public static Player getPlayer(Blob blob){
        return new Player(blob.getId(), blob.getBoundingBox()) {
            @Override
            public void draw(PGraphics pg) {

            }
        };
    }

    public static Player getPlayer(Types.Player type, int color, Player player){

        Player p;

        switch (type){
            case NODE:
                p=new Node(color,player);
                break;
            default:
                p=getPlayer(new Blob(player.getId(), player.getBoundingBox()));
        }
        return p;
    }
}
