package es.geoplanosocial.levels.world1.c;

import es.geoplanosocial.players.Node;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Utils;
import processing.core.PGraphics;

import java.awt.*;



/**
 * A circular representation of a player.
 * Created by josué on 22/05/17.
 */
public class Node1C extends Node {
    private int r = 255;
    private int g = 255;

    private int mouseX_prev, mouseY_prev;

    static private int DEGRADE_RATE = 5;

    public Node1C(int color, Player player) {
        super(color, player);
    }

    @Override
    public void draw(PGraphics pg) {
        Rectangle bb = getBoundingBox();


        if ((mouseX_prev == bb.x) && (mouseY_prev == bb.y)) {
            // Utils.log(" hola " + stopCounter);
            r -= DEGRADE_RATE;
            g -= DEGRADE_RATE;
            if (r < 0) r = 0;
            if (g < 0) g = 0;

        }
        else {
            // Utils.log("adios " + stopCounter);
            r += DEGRADE_RATE;
            g += DEGRADE_RATE;
            if (r > 255) r = 255;
            if (g > 255) g = 255;
        }

        // fixme change "Color.BLUE" and take this color directly from Level1C class
        // color = lerpColor(originalColor, Color.BLUE,  stopCounter, 1);




        mouseX_prev = bb.x;
        mouseY_prev = bb.y;

        pg.beginDraw();
        pg.fill(Utils.color(r, g, 255));
        pg.ellipse(bb.x, bb.y, bb.width, bb.height);
        pg.endDraw();
    }

}
