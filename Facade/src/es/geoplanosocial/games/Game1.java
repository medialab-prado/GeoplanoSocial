package es.geoplanosocial.games;

import es.geoplanosocial.players.Node;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Color;
import es.geoplanosocial.util.Constants;
import es.geoplanosocial.util.Utils;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static processing.core.PApplet.dist;
import static processing.core.PApplet.floor;

/**
 * Default game layout
 * Created by guzman on 01/11/2017.
 */
public class Game1 extends Game {
    public static final int MAIN_COLOR = Color.W5_C_BG;
    private static final String TITLE = "Maraña clásica";
    private static final float MIN_DIST_VERTEX = 30;
    private static final double MIN_DIST_INLINE = 10;
    private static int n_vertex;
    private static final float STROKEWEIGHT_LEVEL4C = 2;
    private static final float INTERSECTIONS_SIZE_LEVEL4C = 5;
    private static final int FACTOR_AUMENTO_DIFICULTAD = 2;
    private final int MAX_INTERVAL = 4000;//In milliseconds
    private final int MAX_INTERVAL_2 = 4000;//In milliseconds
    private Point[] vertex;
    private long timer = System.currentTimeMillis();
    private long[] timer2;
    private final int MIN_VERTEX = 4;
    private final int MAX_VERTEX = 7;

    private ArrayList<Point> intersectionPoints;
    private ArrayList<Integer> vertexWithIntersection;
    private int[] onVertex;
    private boolean[][] anclado;
    private int colorVariable;
    private int[] potentialSolutionVertex;
    private int[] randomLinearVertex;
    private int n_players_local;

    private boolean nSelectedVertexEqualToPlayers = true;
    private int nSelectedVertex;
    private boolean kk;

    protected Game1() {
        super(TITLE);
    }

    @Override
    protected ArrayList<Player> setupPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        for (Player p : Game.players) {
            Node pl = (Node) Player.Factory.getPlayer(Player.Type.NODE, Color.W5_PINK_NODE, p);

            players.add(pl);
        }

        n_players_local = Game.players.size();

