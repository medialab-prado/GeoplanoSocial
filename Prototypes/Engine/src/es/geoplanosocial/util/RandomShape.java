package es.geoplanosocial.util;

import es.geoplanosocial.players.Player;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static es.geoplanosocial.util.Constants.DEBUG;
import static es.geoplanosocial.util.Utils.cartesian2polar;
import static java.lang.Math.*;
import static processing.core.PApplet.dist;

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

    // fixme here just for debugging purposes
    public Point centroid;


    public RandomShape(int vertexNumber) {
        shapeVertex = new ArrayList<>();
        this.vertexNumber = vertexNumber;
        updateRamdomShape();
    }

    // TODO establecer una distancia mínima entre 2 puntos
    // TODO (para >4) no controlo que la forma sea "continua" (mirar)
    public void updateRamdomShape() {
        shapeVertex.clear();

        // random thetas generation
        // double[] theta = new Random().doubles(0, 2*PI).distinct().limit(vertexNumber).toArray();
        // random radius generation
        // double[] r = new Random().doubles(0, 2*PI).distinct().limit(vertexNumber).toArray();

        do {
            shapeVertex.clear();
            for (int i = 0; i < vertexNumber; i++) {
                shapeVertex.add(new Point(Utils.randomInt(1, Constants.LEVEL_HEIGHT), Utils.randomInt(1, Constants.LEVEL_HEIGHT)));
                Utils.log(shapeVertex.get(i).x + " " + shapeVertex.get(i).y);
            }

            Utils.log("---------");

            sortVertexToGetClosedShape();
        } while ((polygonArea(shapeVertex, vertexNumber) < MIN_SHAPE_AREA) ||
                (!checkMinDist(shapeVertex)));
        // todo checkNoInLine(shapeVertex)



        // shapeVertex = sortToGetClosedShape();
    }

    private void sortVertexToGetClosedShape() {
        double[] theta = new double[vertexNumber];

        centroid = centroid();

        for (int i = 0; i < vertexNumber; i++) {
            theta[i] = cartesian2polar(shapeVertex.get(i), centroid)[1];
            if (theta[i] < 0) theta[i] = (2*PI) + theta[i];
        }
        int[] index = getSortedIndices(theta);

        ArrayList<Point> auxVertex = (ArrayList<Point>)shapeVertex.clone();
        for (int i = 0; i < vertexNumber; i++) {
            auxVertex.set(index[i], new Point(shapeVertex.get(i).x, shapeVertex.get(i).y));

            Utils.log(auxVertex.get(i).x + " " + auxVertex.get(i).y);
        }
        shapeVertex = auxVertex;
    }

    public void _updateRamdomShape() {
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

    private Point centroid()  {
        double centroidX = 0, centroidY = 0;

        for(Point knot : shapeVertex) {
            centroidX += knot.getX();
            centroidY += knot.getY();
        }
        return new Point((int)round(centroidX / shapeVertex.size()), (int)round(centroidY / shapeVertex.size()));
    }

    // from https://stackoverflow.com/questions/14186529/java-array-of-sorted-indexes
    private int[] getSortedIndices(double[] originalArray)
    {
        int len = originalArray.length;

        double[] sortedCopy = originalArray.clone();
        int[] indices = new int[len];

        // Sort the copy
        Arrays.sort(sortedCopy);

        // Go through the original array: for the same index, fill the position where the
        // corresponding number is in the sorted array in the indices array
        for (int index = 0; index < len; index++)
            indices[index] = Arrays.binarySearch(sortedCopy, originalArray[index]);

        return indices;
    }
}
