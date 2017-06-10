package es.geoplanosocial.levels.world5.c;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Color;
import es.geoplanosocial.util.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * World 4
 * Level C
 * Created by gbermejo on 27/05/17.
 */
public class Level5C extends Level {

    private static final String TITLE = "Desemaraña-cíclico";
    private static final int MAIN_COLOR = Color.W5_C_BG;

    private static final float STROKEWEIGHT_LEVEL4C = 2;
    private static final float INTERSECTIONS_SIZE_LEVEL4C = 5;
    private final int MAX_INTERVAL = 3000;//In milliseconds
    private long timer = System.currentTimeMillis();
    private int[] randomLinearPlayers;

    private ArrayList<Point> intersectionPoints;

    public Level5C() {
        super(TITLE, MAIN_COLOR);
    }


    @Override
    protected void setupLevel() {
        marañaLineal();
    }


    @Override
    protected ArrayList<Player> setupPlayers() {

        ArrayList<Player> players = new ArrayList<>();

        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W5_PINK_NODE, Level.players.get(0)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W5_BLUE_NODE, Level.players.get(1)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W5_YELLOW_NODE, Level.players.get(2)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W5_RED_NODE, Level.players.get(3)));
        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W5_GREEN_NODE, Level.players.get(4)));

        return players;
    }


    @Override
    public void update() {

        intersectionPoints = calculateIntersections();
        if (intersectionPoints.isEmpty()) {
            if (System.currentTimeMillis() - timer >= MAX_INTERVAL) {
                marañaLineal();
                timer = System.currentTimeMillis();
            }
        }

    }




    @Override
    protected void drawLevel() {
        pg.beginDraw();
        pg.strokeWeight(STROKEWEIGHT_LEVEL4C);
        pg.stroke(Color.WHITE);
        for (int i = 0; i < randomLinearPlayers.length - 1; i++) {
            pg.line((float) players.get(randomLinearPlayers[i]).getLocation().getX(),
                    (float) players.get(randomLinearPlayers[i]).getLocation().getY(),
                    (float) players.get(randomLinearPlayers[i + 1]).getLocation().getX(),
                    (float) players.get(randomLinearPlayers[i + 1]).getLocation().getY());
        }
        pg.noStroke();
        pg.fill(0, 0, 255);
        // Utils.log("adios " + x + " - " + y);
        for (int i = 0; i < intersectionPoints.size(); i++) {
            pg.ellipse(intersectionPoints.get(i).x, intersectionPoints.get(i).y, INTERSECTIONS_SIZE_LEVEL4C, INTERSECTIONS_SIZE_LEVEL4C);

        }
        pg.endDraw();
    }

    private boolean enmarañados() {
        return true;
    }


    private void marañaLineal() {
        do {
            randomLinearPlayers = Utils.shuffleArray(new int[]{0, 1, 2, 3, 4});
            calculateIntersections();
        } while (intersectionPoints.isEmpty());
    }

    private ArrayList<Point> calculateIntersections() {

        intersectionPoints = new ArrayList<>();

        for (int i = 0; i < randomLinearPlayers.length - 1; i++) {
            for (int j = i + 2; j < randomLinearPlayers.length - 1; j++) {
                // linea a comprar intersección 1/2
                float x1 = (float) players.get(randomLinearPlayers[i]).getLocation().getX();
                float y1 = (float) players.get(randomLinearPlayers[i]).getLocation().getY();
                float x2 = (float) players.get(randomLinearPlayers[i + 1]).getLocation().getX();
                float y2 = (float) players.get(randomLinearPlayers[i + 1]).getLocation().getY();

                // linea a comprar intersección 2/2
                float x3 = (float) players.get(randomLinearPlayers[j]).getLocation().getX();
                float y3 = (float) players.get(randomLinearPlayers[j]).getLocation().getY();
                float x4 = (float) players.get(randomLinearPlayers[j + 1]).getLocation().getX();
                float y4 = (float) players.get(randomLinearPlayers[j + 1]).getLocation().getY();

                Utils.log("hola " + Arrays.toString(randomLinearPlayers));

                Point pAux = intersect(x1, y1, x2, y2, x3, y3, x4, y4);

                if (pAux != null) {
                    intersectionPoints.add(pAux);
                    Utils.log("intersección " + Arrays.toString(randomLinearPlayers));
                }
            }
        }
        return intersectionPoints;
    }

    // from http://processingjs.org/learning/custom/intersect/
    private Point intersect(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {

        float a1, a2, b1, b2, c1, c2;
        float r1, r2, r3, r4;
        float denom, offset, num;
        float x, y;

        // Compute a1, b1, c1, where line joining points 1 and 2
        // is "a1 x + b1 y + c1 = 0".
        a1 = y2 - y1;
        b1 = x1 - x2;
        c1 = (x2 * y1) - (x1 * y2);

        // Compute r3 and r4.
        r3 = ((a1 * x3) + (b1 * y3) + c1);
        r4 = ((a1 * x4) + (b1 * y4) + c1);

        // Check signs of r3 and r4. If both point 3 and point 4 lie on
        // same side of line 1, the line segments do not intersect.
        if ((r3 != 0) && (r4 != 0) && same_sign(r3, r4)) {
            return null;
        }

        // Compute a2, b2, c2
        a2 = y4 - y3;
        b2 = x3 - x4;
        c2 = (x4 * y3) - (x3 * y4);

        // Compute r1 and r2
        r1 = (a2 * x1) + (b2 * y1) + c2;
        r2 = (a2 * x2) + (b2 * y2) + c2;

        // Check signs of r1 and r2. If both point 1 and point 2 lie
        // on same side of second line segment, the line segments do
        // not intersect.
        if ((r1 != 0) && (r2 != 0) && (same_sign(r1, r2))) {
            return null;
        }

        //Line segments intersect: compute intersection point.
        denom = (a1 * b2) - (a2 * b1);

        if (denom == 0) {
            return null;
        }

        if (denom < 0) {
            offset = -denom / 2;
        } else {
            offset = denom / 2;
        }

        // The denom/2 is to get rounding instead of truncating. It
        // is added or subtracted to the numerator, depending upon the
        // sign of the numerator.
        num = (b1 * c2) - (b2 * c1);
        if (num < 0) {
            x = (num - offset) / denom;
        } else {
            x = (num + offset) / denom;
        }

        num = (a2 * c1) - (a1 * c2);
        if (num < 0) {
            y = (num - offset) / denom;
        } else {
            y = (num + offset) / denom;
        }

        // lines_intersect
        return new Point((int) x, (int) y);
    }

    private boolean same_sign(float a, float b) {

        return ((a * b) >= 0);
    }

}
