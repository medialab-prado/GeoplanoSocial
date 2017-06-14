package es.geoplanosocial.util;

import es.geoplanosocial.players.Player;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static es.geoplanosocial.util.Constants.*;
import static java.lang.Math.abs;
import static processing.core.PApplet.dist;
import static processing.core.PConstants.MAX_INT;

/**
 * Random shape to imitate methods
 * Created by josué on 04/08/17.
 */
public class RandomShape {

    private ArrayList<Point> shapeVertex;
    private int vertexNumber;

    private float DIST_TOLERANCE = 10;
    private float MIN_SHAPE_AREA = 500;
    private float MIN_DIST_VERTEX = 20;

    public RandomShape(int vertexNumber) {
        shapeVertex = new ArrayList<>();
        this.vertexNumber = vertexNumber;
        updateRamdomShape();
    }

    // TODO establecer una distancia mínima entre 2 puntos
    // TODO (para >4) no controlo que la forma sea "continua" (mirar)
    public void updateRamdomShape() {
        shapeVertex.clear();

        do {
            shapeVertex.clear();
            for (int i = 0; i < vertexNumber; i++) {
                shapeVertex.add(new Point(Utils.randomInt(1, Constants.LEVEL_HEIGHT), Utils.randomInt(1, Constants.LEVEL_HEIGHT)));
            }
        } while ((polygonArea(shapeVertex, vertexNumber) < MIN_SHAPE_AREA) ||
                (!checkMinDist(shapeVertex)));
                // todo checkNoInLine(shapeVertex)

        // shapeVertex = sortToGetClosedShape();
    }

    private boolean checkMinDist(ArrayList<Point> auxPointsArray) {
        for(int i = 0; i < vertexNumber; i++) {
            for(int j = 0; j < vertexNumber; j++) {
                if (i != j) {
                    Utils.log(String.valueOf(dist(auxPointsArray.get(i).x, auxPointsArray.get(i).y, auxPointsArray.get(j).x, auxPointsArray.get(j).y)));
                    if (dist(auxPointsArray.get(i).x, auxPointsArray.get(i).y, auxPointsArray.get(j).x, auxPointsArray.get(j).y) < MIN_DIST_VERTEX) {
                        Utils.log("sinDONE");
                        return false;
                    }
                }
            }
        }
        Utils.log("conDONE");
        return true;
    }

    private ArrayList<Point> sortToGetClosedShape() {
        ArrayList<Point> auxShapeVertex = new ArrayList<>();;
        double[] r = new double[vertexNumber];

        for (int i = 0; i < vertexNumber; i++) {
            r[i] = Utils.cartesian2polar(shapeVertex.get(i))[1];
        }


        //sort the array intio a new array
        double[] y  = r;
        Arrays.sort(y); //sort ascending

        //final array of indexes
        int index_array[] = new int[7];

        //iteretate on x arrat
        for(int i=0; i<vertexNumber; i++) {
            //search the position of a value of the original x array into the sorted y array, store the position in the index array
            index_array[i] = Arrays.binarySearch(r, y[i]);

            auxShapeVertex.add(new Point(shapeVertex.get(index_array[i]).x, shapeVertex.get(index_array[i]).x));
        }
        return auxShapeVertex;
    }

    // TODO A veces falla cuando 2 vértices están muy cerca (creo que se hace "lío" con el concepto de minPoint)
    public boolean playersAnyMatch(ArrayList<Player> players) {
        float[][] distanceMat = new float[players.size() + 1][vertexNumber + 1];
        int[] minPoint = new int[vertexNumber];

        for (int i = 0; i < vertexNumber; i++) minPoint[i] = -1;

        for (int i = 0; i < players.size(); i++) {
            for (int j = 0; j < vertexNumber; j++) {
                distanceMat[i][j] = dist((float) players.get(i).getLocation().getX(), (float) players.get(i).getLocation().getY(), shapeVertex.get(j).x, shapeVertex.get(j).y);
                // Utils.log("Distancia de player " + i + " a punto " + j + " = " + distanceMat[i][j]);
                if (distanceMat[i][j] < DIST_TOLERANCE)
                    minPoint[i] = j;
            }
        }

        for (int i = 0; i < vertexNumber; i++) {
            // Utils.log(minPoint[0] + " + " + minPoint[1] + " + " + minPoint[2] + " = " + minPoint[0] + minPoint[1] + minPoint[2]);
            if (minPoint[i] == -1) return false;
            if (Utils.distinctIntArrayValues(minPoint))
                return true;
            else
                return false;
        }
        return false;
    }

    public int getVertexNumber() {
        return vertexNumber;
    }

    public Point getVertex(int i) {
        return shapeVertex.get(i);
    }

    // adapted from http://www.mathopenref.com/coordpolygonarea2.html
    private float polygonArea(ArrayList<Point> p, int numPoints)
    {
        float area = 0;         // Accumulates area in the loop
        int j = numPoints - 1;  // The last vertex is the 'previous' one to the first

        for (int i = 0; i < numPoints; i++) {
            area = area + (p.get(j).x + p.get(i).x) * (p.get(j).y - p.get(i).y);
            j = i;  //j is previous vertex to i
        }

        Utils.log(String.valueOf(area/2));
        return abs(area/2);
    }
}
