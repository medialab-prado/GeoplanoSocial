package es.geoplanosocial.tracker;

import java.util.ArrayList;

/**
 * Provides rectangles with ids representing blobs.
 * Created by gbermejo on 14/05/17.
 */
public interface BlobsProvider {
    Blob[] fetchPositions();

}
