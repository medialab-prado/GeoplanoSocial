package es.geoplanosocial.games;

import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Color;
import es.geoplanosocial.util.Constants;
import es.geoplanosocial.util.Utils;

import java.awt.*;
import java.util.ArrayList;

/**
 * Default game layout
 * Created by guzman on 01/11/2017.
 */
public class Game1 extends Game {
    private static final String TITLE = "Maraña clásica";

    private static final int N_VERTEX = 5;
    private Point[] vertex;

    public static final int MAIN_COLOR = Color.W5_C_BG;

    private static final float STROKEWEIGHT_LEVEL4C = 2;
    private static final float INTERSECTIONS_SIZE_LEVEL4C = 5;
    private final int MAX_INTERVAL = 4000;//In milliseconds
    private final int MAX_INTERVAL_2 = 4000;//In milliseconds
    private long timer = System.currentTimeMillis();
    private long timer2 = System.currentTimeMillis();

    private int rounds;

    private int n_players = 0;

    private ArrayList<Point> intersectionPoints;
    private ArrayList<Integer> vertexWithIntersection;
    private int[] onVertex;
    private boolean[][] anclado;
    private int colorVariable;
    private int randomLinearVertex_index;
    private int[] potentialSolutionVertex;
    private int[] randomLinearVertex;

    protected Game1() {
        super(TITLE);
        anclado = new boolean[N_VERTEX][players.size()];
        for (int i = 0; i < N_VERTEX; i++)
            for (int j = 0; j < players.size(); j++)
                this.anclado[i][j] = false;
        randomLinearVertex_index = 0;
    }

    @Override
    protected ArrayList<Player> setupPlayers() {
        ArrayList<Player> players = new ArrayList<>();

        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W5_PINK_NODE, Game.players.get(0)));
