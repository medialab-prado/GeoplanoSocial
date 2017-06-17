protected class Node {

  private PVector coordinates;
  private float radius;
  private int colour;
  private PGraphics pg;
  
  //Constructors
  public Node(float radius, int colour) {
    this.coordinates = new PVector(0, 0);
    this.colour = color(red(colour),green(colour),blue(colour));
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
  public void draw(PGraphics pg) {
    pg.beginDraw();
    pg.background(0.0f, 1.0f);
    pg.fill(colour);
    pg.noStroke();
    pg.ellipse(coordinates.x, coordinates.y, radius*2, radius*2);
    pg.endDraw();
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