package es.geoplanosocial.players;

import es.geoplanosocial.util.Utils;
import processing.core.PGraphics;

import java.awt.*;



/**
 * A circular representation of a player.
 * Created by josué on 22/05/17.
 */
public class Node1C extends Player {
    private int color;
    private int r = 255;
    private int g = 255;

    private int mouseX_prev, mouseY_prev;

    static private int DEGRADE_RATE = 5;

    public Node1C(int color, Player player) {
        super(player.getId(), player.getBoundingBox());
        this.color=color;
    }

    @Override
    public void draw(PGraphics pg) {
        Rectangle bb = getBoundingBox();
        bb.width = 20;
        bb.height = 20;

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

        Utils.log(" hola " + r);



        mouseX_prev = bb.x;
        mouseY_prev = bb.y;

        pg.beginDraw();
        pg.fill(color(r, g, 255));
        pg.ellipse(bb.x, bb.y, bb.width, bb.height);
        pg.endDraw();
    }

    int color(int r, int g, int b) {
        if (r > 255) r = 255;
        else if (r < 0) r = 0;
        if (g > 255) g = 255;
        else if (g < 0) g = 0;
        if (b > 255) b = 255;
        else if (b < 0) b = 0;

        return 0xff000000 | (r << 16) | (g << 8) | b;
    }
}