//        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W5_BLUE_NODE, Game.players.get(1)));
//        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W5_YELLOW_NODE, Game.players.get(2)));
//        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W5_RED_NODE, Game.players.get(3)));
//        players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W5_GREEN_NODE, Game.players.get(4)));

        return players;
    }

    @Override
    protected void setup() {
        setDrawPlayersFront(true);
        marañaLineal();
        calcularPotentialSolutionVertex();
        potentialSolutionVertex = Utils.shuffleArray(potentialSolutionVertex);
        randomLinearVertex_index = 0;
        rounds = 0;
    }

    @Override
    public void update() {
        // intersection??
        calculateTotalIntersections();
        if (intersectionPoints.isEmpty()) {
            vertex[potentialSolutionVertex[randomLinearVertex_index]].setLocation(players.get(0).getLocation());
            colorVariable = Color.GREEN;
            if (System.currentTimeMillis() - timer >= MAX_INTERVAL) {
                marañaLineal();
                timer = System.currentTimeMillis();
                anclado[potentialSolutionVertex[randomLinearVertex_index]][0] = false;
                // rounds++;
            }
        } else {
            timer = System.currentTimeMillis();

            for (int j = 0; j < players.size(); j++) {
                if (!anclado[potentialSolutionVertex[randomLinearVertex_index]][j]) {
                    Point a = vertex[potentialSolutionVertex[randomLinearVertex_index]].getLocation();
                    Point x = players.get(j).getLocation();
                    if (a.distance(x) < 20) {
                        anclado[potentialSolutionVertex[randomLinearVertex_index]][j] = true;
                        // colorVariable = Color.GREY;
                    }
                } else { // if anclado
                    vertex[potentialSolutionVertex[randomLinearVertex_index]].setLocation(players.get(j).getLocation());
                    colorVariable = Color.GREY;

                    if (!partialIntersections(potentialSolutionVertex[randomLinearVertex_index])) {
                        if (System.currentTimeMillis() - timer2 >= MAX_INTERVAL_2) {
                            anclado[potentialSolutionVertex[randomLinearVertex_index]][j] = false;
                            randomLinearVertex_index++;
                            //calcularPotentialSolutionVertex();
                            timer2 = System.currentTimeMillis();
                        }
                    } else {
                        timer2 = System.currentTimeMillis();
                    }
                }
            }
        }
    }

    private void calcularPotentialSolutionVertex() {
        potentialSolutionVertex = new int[vertexWithIntersection.size()];
        for (int i = 0; i < vertexWithIntersection.size(); i++) {
            potentialSolutionVertex[i] = vertexWithIntersection.get(i);
        }
    }

    @Override
    protected void draw() {
        pg.beginDraw();
        // pintar líneas
        for (int i = 0; i < N_VERTEX - 1; i++) {
            pg.strokeWeight(STROKEWEIGHT_LEVEL4C);
            if ((randomLinearVertex[i] == potentialSolutionVertex[randomLinearVertex_index]) ||
                    (randomLinearVertex[i + 1] == potentialSolutionVertex[randomLinearVertex_index])) {
                pg.stroke(colorVariable);
            } else {
                pg.stroke(Color.WHITE);
            }
            pg.line((float) vertex[randomLinearVertex[i]].getLocation().getX(),
                    (float) vertex[randomLinearVertex[i]].getLocation().getY(),
                    (float) vertex[randomLinearVertex[i + 1]].getLocation().getX(),
                    (float) vertex[randomLinearVertex[i + 1]].getLocation().getY());

        }
        // pintar vertex
        for (int i = 0; i < N_VERTEX; i++) {
            pg.noStroke();
            pg.fill(Color.GREY);
            pg.ellipse((float) vertex[randomLinearVertex[i]].getLocation().getX(),
                    (float) vertex[randomLinearVertex[i]].getLocation().getY(),
                    Constants.VERTEX_NORMAL_RADIO,
                    Constants.VERTEX_NORMAL_RADIO);

        }
        pg.noStroke();
        pg.fill(0, 0, 255);

        for (int i = 0; i < intersectionPoints.size(); i++) {
            pg.ellipse(intersectionPoints.get(i).x, intersectionPoints.get(i).y, INTERSECTIONS_SIZE_LEVEL4C, INTERSECTIONS_SIZE_LEVEL4C);
        }
        pg.noStroke();
        pg.fill(Color.VERTEXT_SELECTED);
        for (int i = 0; i < players.size(); i++) {
            pg.ellipse(vertex[potentialSolutionVertex[randomLinearVertex_index]].x, vertex[potentialSolutionVertex[randomLinearVertex_index]].y, Constants.VERTEX_SELECTED_RADIO, Constants.VERTEX_SELECTED_RADIO);
        }
        pg.endDraw();
    }


    private boolean enmarañados() {
        return true;
    }


    private void marañaLineal() {
        colorVariable = Color.WHITE;
        do {
            vertex = randomVertex();
            randomLinearVertex = Utils.shuffleArray(new int[]{0, 1, 2, 3, 4});
            calculateTotalIntersections();
        } while (intersectionPoints.isEmpty());
    }

    // todo create constraints to make the random points appear (1) quite separately and (2) not in-line
    private Point[] randomVertex() {
        Point[] vAux = new Point[N_VERTEX];
        if (true) {
            vAux[0] = new Point(30, 50);
            vAux[1] = new Point(96, 30);
            vAux[2] = new Point(162, 40);
            vAux[3] = new Point(150, 80);
            vAux[4] = new Point(50, 100);
        } else {
            for (int i = 0; i < this.N_VERTEX; i++) {
                vAux[i] = new Point();
                vAux[i].x = Utils.randomInt(0, Constants.LEVEL_WIDTH);
                vAux[i].y = Utils.randomInt(0, Constants.LEVEL_HEIGHT);
            }
        }
        return vAux;
    }

    private boolean partialIntersections(int index) {
        for (Integer i : vertexWithIntersection)
            if (i.intValue() == index) return true;
        return false;
    }

    private void calculateTotalIntersections() {

        intersectionPoints = new ArrayList<>();
        vertexWithIntersection = new ArrayList<>();

        for (int i = 0; i < N_VERTEX - 1; i++) {
            for (int j = i + 2; j < N_VERTEX - 1; j++) {
                // linea a comprar intersección 1/2
                float x1 = (float) vertex[randomLinearVertex[i]].getLocation().getX();
                float y1 = (float) vertex[randomLinearVertex[i]].getLocation().getY();
                float x2 = (float) vertex[randomLinearVertex[i + 1]].getLocation().getX();
                float y2 = (float) vertex[randomLinearVertex[i + 1]].getLocation().getY();

                // linea a comprar intersección 2/2
                float x3 = (float) vertex[randomLinearVertex[j]].getLocation().getX();
                float y3 = (float) vertex[randomLinearVertex[j]].getLocation().getY();
                float x4 = (float) vertex[randomLinearVertex[j + 1]].getLocation().getX();
                float y4 = (float) vertex[randomLinearVertex[j + 1]].getLocation().getY();

                Point pAux = intersect(x1, y1, x2, y2, x3, y3, x4, y4);

                if (pAux != null) {
                    intersectionPoints.add(pAux);
                    vertexWithIntersection.add(new Integer(randomLinearVertex[i]));
                    vertexWithIntersection.add(new Integer(randomLinearVertex[i + 1]));
                    vertexWithIntersection.add(new Integer(randomLinearVertex[j]));
                    vertexWithIntersection.add(new Integer(randomLinearVertex[j + 1]));
                }
            }
        }
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
