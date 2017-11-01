package es.geoplanosocial.players;

import processing.core.PApplet;
import processing.core.PGraphics;

import java.awt.*;


/**
 * A square representation of a player.
 * Created by gbermejo on 15/05/17.
 */
public class Square extends VisiblePlayer {
    private float radii;

    public Square(int color, Player player) {
        super(color, player);
        this.radii = 0;
    }

    public float getRadii() {
        return radii;
    }

    public void setRadii(float radii) {
        this.radii = radii>getBoundingBox().width/2.0f?getBoundingBox().width/2.0f:radii;
    }

    public void setRadiiPercentage(float percentage) {
        setRadii(PApplet.lerp(0,getBoundingBox().width/2.0f,percentage));
    }

    @Override
    public void draw(PGraphics pg) {
        Rectangle bb = getBoundingBox();
        pg.beginDraw();
        pg.noStroke();
        pg.fill(getColor());
        pg.rect(bb.x, bb.y, bb.width, bb.width, radii);
        pg.endDraw();
    }
}
