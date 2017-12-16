package es.geoplanosocial.util;

import processing.core.PApplet;
import processing.core.PGraphics;

import java.awt.*;
import java.awt.Color;

import static processing.core.PApplet.*;
import static processing.core.PConstants.RGB;

/**
 * Created by guzman on 11/12/2017.
 */
public class GlowEllipse {


    private static float glowExtension = 1.7f;


    private static PApplet processing;
    private static PGraphics pg;
    private static int color;

    public static void setProcessing(PApplet processing) {
        GlowEllipse.processing = processing;
    }

    public static void setPg(PGraphics pg) {
        GlowEllipse.pg = pg;
    }

    public static void setColor(int color) {
        GlowEllipse.color = color;
        java.awt.Color c = new java.awt.Color(color);
    }

    public static void ellipseG(int xCircle, int yCircle, int r, boolean isGlowing) {

        if(processing==null || pg==null) return;

        if(isGlowing) {

            PGraphics circlePG = processing.createGraphics(ceil(r * glowExtension), ceil(r * glowExtension));

            circlePG.beginDraw();
            circlePG.clear();
            circlePG.endDraw();

            int startColor = color;
            java.awt.Color cc = new Color(startColor);
            int endColor = Utils.color(cc.getRed(), cc.getGreen(), cc.getBlue(), 0);

            float centerX = circlePG.width / 2.0f;
            float centerY = circlePG.height / 2.0f;

            float innerRadius = r / 2.0f;
            float outerRadius = r * glowExtension / 2.0f;


            circlePG.loadPixels();
            for (int x = 0; x < circlePG.width; x++) {
                for (int y = 0; y < circlePG.height; y++) {
                    float d = dist(x, y, centerX, centerY);
                    int c = lerpColor(startColor, endColor, (d - innerRadius) / (outerRadius - innerRadius), RGB);

                    circlePG.pixels[x + y * circlePG.width] = c;
                }
            }
            circlePG.updatePixels();

            pg.beginDraw();
            pg.imageMode(CENTER);
            pg.image(circlePG, xCircle, yCircle);
            pg.endDraw();
        }else {
            pg.beginDraw();
            //pg.stroke(color);
            pg.noStroke();
            pg.fill(color);
            pg.ellipse(xCircle, yCircle, r, r);
            pg.endDraw();
        }
    }
}