        return players;
    }

    @Override
    protected void setup() {

        setDrawPlayersFront(true);

        calcular_n_vertex(getCurrentLevel());


        anclado = new boolean[n_vertex][n_players_local];
        for (int i = 0; i < n_vertex; i++)
            for (int j = 0; j < n_players_local; j++)
                this.anclado[i][j] = false;

        setDrawPlayersFront(true);
        marañaLineal();
        calcularPotentialSolutionVertex();
        Utils.log("x " + Arrays.toString(potentialSolutionVertex));
        Utils.log("y " + kk);
        potentialSolutionVertex = Utils.shuffleArray(potentialSolutionVertex);
        // randomLinearVertex_index = 0;
        timer2 = new long[n_players_local];
        for (int i = 0; i < n_players_local; i++) timer2[i] = System.currentTimeMillis();
    }

    private void calcular_n_vertex(int n_level) {
        Utils.log("LEVEL # -------------> " + n_level);
        n_vertex = MIN_VERTEX + floor(n_level / FACTOR_AUMENTO_DIFICULTAD);
        if (n_vertex > MAX_VERTEX) n_vertex = MAX_VERTEX;
        Utils.log("VERTEX # ------------> " + n_vertex);
    }

    @Override
    public void update() {

        if (nSelectedVertexEqualToPlayers) nSelectedVertex = n_players_local;

        // intersection??
        calculateTotalIntersections();
        if (intersectionPoints.isEmpty()) {
            // get anclados
            // boolean[] playersAnclados = getPlayersAnclados();
            for (int i = 0; i < anclado.length; i++)
                for (int j = 0; j < anclado[0].length; j++)
                    if (anclado[i][j]) {
                        // vertex[potentialSolutionVertex[aux]].setLocation(players.get(j).getLocation());
                        vertex[i].setLocation(players.get(j).getLocation());
                    }
            colorVariable = Color.GREEN;
            if (System.currentTimeMillis() - timer >= MAX_INTERVAL) {
                marañaLineal();
                timer = System.currentTimeMillis();
                desanclarTodo();
                nextLevel();
                // rounds++;
            }
        } else {
            timer = System.currentTimeMillis();

            for (int j = 0; j < n_players_local; j++) {
                //
                // si el jugador que estoy evaluando (en el for)
                // no está anclado a ningún nodo -->
                // compruebo si pasa cerca de alguno para anclarlo
                //
                if (!playerAncladoAalgo(j)) {
                    Point x = players.get(j).getLocation();
                    for (int i = 0; i < nSelectedVertex; i++) {
                        Point a = vertex[potentialSolutionVertex[i]].getLocation();
                        if ((!vertexAncladoAalguien(potentialSolutionVertex[i])) && (a.distance(x) < 20)) {
                            anclado[potentialSolutionVertex[i]][j] = true;
                            // colorVariable = Color.GREY;
                            break;
                        }
                    }
                //
                // si el jugador que estoy evaluando (en el for)
                // está anclado a un nodo -->
                // compruebo si semi-resuelve la maraña
                //
                } else { // if anclado
                    vertex[ancladoAvertex(j)].setLocation(players.get(j).getLocation());
                    colorVariable = Color.GREY;

                    if (!partialIntersections(ancladoAvertex(j))) {
                        if (System.currentTimeMillis() - timer2[j] >= MAX_INTERVAL_2) {
                            if (nSelectedVertex == 1) {
                                calcularPotentialSolutionVertex();
                                potentialSolutionVertex = Utils.shuffleArray(potentialSolutionVertex);
                            }
                            else {
                                actualizarPotentialSolutionVertex(j);
                            }
                            anclado[ancladoAvertex(j)][j] = false;
                            timer2[j] = System.currentTimeMillis();
                        }
                    } else {
                        timer2[j] = System.currentTimeMillis();
                    }

                }
            }
        }
    }

    private boolean playerAncladoAalgo(int player) {
        for (int i = 0; i < anclado.length; i++)
            if (anclado[i][player]) return true;
        return false;
    }

    private boolean vertexAncladoAalguien(int vertex) {
        for (int i = 0; i < anclado[0].length; i++)
            if (anclado[vertex][i]) return true;
        return false;
    }

    private int ancladoAvertex(int player) {
        for (int i = 0; i < anclado.length; i++)
            if (anclado[i][player]) return i;
        return -1;
    }


    private boolean[] KK_getPlayersAnclados() {
        boolean[] aux = new boolean[n_players_local];

        for (int i = 0; i < anclado[0].length; i++)
            aux[i] = false;

        for (int i = 0; i < anclado.length; i++)
            for (int j = 0; j < anclado[0].length; j++)
                aux[j] = aux[j] || anclado[i][j];

        return aux;
    }

    private void desanclarTodo() {
        for (int i = 0; i < anclado.length; i++)
            for (int j = 0; j < anclado[0].length; j++)
                anclado[i][j] = false;
    }

    private void calcularPotentialSolutionVertex() {
        potentialSolutionVertex = new int[vertexWithIntersection.size()];
        for (int i = 0; i < vertexWithIntersection.size(); i++) {
            potentialSolutionVertex[i] = vertexWithIntersection.get(i);
        }
    }

    private void actualizarPotentialSolutionVertex(int playerLastMovement) {
        // de potentialSolutionVertex, quito el vertex que se ha movido y me quedo con el resto (de los que estaban ON)
        int[] aux = new int[nSelectedVertex - 1];
        int auxCount = 0;
        for (int i = 0; i < nSelectedVertex; i++) {
            if (potentialSolutionVertex[i] != ancladoAvertex(playerLastMovement)) {
                aux[auxCount] = potentialSolutionVertex[i];
                auxCount++;
            }
        }

        boolean[] indexToRemove = new boolean[vertexWithIntersection.size()];
        for (int i = 0; i < vertexWithIntersection.size(); i++) {
            indexToRemove[i] = false;
            for (int j = 0; j < nSelectedVertex - 1; j++) {
                if (vertexWithIntersection.get(i) == aux[j]) {
                    indexToRemove[i] = true;
                    break;
                }
            }
        }

        potentialSolutionVertex = new int[vertexWithIntersection.size()];
        for (int i = 0; i < nSelectedVertex - 1; i++) {
            potentialSolutionVertex[i] = aux[i];
        }

        // randomice???

        int auxIndex = nSelectedVertex - 1;
        for (int i = 0; i < indexToRemove.length; i++) {
                if (!indexToRemove[i]) {
                    potentialSolutionVertex[auxIndex] = vertexWithIntersection.get(i);
                    auxIndex++;
                }
            }


    }

    @Override
    protected void draw() {
        pg.beginDraw();
        //
        // pintar líneas
        //
        for (int i = 0; i < n_vertex - 1; i++) {
            pg.strokeWeight(STROKEWEIGHT_LEVEL4C);
            //
            // pintar líneas XXXX
            //
            if ((randomLinearVertex[i] == potentialSolutionVertex[0]) ||
                    (randomLinearVertex[i + 1] == potentialSolutionVertex[0])) {
                pg.stroke(colorVariable);
            //
            // pintar líneas cualquiera
            //
            } else {
                pg.stroke(Color.WHITE);
            }
            pg.line((float) vertex[randomLinearVertex[i]].getLocation().getX(),
                    (float) vertex[randomLinearVertex[i]].getLocation().getY(),
                    (float) vertex[randomLinearVertex[i + 1]].getLocation().getX(),
                    (float) vertex[randomLinearVertex[i + 1]].getLocation().getY());
        }
        //
        // pintar vertex
        //
        for (int i = 0; i < n_vertex; i++) {
            pg.noStroke();
            pg.fill(Color.GREY);
            pg.ellipse((float) vertex[randomLinearVertex[i]].getLocation().getX(),
                    (float) vertex[randomLinearVertex[i]].getLocation().getY(),
                    Constants.VERTEX_NORMAL_RADIO,
                    Constants.VERTEX_NORMAL_RADIO);

        }
        pg.noStroke();
        pg.fill(0, 0, 255);

        //
        // pintar intersection points
        //
        for (int i = 0; i < intersectionPoints.size(); i++) {
            pg.ellipse(intersectionPoints.get(i).x, intersectionPoints.get(i).y, INTERSECTIONS_SIZE_LEVEL4C, INTERSECTIONS_SIZE_LEVEL4C);
        }

        //
        // pintar vertex_ON
        //
        pg.noStroke();
        pg.fill(Color.VERTEXT_SELECTED);
        for (int i = 0; i < nSelectedVertex; i++) {
            pg.ellipse(vertex[potentialSolutionVertex[i]].x, vertex[potentialSolutionVertex[i]].y, Constants.VERTEX_SELECTED_RADIO, Constants.VERTEX_SELECTED_RADIO);
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
            randomLinearVertex = new int[n_vertex];
            for (int i = 1; i < n_vertex; i++)
                randomLinearVertex[i] = i;
            randomLinearVertex = Utils.shuffleArray(randomLinearVertex);
            calculateTotalIntersections();
        } while ((intersectionPoints.isEmpty())  ||
                (!checkNoInLine(vertex)) ||
                (!checkMinDist(vertex)));
    }

    private boolean checkMinDist(Point[] auxPointsArray) {
        for (int i = 0; i < n_vertex; i++) {
            for (int j = 0; j < n_vertex; j++) {
                if (i != j) {
                    // Utils.log(String.valueOf(dist(auxPointsArray.get(i).x, auxPointsArray.get(i).y, auxPointsArray.get(j).x, auxPointsArray.get(j).y)));
                    if (dist(auxPointsArray[i].x, auxPointsArray[i].y, auxPointsArray[j].x, auxPointsArray[j].y) < MIN_DIST_VERTEX) {
                        // Utils.log("sinDONE");
                        return false;
                    }
                }
            }
        }
        // Utils.log("conDONE");
        return true;
    }

    private boolean checkNoInLine(Point[] shapeVertex) {
        // i recorre lineas
        for (int i = 0; i < n_vertex - 1; i++) {
            // j recorre puntos
            for (int j = 0; j < n_vertex; j++) {
                if ((i != j) && (j != i+1)) {
                    // Utils.log(String.valueOf(dist(auxPointsArray.get(i).x, auxPointsArray.get(i).y, auxPointsArray.get(j).x, auxPointsArray.get(j).y)));
                    if (distanceFromPointToLine(new Point(shapeVertex[randomLinearVertex[j]].x, shapeVertex[randomLinearVertex[j]].y), new Line2D.Double(shapeVertex[randomLinearVertex[i]].x, shapeVertex[randomLinearVertex[i]].y, shapeVertex[randomLinearVertex[i+1]].x, shapeVertex[randomLinearVertex[i+1]].y)) < MIN_DIST_INLINE) {
                        // Utils.log("sinDONE");
                        return false;
                    }
                }
            }
        }
        // Utils.log("conDONE");
        return true;
    }

    // adapted from http://www.java2s.com/Code/CSharp/Development-Class/DistanceFromPointToLine.htm
    public static double distanceFromPointToLine(Point point, Line2D line) {
        // given a line based on two points, and a point away from the line,
        // find the perpendicular distance from the point to the line.
        // see http://mathworld.wolfram.com/Point-LineDistance2-Dimensional.html
        // for explanation and defination.
        Point2D l1 = line.getP1();
        Point2D l2 = line.getP2();

        return abs((l2.getX() - l1.getX()) * (l1.getY() - point.getY()) - (l1.getX() - point.getX()) * (l2.getY() - l1.getY())) /
                sqrt(pow(l2.getX() - l1.getX(), 2) + pow(l2.getY() - l1.getY(), 2));
    }

    // todo create constraints to make the random points appear (1) quite separately and (2) not in-line
    private Point[] randomVertex() {
        Point[] vAux = new Point[n_vertex];
        if (false) {
            vAux[0] = new Point(30, 50);
            vAux[1] = new Point(96, 30);
            vAux[2] = new Point(162, 40);
            vAux[3] = new Point(150, 80);
            vAux[4] = new Point(50, 100);
        } else {
            for (int i = 0; i < this.n_vertex; i++) {
                vAux[i] = new Point();
                vAux[i].x = Utils.randomInt(Constants.PLAY_AREA_OFFSET_X, Constants.LEVEL_WIDTH - Constants.PLAY_AREA_OFFSET_X);
                vAux[i].y = Utils.randomInt(Constants.PLAY_AREA_OFFSET_Y, Constants.LEVEL_HEIGHT - Constants.PLAY_AREA_OFFSET_Y);
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

        for (int i = 0; i < n_vertex - 1; i++) {
            for (int j = i + 2; j < n_vertex - 1; j++) {
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

                boolean addPoint = true;

                if ((pAux != null)) {
                    intersectionPoints.add(pAux);
                    if (!vertexWithIntersection.contains(randomLinearVertex[i]))
                        vertexWithIntersection.add(randomLinearVertex[i]);
                    if (!vertexWithIntersection.contains(randomLinearVertex[i + 1]))
                        vertexWithIntersection.add(randomLinearVertex[i + 1]);
                    if (!vertexWithIntersection.contains(randomLinearVertex[j]))
                        vertexWithIntersection.add(randomLinearVertex[j]);
                    if (!vertexWithIntersection.contains(randomLinearVertex[j + 1]))
                        vertexWithIntersection.add(randomLinearVertex[j + 1]);
//                        vertexWithIntersection.add(randomLinearVertex[i]);
//                        vertexWithIntersection.add(randomLinearVertex[i + 1]);
//                        vertexWithIntersection.add(randomLinearVertex[j]);
//                        vertexWithIntersection.add(randomLinearVertex[j + 1]);
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
