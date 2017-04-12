protected class Player {

  private static final float TRANSFER_COEFF = 1.0f;
  private static final float MIN_ENERGY = 10.0f;

  private PVector coordinates;
  private float energy;//Also represents node radius
  private int colour;
  private boolean giving;//If is the one giving energy

  //Constructors
  public Player(float energy, int colour, boolean giving) {
    this.coordinates = new PVector(0, 0);

    this.energy = (energy > MIN_ENERGY ? energy : MIN_ENERGY);
    this.colour = colour;
    this.giving = giving;
  }

  //Checks and transfers energy if needed
  public void checkTransference(Player p) {
    float distance = dist(p.getCoordinates().x, p.getCoordinates().y, coordinates.x, coordinates.y);
    if (distance < p.getEnergy() + energy) {//Collision between nodes
      transfer();
      p.transfer();
      if (energy <= MIN_ENERGY) {
        energy = MIN_ENERGY;
        giving = false;
        p.setGiving(true);
      } else if (p.getEnergy() <= MIN_ENERGY) {
        p.setEnergy(MIN_ENERGY);
        p.setGiving(false);
        giving = true;
      }
    }
  }

  //Transfers energy
  //XXX done a linear interchange maybe change to exponential depending on distance?
  public void transfer() {
    int sign = giving ? -1 : 1;
    energy += sign * TRANSFER_COEFF;
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
    pg.ellipse(coordinates.x, coordinates.y, energy*2, energy*2);
    pg.endDraw();
  }

  //Getters and setters
  public float getEnergy() {
    return energy;
  }

  public PVector getCoordinates() {
    return coordinates;
  }

  public void setGiving(boolean giving) {
    this.giving = giving;
  }

  public void setEnergy(float energy) {
    this.energy = energy;
  }
}