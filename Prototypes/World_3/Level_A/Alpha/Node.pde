protected class Node {

  private PVector coordinates;
  private float radius;
  private int colour;

  //Constructors
  public Node(float radius, int colour) {
    this.coordinates = new PVector(0, 0);
    this.colour = color(red(colour),green(colour),blue(colour), 127);
    this.radius = radius;
  }



  //Moves node
  public void move(int x, int y) {
    coordinates.x = x;
    coordinates.y = y;
  }

  //Draw node
  public void draw(PGraphics pg) {
    pg.beginDraw();
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
}