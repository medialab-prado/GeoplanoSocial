package es.geoplanosocial.util;

import es.geoplanosocial.levels.Level;
import es.geoplanosocial.players.Player;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import static es.geoplanosocial.util.Constants.*;


import static processing.core.PApplet.dist;


/**
 * Random shape to imitate methods
 * Created by josué on 04/08/17.
 */
public class RandomShape {

    private ArrayList<Point> shapeVertex;
    private int vertexNumber;

    private float DIST_TOLERANCE = 20;

    public RandomShape(int vertexNumber) {
        shapeVertex = new ArrayList<>();
        this.vertexNumber = vertexNumber;

        for (int i = 0; i < vertexNumber; i++) {
            shapeVertex.add(new Point(Utils.randomInt(1, Constants.LEVEL_HEIGHT), Utils.randomInt(1, Constants.LEVEL_HEIGHT)));
        }

        if (DEBUG) {
            shapeVertex.set(1, new Point(10, 10));
            shapeVertex.set(2, new Point(50, 100));
        }
    }

    public void updateRamdomShape() {
        shapeVertex.clear();
        for (int i = 0; i < vertexNumber; i++) {
            shapeVertex.add(new Point(Utils.randomInt(1, Constants.LEVEL_HEIGHT), Utils.randomInt(1, Constants.LEVEL_HEIGHT)));
        }

        if (DEBUG) {
            shapeVertex.set(1, new Point(10, 10));
            shapeVertex.set(2, new Point(50, 100));
        }
    }

    public boolean playersMatch(ArrayList<Player> players) {
        float[][] distanceMat = new float[players.size() + 1][vertexNumber + 1];
        int[] minPoint = new int[]{-1, -1, -1};

        for (int i=0; i<players.size(); i++)
            for (int j=0; j<vertexNumber; j++) {
                distanceMat[i][j] = dist((float)players.get(i).getLocation().getX(), (float)players.get(i).getLocation().getY(), shapeVertex.get(j).x, shapeVertex.get(j).y);
                Utils.log("Distancia de player " + i + " a punto " + j + " = " + distanceMat[i][j]);
                if (distanceMat[i][j] < DIST_TOLERANCE)
                    minPoint[i] = j;
            }
        Utils.log(minPoint[0] + " + " + minPoint[1] + " + " + minPoint[2] + " = " + minPoint[0] + minPoint[1] + minPoint[2]);
        if ((minPoint[0] == -1) || (minPoint[1] == -1) || (minPoint[2] == -1)) return false;
        if ((minPoint[0] != minPoint[1]) && (minPoint[0] != minPoint[2]) && (minPoint[1] != minPoint[2]))
            return true; //<>//
        else
            return false;
    }

    public int getVertexNumber() {
        return vertexNumber;
    }

    public Point getVertex(int i) {
        return shapeVertex.get(i);
    }
}
