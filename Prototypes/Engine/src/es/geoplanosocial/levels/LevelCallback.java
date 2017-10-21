package es.geoplanosocial.levels;

import es.geoplanosocial.util.Types;

/**
 * Created by guzman on 21/10/2017.
 */
public interface LevelCallback {
    void nextLevel();
    boolean getCurrentLevelCompletion();
}
