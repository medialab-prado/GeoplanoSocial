package es.geoplanosocial.util;

import es.geoplanosocial.levels.Level;
import processing.core.PApplet;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Set;
import java.util.HashSet;

import static es.geoplanosocial.util.Color.DARK_GREY;
import static es.geoplanosocial.util.Color.GREY;
import static es.geoplanosocial.util.Color.LIGHT_GREY;


/**
 * Utility methods
 * Created by gbermejo on 19/04/17.
 */
public class Utils {

    public static int color(int r, int g, int b) {
        return color(r, g, b, 255);
    }

    public static int color(int r, int g, int b, int a) {
        if (r > 255) r = 255;
        else if (r < 0) r = 0;
        if (g > 255) g = 255;
        else if (g < 0) g = 0;
        if (b > 255) b = 255;
        else if (b < 0) b = 0;
        if (a > 255) a = 255;
        else if (a < 0) a = 0;

        return 0x00000000 | (a << 24) | (r << 16) | (g << 8) | b;
    }

    private static SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");


    public static void log(String text) {
        if (Constants.DEBUG)
            System.out.println("[" + formatter.format(System.currentTimeMillis()) + "]" + text);
    }


    public static int[] getWorldColors(int players) {
        int[] worldColors = new int[Level.Type.values().length];

        int index = 0;
        for (Level.Type level : Level.Type.values()) {

            Class l = Level.Factory.getLevelClass(players, level);
            try {
                worldColors[index++] = l.getDeclaredField("MAIN_COLOR").getInt(null);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
                worldColors[index++] = Color.MAGENTA;
            }
        }

        if(players==0){
            worldColors = new int[]{LIGHT_GREY, GREY, DARK_GREY};
        }

        return worldColors;
    }

    private static Random random = new Random();

    public static int randomInt(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    // Implementing Fisher–Yates shuffle
    public static int[] shuffleArray(int[] ar) {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
        return ar;
    }

    public static boolean distinctIntArrayValues(int[] arr){
        Set<Integer> foundNumbers = new HashSet<Integer>();
        for (int num : arr) {
            if(foundNumbers.contains(num)){
                return false;
            }
            foundNumbers.add(num);
        }
        return true;
    }

    // from http://mindprod.com/jgloss/polar.html
    public static double[] cartesian2polar(Point p, Point centroid) {
        double[] x = new double[2];
        Point auxP = new Point(p.x - centroid.x, p.y - centroid.y);

        // Cartesian to polar
        // double radius = Math.sqrt( auxP.x * auxP.x + auxP.y * auxP.y );
        // double angleInRadians = Math.acos( auxP.x / radius );

        double radius     = Math.sqrt(auxP.x * auxP.x + auxP.y * auxP.y);
        double angleInRadians = Math.atan2(auxP.y, auxP.x);

        x[0] = radius;
        x[1] = angleInRadians;

        return x;
    }

    // from http://mindprod.com/jgloss/polar.html
    public static Point polar2cartesian(double[] x) {
        Point p = new Point();

        // polar to Cartesian
        p.setLocation(Math.cos( x[2] ) * x[1], Math.sin( x[2] ) * x[1]);

        return p;
    }

    public static boolean isCircleCollision(Point c1, float r1, Point c2, float r2){
        float distance = PApplet.dist(c1.x, c1.y, c2.x, c2.y);
        if (distance <= r1+r2) {//Collision between nodes
            return true;
        }else {
            return false;
        }
    }

    public static boolean isRunningFromJar(){
        String className = Utils.class.getName().replace('.', '/');
        String classJar = Utils.class.getResource("/" + className + ".class").toString();
        return classJar.startsWith("jar:");
    }

    public static boolean circleLineIntersect(float x1, float y1, float x2, float y2, float cx, float cy, float cr ) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        float a = dx * dx + dy * dy;
        float b = 2 * (dx * (x1 - cx) + dy * (y1 - cy));
        float c = cx * cx + cy * cy;
        c += x1 * x1 + y1 * y1;
        c -= 2 * (cx * x1 + cy * y1);
        c -= cr * cr;
        float bb4ac = b * b - 4 * a * c;
        return (bb4ac>=0);
    }

    public static String getResourcePath(String resource){
        File root = new File(".");
        File resources = new File(root, "resources");
        File res = new File(resources, resource);
        return res.getPath();
        //URL url = Utils.class.getClassLoader().getResource(resource);
        //return url.getPath();
    }
}
