package es.geoplanosocial.players;

import processing.core.PGraphics;

import java.awt.*;


/**
 * A circular representation of a player.
 * Created by gbermejo on 15/05/17.
 */
public class Node extends VisiblePlayer {

    public Node(int color, Player player) {
        super(color, player);
    }


    @Override
    public void draw(PGraphics pg) {
        Rectangle bb = getBoundingBox();
        pg.beginDraw();
        pg.noStroke();
        pg.fill(super.getColor());
        pg.ellipse(bb.x, bb.y, bb.width, bb.height);
        pg.endDraw();
    }
}
