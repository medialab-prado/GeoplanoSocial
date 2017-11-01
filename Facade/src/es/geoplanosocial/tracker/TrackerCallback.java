package es.geoplanosocial.tracker;

import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Types;

import java.util.ArrayList;

/**
 * Callbacks triggered by tracker
 * Created by gbermejo on 14/05/17.
 */
public interface TrackerCallback {
    void newPlayers(ArrayList<Player> newPlayers);
    void changeWorld();
    void changeLevel(Types.Direction direction);

}
