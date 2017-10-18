package es.geoplanosocial.levels.world1.a;

import es.geoplanosocial.players.Node;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Utils;
import processing.core.PGraphics;

import java.awt.*;



/**
 * A circular representation of a player.
 * Created by josué on 22/05/17.
 */
public class Node1A extends Node {
    private Color color;
    private int a = 255;

    private int mouseX_prev, mouseY_prev;

    private static int INCREASE_RATE = 8;
    private static int DECREASE_RATE = 2;

    public Node1A(int color, Player player) {
        super(color, player);
        this.color = new Color(color);
    }

    @Override
    public void draw(PGraphics pg) {
        Rectangle bb = getBoundingBox();


        if ((mouseX_prev == bb.x) && (mouseY_prev == bb.y)) {
            // Utils.log(" hola " + stopCounter);
//            r -= DEGRADE_RATE;
//            g -= DEGRADE_RATE;
//            if (r < 0) r = 0;
//            if (g < 0) g = 0;
            a -= DECREASE_RATE;
            if (a < 0) a = 0;

        }
        else {
            // Utils.log("adios " + stopCounter);
//            r += DEGRADE_RATE;
//            g += DEGRADE_RATE;
//            if (r > 255) r = 255;
//            if (g > 255) g = 255;
            a += INCREASE_RATE;
            if (a > 255) a = 255;

        }

        // fixme change "Color.BLUE" and take this color directly from Level1A class
        // color = lerpColor(originalColor, Color.BLUE,  stopCounter, 1);




        mouseX_prev = bb.x;
        mouseY_prev = bb.y;

        pg.beginDraw();
        pg.noStroke();
        pg.fill(Utils.color(color.getRed(), color.getGreen(), color.getBlue(), a));
        pg.ellipse(bb.x, bb.y, bb.width, bb.height);
        pg.endDraw();
    }

}
