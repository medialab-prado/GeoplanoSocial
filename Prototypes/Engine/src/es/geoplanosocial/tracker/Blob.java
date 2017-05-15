package es.geoplanosocial.tracker;

import java.awt.Rectangle;

/**
 * Represents an element detected by CV
 * Created by gbermejo on 14/05/17.
 */
public class Blob {
    private String id;
    private Rectangle boundingBox;

    public Blob(String id, Rectangle boundingBox) {
        this.id = id;
        this.boundingBox = boundingBox;
    }

    public String getId() {
        return id;
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }
}
