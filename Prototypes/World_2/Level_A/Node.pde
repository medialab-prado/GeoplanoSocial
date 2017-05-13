protected class Node {

  private PVector coordinates;
  private float radius;
  private int colour;
  private PGraphics pg;

  // generados para Level2A by Josué
  int numberOfPoints = 5;
  int outerRing = 30;
  int spaceBetween = 10;
  float controlValue1 = -1;
  float controlValue2 = 0.5;
  float controlValue3 = 0.5;
  float controlValue4 = 0.5;
  float controlValue5 = 0.5;
  float[] controlValueArray = new float[numberOfPoints];
  int[] radiusPoint = new int[numberOfPoints];
  float rotation = 0;
  float playerAngle = 180;
  PVector targetPos;
  float distance;


  //Constructors
  public Node(int colour) {
    this.coordinates = new PVector(0, 0);
    this.colour = colour;
    // this.radius = radius;
    this.pg = createGraphics(width, height);
    
      controlValueArray[0] = controlValue1;
  controlValueArray[1] = controlValue2;
  controlValueArray[2] = controlValue3;
  controlValueArray[3] = controlValue4;
  controlValueArray[4] = controlValue5;
  
  targetPos = new PVector(width/2, height/2);
  }




  //Moves node
  public void move(int x, int y, PVector otherPos) {
    coordinates.x = x;
    coordinates.y = y;
    this.targetPos = otherPos;
    distance = dist(x, y, targetPos.x, targetPos.y);
    draw(pg);
  }

  //Draw node
  private void draw(PGraphics pg) {
    pg.beginDraw();
    pg.background(0.0f, 1.0f);
    //pg.fill(colour);
    //pg.noStroke();
    // pg.ellipse(coordinates.x, coordinates.y, radius, radius*2);
    generateDeformedCircle(coordinates.x, coordinates.y, pg);
    pg.endDraw();
  }

  void generateDeformedCircle(float locX, float locY, PGraphics pg) {
    float dx, dy;
    PVector coordinates = new PVector(0, 0);
    coordinates.x = locX;
    coordinates.y = locY;

    generateRadiusPoints();
    println(radiusPoint);
    
    pg.translate(locX, locY);
    
          if (turnTowardsObject(targetPos, coordinates, pg)) {
        println("faced");
      }
      else {
                println("NO faced");
      }

    pg.fill(colour);
    pg.noStroke();
    pg.curveTightness(-0.3);
    pg.beginShape();
    for (int j=0; j<numberOfPoints+3; j++) {
      if (j < numberOfPoints) {
        int rPoint = radiusPoint[j];
        pg.curveVertex(sin((TWO_PI/numberOfPoints)*j)*rPoint, cos((TWO_PI/numberOfPoints)*j)*rPoint);
      } else {
        int rPoint = radiusPoint[(j-numberOfPoints)];
        pg.curveVertex(sin((TWO_PI/numberOfPoints)*(j-numberOfPoints))*rPoint, 
          cos((TWO_PI/numberOfPoints)*(j-numberOfPoints))*rPoint);
      }
    }
    pg.endShape();
  }


  //Getters and setters
  public PVector getCoordinates() {
    return coordinates;
  }

  public float getRadius() {
    return radius;
  }

  public PGraphics getPG() {
    return pg;
  }




  void generateRadiusPoints() {
    //controlValueArray[0] = controlValue1;
    //controlValueArray[1] = controlValue2;
    //controlValueArray[2] = controlValue3;
    //controlValueArray[3] = controlValue4;
    //controlValueArray[4] = controlValue5;

    for (int j=0; j<numberOfPoints; j++) {
      //float r = random(-0.1, 0.1);
      // float r = -3;
      float r = map(distance, 0, width, 0.1, -10);
      println("map:" + r);
      // controlValueArray[0] = controlValueArray[0] + r;
      controlValueArray[0] = r;
      //if ((controlValueArray[0] + r) > 0.5) controlValueArray[0] = 0.5;
      //else if ((controlValueArray[0] + r) < -3) controlValueArray[0] = -3;
      // controlValueArray[1] = controlValueArray[1] + random(-0.2,0.2);
      radiusPoint[j] = int(outerRing-controlValueArray[j]*spaceBetween);
    }
  }

  boolean turnTowardsObject(PVector gotoPos, PVector botPos, PGraphics pg)
  {
    float angle;
    angle = round(degrees(circularAngleBetween(gotoPos, botPos)));

    float distanciaDirecta = abs(playerAngle - angle);
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

    println("A: " + angle + " P: " + playerAngle + " D: " + distanciaDirecta + " I: " + distanciaIndirecta);

    if (playerAngle > 360) playerAngle -= 360;
    if (playerAngle < 0) playerAngle += 360;

    //
    //angle = circularAngleBetween( gotoPos, botPos );
    if (round(playerAngle) == round(degrees(angle)))
    {
      return true;
    } else
    {
      pg.rotate(radians(playerAngle));
      return false;
    }
  }

  float circularAngleBetween (PVector myPVector1, PVector myPVector2) {
    // from http://forum.processing.org/topic/pvector-anglebetween
    // with thanks.
    // delivers an angle in rad where myPVector1 is considered as a
    // point being the center of a circle and myPVector2 a point being on the
    // circumference.
    //
    float a = atan2(myPVector1.y-myPVector2.y, myPVector1.x-myPVector2.x) - (TWO_PI/4);
    if (a<0) {
      a+=TWO_PI;
    }
    return a;
  } // func
}