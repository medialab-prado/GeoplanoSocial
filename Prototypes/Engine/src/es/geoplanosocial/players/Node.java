package es.geoplanosocial.players;

import processing.core.PGraphics;

import java.awt.*;


/**
 * Created by gbermejo on 15/05/17.
 */
public class Node extends Player {
    int color;

    public Node(int color, Player player) {
        super(player.getId(), player.getBoundingBox());
        this.color=color;
    }

    @Override
    public void draw(PGraphics pg) {
        Rectangle bb = getBoundingBox();
        pg.beginDraw();
        pg.fill(color);
        pg.ellipse(bb.x, bb.y, bb.width, bb.height);
        pg.endDraw();
    }
}
