package es.geoplanosocial.players;

import processing.core.PGraphics;

import java.awt.*;


/**
 * A circular representation of a player.
 * Created by gbermejo on 15/05/17.
 */
public class Node extends Player {
    private int color;

    public Node(int color, Player player) {
        super(player.getId(), player.getBoundingBox());
        this.color=color;
    }

    public int getColor() {
        return color;
    }

    @Override
    public void draw(PGraphics pg) {
        Rectangle bb = getBoundingBox();
        pg.beginDraw();
        pg.noStroke();
        pg.fill(color);
        pg.ellipse(bb.x, bb.y, bb.width, bb.height);
        pg.endDraw();
    }
}
