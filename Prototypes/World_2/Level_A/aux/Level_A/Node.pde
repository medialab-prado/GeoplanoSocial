protected class Node {

  private PVector coordinates;
  private float radius;
  private int colour;
  private PGraphics pg;
  
  // generados para Level2A by Josué
  private int numberOfPoints = 5;
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

  //Constructors
  public Node(float radius, int colour) {
    this.coordinates = new PVector(0, 0);
    this.colour = colour;
    this.radius = radius;
    this.pg = createGraphics(width, height);
  }

  public Node() {
    controlValueArray[0] = controlValue1;
    controlValueArray[1] = controlValue2;
    controlValueArray[2] = controlValue3;
    controlValueArray[3] = controlValue4;
    controlValueArray[4] = controlValue5;
    
        this.coordinates = new PVector(0, 0);
    this.coordinates.x = SCREEN_WIDTH / 2;
    coordinates.y = SCREEN_HEIGHT / 2;
    
    this.pg = createGraphics(width, height);
  }


  //Moves node
  public void move(int x, int y) {
    coordinates.x = x;
    coordinates.y = y;
    draw(pg);
  }

  //Draw node
  private void draw(PGraphics pg) {
    pg.beginDraw();
    pg.background(0.0f, 1.0f);
    pg.fill(colour);
    pg.noStroke();
    // pg.ellipse(coordinates.x, coordinates.y, radius, radius*2);
    generateDeformedCircle(mouseX, mouseY);
    pg.endDraw();
  }

  void generateDeformedCircle(float locX, float locY) {
    float dx, dy;
    PVector coordinates = new PVector(0, 0);
    coordinates.x = locX;
    coordinates.y = locY;

  generateRadiusPoints();
  translate(locX, locY);
  
  
  float a = PVector.angleBetween(targetPos, coordinates);
  println(degrees(a)); 
    
      if (turnTowardsObject(targetPos, coordinates)) {
        println("faced");
      }
      else {
                println("NO faced");
      }
  
  
  
  
  
  
  
  // float r = random(-0.2, 0.2);
  //rotation = rotation + r;
  //if (rotation > TWO_PI) rotation = TWO_PI;
  //else if (rotation < 0) rotation = 0;
  //rotation = rotation + 45;
  //if (rotation == 360) rotation = 0;
  //rotate(rotation);
  noFill();
  stroke(255);
  curveTightness(-0.3);
  beginShape();
  for (int j=0; j<numberOfPoints+3; j++) {
    if (j < numberOfPoints) {
      int rPoint = radiusPoint[j];
      curveVertex(sin((TWO_PI/numberOfPoints)*j)*rPoint, cos((TWO_PI/numberOfPoints)*j)*rPoint);
    } else {
      int rPoint = radiusPoint[(j-numberOfPoints)];
      curveVertex(sin((TWO_PI/numberOfPoints)*(j-numberOfPoints))*rPoint, 
        cos((TWO_PI/numberOfPoints)*(j-numberOfPoints))*rPoint);
    }
  }
  endShape();
}
  
  public boolean pointInCircunference(PVector point) {
    //Circle equation
    float firstTerm=pow(point.x-coordinates.x, 2)+pow(point.y-coordinates.y, 2);
    float secondTerm=radius*radius;

    if (firstTerm==secondTerm) {
      return true;
    } else {
      return false;
    }
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
}

//Checks and reacts on intersections
private static boolean checkIntersection(Node n1, Node n2) {
  boolean collision=false;
  float distance = dist(n1.getCoordinates().x, n1.getCoordinates().y, n2.getCoordinates().x, n2.getCoordinates().y);
  if (distance < n1.getRadius() + n2.getRadius()) {//Collision between nodes
    //Intersection
    collision=true;
  }
  return collision;
}

void generateRadiusPoints() {
  //controlValueArray[0] = controlValue1;
  //controlValueArray[1] = controlValue2;
  //controlValueArray[2] = controlValue3;
  //controlValueArray[3] = controlValue4;
  //controlValueArray[4] = controlValue5;

  for (int j=0; j<numberOfPoints; j++) {
    // float r = random(-0.1, 0.1);
    float r = -3;
    controlValueArray[0] = controlValueArray[0] + r;
    if ((controlValueArray[0] + r) > 0.5) controlValueArray[0] = 0.5;
    else if ((controlValueArray[0] + r) < -3) controlValueArray[0] = -3;
    // controlValueArray[1] = controlValueArray[1] + random(-0.2,0.2);
    radiusPoint[j] = int(outerRing-controlValueArray[j]*spaceBetween);
  }
}