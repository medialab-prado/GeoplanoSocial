package es.geoplanosocial.util;

import processing.core.PApplet;
import processing.core.PGraphics;

import static processing.core.PApplet.map;

/**
 * Created by guzman on 11/12/2017.
 */
public class GlowLine {

    private static int ALPHA=127;
    private static float WIDTH = 3;


    private enum Direction {
        VERTICAL,
        HORIZONTAL

    }


    private static PApplet processing;
    private static PGraphics pg;
    private static int color;
    private static int colorAlpha;

    public static void setProcessing(PApplet processing) {
        GlowLine.processing = processing;
    }

    public static void setPg(PGraphics pg) {
        GlowLine.pg = pg;
    }

    public static void setColor(int color) {
        GlowLine.color = color;
        java.awt.Color c = new java.awt.Color(color);
        GlowLine.colorAlpha = Utils.color(c.getRed(), c.getGreen(), c.getBlue(), ALPHA);
    }

    public static void lineG(float x1, float y1, float x2, float y2, boolean isGlowing) {

        if(processing==null || pg==null) return;


        float w = Math.abs(x1-x2);
        float h = Math.abs(y1-y2);

        Direction direction;

        if(w==0 &&h==0){
            //println("Same point");
            return;
        }else if(w==0){
            //println("Vertical");
            direction = Direction.VERTICAL;
        }else if(h==0){
            //println("Horizontal");
            direction = Direction.HORIZONTAL;
        }else if(w>h){
            //println("Diagonal Horizontal");
            direction = Direction.HORIZONTAL;
        }else if(w<h){
            //println("Diagonal Vertical");
            direction = Direction.VERTICAL;
        }else{
            //println("Diagonal");
            direction = Direction.HORIZONTAL;
        }

        float halfH = direction == Direction.HORIZONTAL ? WIDTH:0;
        float halfW = direction == Direction.VERTICAL ? WIDTH:0;

        float incrH = direction == Direction.HORIZONTAL?1:0;
        float incrW = direction == Direction.VERTICAL?1:0;

        pg.beginDraw();
        pg.noFill();
        if(isGlowing) {
            for (int i = 0, j = 0; i < halfW || j < halfH; i += incrW, j += incrH) {
                float inter = map(Math.max(i, j), 0, Math.max(halfH, halfW), 1, 0);

                int c = processing.lerpColor(colorAlpha, color, inter);
                pg.stroke(c);
                pg.line(x1 + i, y1 + j, x2 + i, y2 + j);
                pg.line(x1 - i, y1 - j, x2 - i, y2 - j);
            }
        }else{
            pg.stroke(color);
            pg.strokeWeight(WIDTH/2.0f);//FIXME
            pg.line(x1, y1, x2, y2);
        }
        pg.endDraw();

    }
}
