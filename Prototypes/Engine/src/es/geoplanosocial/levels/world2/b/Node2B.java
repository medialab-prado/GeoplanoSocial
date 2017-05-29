package es.geoplanosocial.levels.world2.b;

import es.geoplanosocial.players.Player;
import processing.core.PGraphics;

import java.awt.*;


/**
 * A circular representation of a player.
 * Created by josué on 22/05/17.
 */
public class Node2B extends Player {

    private int color;

    public Node2B(int color, Player player) {
        super(player.getId(), player.getBoundingBox());
        this.color = color;
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
