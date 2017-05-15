package es.geoplanosocial.players;

import processing.core.PConstants;
import processing.core.PGraphics;

import java.awt.Rectangle;

/**
 * Abstract generic player
 * Created by gbermejo on 14/05/17.
 */
public abstract class Player {

    private String id;
    private Rectangle boundingBox;

    public Player(String id, Rectangle position) {
        setId(id);
        setBoundingBox(position);
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    private void setBoundingBox(Rectangle boundingBox) {
        this.boundingBox = boundingBox;
    }

    public void update(Rectangle current){
        setBoundingBox(current);
    }

    public PGraphics debug(PGraphics pg){
        pg.beginDraw();
        pg.textSize(5);
        pg.textAlign(PConstants.CENTER, PConstants.CENTER);
        pg.text(id, boundingBox.x, boundingBox.y);

        pg.endDraw();
        return pg;
    }

    public abstract void draw(PGraphics pg);

}
