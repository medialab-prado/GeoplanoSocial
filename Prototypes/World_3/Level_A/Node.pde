protected class Node {

  private PVector coordinates;
  private float radius;
  private int colour;
  private PGraphics pg;

  //Constructors
  public Node(float radius, int colour) {
    this.coordinates = new PVector(0, 0);
    this.colour = colour;
    this.radius = radius;
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
    pg.ellipse(coordinates.x, coordinates.y, radius*2, radius*2);
    pg.endDraw();
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

private static PVector[] computeCollisionCircles(Node n1, Node n2) {

  //Values
  float r1=n1.getRadius();
  float r2=n2.getRadius();
  float x1=n1.getCoordinates().x;
  float x2=n2.getCoordinates().x;
  float y1=n1.getCoordinates().y;
  float y2=n2.getCoordinates().y;


  //Pow2
  float r1_2=r1*r1;
  float r2_2=r2*r2;
  float x1_2=x1*x1;
  float x2_2=x2*x2;
  float y1_2=y1*y1;
  float y2_2=y2*y2;


  //Terms when substracting circle's equation for both nodes x=n-my
  float n = ((r1_2-r2_2)-(x1_2-x2_2)-(y1_2-y2_2)) / (2*(x2-x1));
  float m = (y2-y1) /(x2-x1);

  //Substitute x on circle's equation for node 1
  float a = m * m +1;
  float b = 2*x1*m - 2*n*m - 2*y1;
  float c = n*n - 2*x1*n + x1_2 + y1_2 - r1_2;

  PVector solutions = solveQuadraticEquation(a, b, c);


  //Substitute value on x=n-my
  float X1= n - m*solutions.x;
  float X2= n - m*solutions.y;

  return new PVector[]{new PVector(X1, solutions.x), new PVector(X2, solutions.y)};
}


private static PVector[] computeCollisionLineCircle(Node node, float m, float n) {

  float x=node.getCoordinates().x;
  float y=node.getCoordinates().y;
  float x_2=x*x;
  float y_2=y*y;
  float r_2=node.getRadius()*node.getRadius();


  //Substitute x on circle's equation for node 1
  float a = 1 + m*m;
  float b = 2*n*m - 2*x - 2*y*m;
  float c = x_2 + n*n - 2*y*n + y_2 - r_2;


  PVector solutions = solveQuadraticEquation(a, b, c);


  //Substitute value on x=n-my
  float Y1= m*solutions.x + n;
  float Y2= m*solutions.y + n;


  return new PVector[]{new PVector(solutions.x, Y1), new PVector(solutions.y, Y2)};
}

private static PVector solveQuadraticEquation(float a, float b, float c) {

  PVector solutions = new PVector();

  //Solve quadratic equation ay^2 + by + c = 0
  solutions.x=(-b + sqrt(b*b-4*a*c)) / (2*a);
  solutions.y=(-b - sqrt(b*b-4*a*c)) / (2*a);

  return solutions;
}

private static PVector computeClosestToMiddle(PVector[]points, PVector middle) {
  float minDistance=Float.MAX_VALUE;
  int minPointIndex=0;

  for (int i=0; i<points.length; i++) {
    float tempDistance=dist(middle.x, middle.y, points[i].x, points[i].y);
    if (tempDistance<minDistance) {
      minDistance=tempDistance;
      minPointIndex=i;
    }
  }

  return points[minPointIndex];
}


public static void drawIntersections(PGraphics pgIntersection, Node[] nodes, boolean debug) {


  //Compute Region of Interest
  PVector min = new PVector(Float.MAX_VALUE, Float.MAX_VALUE);
  PVector max = new PVector(Float.MIN_VALUE, Float.MIN_VALUE);

  for (int i=0; i<nodes.length; i++) {
    for (int j=i+1; j<nodes.length; j++) {
      if (checkIntersection(nodes[i], nodes[j])) {
        PVector[] circleCollisions=getCollisionBoundaries(nodes[i], nodes[j]);
        for (int k=0; k<circleCollisions.length; k++) {
          min.x=min(min.x, circleCollisions[k].x);
          min.y=min(min.y, circleCollisions[k].y);
          max.x=max(max.x, circleCollisions[k].x);
          max.y=max(max.y, circleCollisions[k].y);
        }
      }
    }
  }

  //Collisions
  if (max.x>=0 && max.y>=0) {

    //pgIntersection.beginDraw();
    //pgIntersection.rect(min.x, min.y, max.x-min.x, max.y-min.y);//Whole collision region
    //pgIntersection.endDraw();

    drawCollisions(pgIntersection, nodes, min, max);//Update color values in ROI

    if (debug) {//Draw debug lines and points
      for (int i=0; i<nodes.length; i++) 
        for (int j=i+1; j<nodes.length; j++) 
          drawDebugInfo(pgIntersection, nodes[i], nodes[j]);
    }
  }
}

private static PVector[] getCollisionBoundaries(Node n1, Node n2) {
  //*Compute collision points on nodes*
  PVector[] circleCollisions=computeCollisionCircles(n1, n2);
  PVector c1= circleCollisions[0];
  PVector c2= circleCollisions[1];

  //One over other then return bounding box
  if (Float.isNaN(c1.x))return new PVector[]{new PVector(n1.getCoordinates().x-n1.getRadius(), n1.getCoordinates().y-n1.getRadius()), new PVector(n1.getCoordinates().x+n1.getRadius(), n1.getCoordinates().y+n1.getRadius())};

  //Middle point
  PVector middle = PVector.add(c1, c2).div(2.0f);

  //*Compute perpendicular line that cuts in the middle point*

  //Slope of line
  float m=(c1.y-c2.y)/(c1.x-c2.x);

  //Perpendicular line including medium point
  m=-1.0f/m;
  float n=middle.y-m*middle.x;


  //*Compute collision points of perpendicular with nodes*
  PVector[] nodePerpCollisions1=computeCollisionLineCircle(n1, m, n);
  PVector[] nodePerpCollisions2=computeCollisionLineCircle(n2, m, n);

  PVector closestNode1=computeClosestToMiddle(nodePerpCollisions1, middle);
  PVector closestNode2=computeClosestToMiddle(nodePerpCollisions2, middle);

  //Same x
  if (c1.x-c2.x==0) {
    return new PVector[]{c1, c2, closestNode1, closestNode2};
  } else {
    //Perpendicular from points that cuts the node
    m=(c1.y-c2.y)/(c1.x-c2.x);

    n=closestNode1.y-m*closestNode1.x;
    PVector[] node1Boundaries=computeCollisionLineCircle(n2, m, n);

    n=closestNode2.y-m*closestNode2.x;
    PVector[] node2Boundaries=computeCollisionLineCircle(n1, m, n);

    return new PVector[]{node1Boundaries[0], node1Boundaries[1], node2Boundaries[0], node2Boundaries[1]};
  }
}

public static void drawCollisions(PGraphics pgIntersection, Node[] nodes, PVector min, PVector max) {

  //Get pixels of nodes
  int[][]nodePixels=new int[nodes.length][];
  for (int i=0; i<nodes.length; i++) {
    PGraphics p=nodes[i].getPG();
    p.loadPixels();
    nodePixels[i]=p.pixels;
  }

  //Set boundaries for region of interest
  int minX=floor(min.x);
  int minY=floor(min.y);
  int maxX=ceil(max.x);
  int maxY=ceil(max.y);


  //Sum values nodes in target region
  pgIntersection.loadPixels();
  for (int x = minX; x < maxX; x++) {
    for (int y = minY; y < maxY; y++) {
      int index = x + y * pgIntersection.width;
      if (index>0) {
        pgIntersection.pixels[index]=0;
        for (int i=0; i<nodes.length; i++) {
          pgIntersection.pixels[index] += nodePixels[i][index];
        }
      }
    }
  }

  pgIntersection.updatePixels();
}

public static void drawDebugInfo(PGraphics pg, Node n1, Node n2) {
  //*Compute collision points on nodes*
  PVector[] circleCollisions=computeCollisionCircles(n1, n2);
  PVector c1= circleCollisions[0];
  PVector c2= circleCollisions[1];

  //Draw collision points on nodes
  pg.beginDraw();
  pg.fill(COLOR_WHITE);
  pg.ellipse(c1.x, c1.y, 5, 5);
  pg.ellipse(c2.x, c2.y, 5, 5);

  //Line between them
  pg.stroke(COLOR_WHITE);
  pg.line(c1.x, c1.y, c2.x, c2.y);

  //Middle point
  PVector middle = PVector.add(c1, c2).div(2.0f);

  pg.ellipse(middle.x, middle.y, 5, 5);

  pg.strokeWeight(1);
  pg.stroke(COLOR_WHITE);

  //*Compute perpendicular line that cuts in the middle point*

  //Slope of line
  float m=(c1.y-c2.y)/(c1.x-c2.x);

  //Perpendicular line including medium point
  m=-1.0f/m;
  float n=middle.y-m*middle.x;


  //*Compute collision points of perpendicular with nodes*
  PVector[] nodePerpCollisions1=computeCollisionLineCircle(n1, m, n);
  PVector[] nodePerpCollisions2=computeCollisionLineCircle(n2, m, n);


  PVector closestNode1=computeClosestToMiddle(nodePerpCollisions1, middle);
  PVector closestNode2=computeClosestToMiddle(nodePerpCollisions2, middle);

  pg.line(closestNode1.x, closestNode1.y, closestNode2.x, closestNode2.y);

  pg.fill(COLOR_BLACK);
  pg.ellipse(closestNode1.x, closestNode1.y, 5, 5);
  pg.ellipse(closestNode2.x, closestNode2.y, 5, 5);



  m=(c1.y-c2.y)/(c1.x-c2.x);
  n=closestNode1.y-m*closestNode1.x;
  PVector[] node1Boundaries=computeCollisionLineCircle(n2, m, n);

  n=closestNode2.y-m*closestNode2.x;
  PVector[] node2Boundaries=computeCollisionLineCircle(n1, m, n);


  pg.fill(COLOR_LIGHT_GREY);
  pg.ellipse(node1Boundaries[0].x, node1Boundaries[0].y, 5, 5);
  pg.ellipse(node1Boundaries[1].x, node1Boundaries[1].y, 5, 5);
  pg.ellipse(node2Boundaries[0].x, node2Boundaries[0].y, 5, 5);
  pg.ellipse(node2Boundaries[1].x, node2Boundaries[1].y, 5, 5);

  pg.endDraw();
}