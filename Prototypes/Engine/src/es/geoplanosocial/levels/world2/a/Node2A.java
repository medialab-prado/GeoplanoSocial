package es.geoplanosocial.levels.world2.a;

import es.geoplanosocial.players.Node;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Constants;
import es.geoplanosocial.util.Utils;
import processing.core.PGraphics;
import processing.core.PVector;
import java.awt.*;
import static java.lang.Float.max;
import static java.lang.Float.min;
import static processing.core.PApplet.*;
import static processing.core.PConstants.TWO_PI;

public class Node2A extends Node {

    private final int color;
    private int numberOfPoints = 5;
    private int outerRing;
    int spaceBetween = 2;
    private float[] controlValueArray = new float[numberOfPoints];
    private int[] radiusPoint = new int[numberOfPoints];
    private float playerAngle = 180;
    PVector targetPos;
    float distance;



    public Node2A(int color, Player player) {
        super(color, player);
        this.color = color;
        this.outerRing = this.getBoundingBox().width;
        controlValueArray[0] = -1;
        controlValueArray[1] = (float) 0.5;
        controlValueArray[2] = (float) 0.5;
        controlValueArray[3] = (float) 0.5;
        controlValueArray[4] = (float) 0.5;
    }

    @Override
    public void draw(PGraphics pg) {
        pg.beginDraw();
        generateDeformedCircle(this.getLocation().x, this.getLocation().y, pg);
        pg.endDraw();
    }

    private void generateDeformedCircle(float locX, float locY, PGraphics pg) {
        PVector coordinates = new PVector(0, 0);
        coordinates.x = locX;
        coordinates.y = locY;

        generateRadiusPoints();
        //Utils.log(String.valueOf(radiusPoint));

        pg.translate(locX, locY);

        if (turnTowardsObject(targetPos, coordinates, pg)) {
            //Utils.log("faced");
        }
        /*else {
            Utils.log("NO faced");
        }*/

        pg.fill(color);
        pg.noStroke();
        pg.curveTightness((float) -0.3);
        pg.beginShape();
        for (int j=0; j<numberOfPoints+3; j++) {
            if (j < numberOfPoints) {
                int rPoint = radiusPoint[j];
                pg.curveVertex((float)sin((TWO_PI/numberOfPoints)*j)*rPoint, (float)cos((TWO_PI/numberOfPoints)*j)*rPoint);
            } else {
                int rPoint = radiusPoint[(j-numberOfPoints)];
                pg.curveVertex((float)sin((TWO_PI/numberOfPoints)*(j-numberOfPoints))*rPoint,(float)
                        cos((TWO_PI/numberOfPoints)*(j-numberOfPoints))*rPoint);
            }
        }
        pg.endShape();
    }

    private   void generateRadiusPoints() {
        for (int j=0; j<numberOfPoints; j++) {
            float r = map(distance, 0, Constants.LEVEL_WIDTH, (float) 0.1, -10);
            //Utils.log("map:" + r);
            controlValueArray[0] = r;
            radiusPoint[j] = (int) (outerRing-controlValueArray[j]*spaceBetween);
        }
    }

    private boolean turnTowardsObject(PVector gotoPos, PVector botPos, PGraphics pg)
    {
        float angle;

        angle = java.lang.Math.round(degrees(circularAngleBetween(gotoPos, botPos)));
        float distanciaDirecta = java.lang.Math.abs(playerAngle - angle);
        float distanciaIndirecta = min(playerAngle, angle) - max(playerAngle, angle) + 360;

        if (  playerAngle < angle ) {
            if (distanciaDirecta < distanciaIndirecta) {
                playerAngle = playerAngle + 4;
            } else {
                playerAngle = playerAngle - 4;
            }
        } else if (  playerAngle >  angle ) {
            if (distanciaDirecta < distanciaIndirecta) {
                playerAngle = playerAngle - 4;
            } else {
                playerAngle = playerAngle + 4;
            }
        }

        //Utils.log("A: " + angle + " P: " + playerAngle + " D: " + distanciaDirecta + " I: " + distanciaIndirecta);

        if (playerAngle > 360) playerAngle -= 360;
        if (playerAngle < 0) playerAngle += 360;

        //
        //angle = circularAngleBetween( gotoPos, botPos );
        if (java.lang.Math.round(playerAngle) == java.lang.Math.round(degrees(angle)))
        {
            return true;
        } else
        {
            pg.rotate(radians(playerAngle));
            return false;
        }
    }

    private   float circularAngleBetween (PVector myPVector1, PVector myPVector2) {
        // from http://forum.processing.org/topic/pvector-anglebetween
        // with thanks.
        // delivers an angle in rad where myPVector1 is considered as a
        // point being the center of a circle and myPVector2 a point being on the
        // circumference.
        float a = atan2(myPVector1.y-myPVector2.y, myPVector1.x-myPVector2.x) - (TWO_PI/4);
        if (a<0) {
            a+=TWO_PI;
        }
        return a;
    }

    public void targetPos(Point location) {
        this.targetPos = new PVector(location.x, location.y);
        this.distance = dist(this.getLocation().x, this.getLocation().y, targetPos.x, targetPos.y);
    }
}