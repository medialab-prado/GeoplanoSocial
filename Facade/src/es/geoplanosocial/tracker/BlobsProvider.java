package es.geoplanosocial.tracker;

/**
 * Provides rectangles with ids representing blobs.
 * Created by gbermejo on 14/05/17.
 */
public interface BlobsProvider {
    Blob[] fetchPositions();

}
