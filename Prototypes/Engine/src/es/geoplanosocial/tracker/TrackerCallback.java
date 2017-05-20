package es.geoplanosocial.tracker;

import es.geoplanosocial.util.Types;

/**
 * Callbacks triggered by tracker
 * Created by gbermejo on 14/05/17.
 */
public interface TrackerCallback {
    void morePlayers();
    void lessPlayers();
    void changeLevel(Types.Direction direction);

}
