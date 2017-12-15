package es.geoplanosocial.players;


import es.geoplanosocial.util.*;
import processing.core.PApplet;
import processing.core.PGraphics;


import java.awt.*;
import java.awt.Color;

import static processing.core.PApplet.*;
import static processing.core.PConstants.CENTER;
import static processing.core.PConstants.RGB;

/**
 * Created by guzman on 23/11/2017.
 */
public class GlowNode extends Node{

    private static float glowExtension = 1.7f;

    private PGraphics ownPG;

    private PApplet processing;

    private boolean glowing = false;

    public GlowNode(int color, Player player, PApplet processing) {
        super(color, player);

        this.processing = processing;
        createGraphics();

    }

    private void createGraphics() {
        if(processing!=null) {
            Rectangle bb = getBoundingBox();

            ownPG = processing.createGraphics(ceil(bb.width * glowExtension), ceil(bb.height * glowExtension));

            ownPG.beginDraw();
            ownPG.clear();
            ownPG.endDraw();

            int startColor = getColor();
            Color cc = new Color(startColor);
            int endColor = Utils.color(cc.getRed(), cc.getGreen(), cc.getBlue(), 0);

            float centerX = ownPG.width / 2.0f;
            float centerY = ownPG.height / 2.0f;

            float innerRadius = bb.width / 2.0f;
            float outerRadius = bb.width * glowExtension / 2.0f;


            ownPG.loadPixels();
            for (int x = 0; x < ownPG.width; x++) {
                for (int y = 0; y < ownPG.height; y++) {
                    float d = dist(x, y, centerX, centerY);
                    int c = lerpColor(startColor, endColor, (d - innerRadius) / (outerRadius - innerRadius), RGB);

                    ownPG.pixels[x + y * ownPG.width] = c;
                }
            }
            ownPG.updatePixels();
        }

    }

    @Override
    public void draw(PGraphics pg) {
        if(glowing) {
            Rectangle bb = getBoundingBox();
            pg.beginDraw();
            pg.imageMode(CENTER);
            pg.image(ownPG, bb.x, bb.y);
            pg.endDraw();
        }else{
            super.draw(pg);
        }
    }

    @Override
    public void setBoundingBox(Rectangle boundingBox) {
        super.setBoundingBox(boundingBox);
        createGraphics();
    }

    public boolean isGlowing() {
        return glowing;
    }

    public void setGlowing(boolean glowing) {
        this.glowing = glowing;
    }
}
