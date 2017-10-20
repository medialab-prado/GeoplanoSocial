package es.geoplanosocial.players;

import processing.core.PApplet;
import processing.core.PGraphics;

import java.awt.*;


/**
 * A extended circular representation of a player.
 * Created by gbermejo on 20/10/17.
 */
public class ExtendedNode extends Node {

    private float fullness;

    private float alpha;

    public ExtendedNode(int color, Player player) {
        super(color, player);
        fullness = 1.0f;
        alpha =255.0f;
    }


    @Override
    public void draw(PGraphics pg) {
        Rectangle bb = getBoundingBox();
        pg.beginDraw();

        Color c = new Color(super.getColor());
        //Outside
        pg.noFill();
        pg.stroke(c.getRed(),c.getGreen(),c.getBlue(),255);
        pg.strokeWeight(2);
        pg.ellipse(bb.x, bb.y, bb.width, bb.height);

        //Inside
        pg.noStroke();
        pg.fill(c.getRed(),c.getGreen(),c.getBlue(), alpha);
        pg.ellipse(bb.x, bb.y, bb.width*fullness, bb.height*fullness);
        pg.endDraw();
    }


    public float getFullness() {
        return fullness;
    }

    public float getAlpha() {
        return alpha/255.0f;
    }

    public void setFullness(float fullness) {
        this.fullness = PApplet.constrain(fullness,0,1);
    }

    public void setAlpha(float alpha) {
        this.alpha = PApplet.lerp(0,255, alpha);
    }
}