/**
 * World 3 - Venn lights
 * Level A 
 */

/***********
 *CONSTANTS*
 ***********/

static final int FPS=60;


//For testing purposes, will be 1 on the facade
static final int SCREEN_SCALE=4;

//Size of game area in facade -> http://medialab-prado.es/article/fachada_digital_informacion_tecnica
static final int SCREEN_WIDTH=192 * SCREEN_SCALE;
static final int SCREEN_HEIGHT=125 * SCREEN_SCALE;


//Handy colors, preventing magic numbers
static final int COLOR_BLACK=0;
static final int COLOR_WHITE=255;
static final int COLOR_LIGHT_GREY=170;// 2/3
final int COLOR_RED=color(255, 0, 0);
final int COLOR_GREEN=color(0, 255, 0);
final int COLOR_BLUE=color(0, 0, 255);


static final int PLAYERS_NUMBER = 3;
static final float PLAYERS_RADIUS = 20.0f;
final int[] PLAYERS_COLORS = new int[]{COLOR_RED, COLOR_GREEN, COLOR_BLUE};

//Background related
static PGraphics BG;

//Players
static Node [] players;

//Lines betwwen
final float DIST_MIN = 300;

float[] triangle;

/**********************
 *PROCESSING FUNCTIONS*
 **********************/

//Runs before setup, allows set screen size with variables ;)
//XXX: Maybe needed to change renderer in the future
void settings() {
  size(SCREEN_WIDTH, SCREEN_HEIGHT);
  smooth(8);
}

void setup() {
  //Parameters
  frameRate(FPS);

  noCursor();//Ugly

  //Generate background
  BG=generateBackgroundPlain(width, height);


  //Generate players
  players=new Node[PLAYERS_NUMBER];
  for (int i=0; i<players.length; i++)
    players[i] = new Node(PLAYERS_RADIUS, PLAYERS_COLORS[i]);

  //Generate graphic holding intersections
  // pgIntersection = createGraphics(width, height);
  
  triangle = new float[]{(width /2), 100, (width /2)+50, 400, (width /2)-50, 400};
}

void draw() {
  //long startTime = System.nanoTime();

  //Make a clone of bg
  PGraphics pg = cloneGraphics(BG);

  //Update elements
  update();

  //Draw bg and elements
  drawBackground(pg);


  if (playersTriangleMatch())
    drawRandomTriangle(true);
  else
    drawRandomTriangle(false);
    
      drawPlayers();
  drawLinesBetweenPlayers();


  //println("Duration\t"+ (System.nanoTime() - startTime));

  // saveFrame();//Imagemagick -> convert -delay 60,1000  -loop 0 *.tif World3B.gif
}


/***************
 *OWN FUNCTIONS*
 ***************/
void drawRandomTriangle(boolean doRandom) {
  float distance1, distance2, distance3;
  do {
    if (doRandom) {
      //triangle[0] = random(0, width);
      //triangle[1] = random(0, height);  
      //triangle[2] = random(0, width);
      //triangle[3] = random(0, height);  
      //triangle[4] = random(0, width);
      //triangle[5] = random(0, height);
      float r = random (20, (width /2));
      float rr = random (0, (height / 2));

      triangle[0] = (width /2) - r;
      triangle[1] = height - rr;  
      triangle[2] = (width /2) + r;
      triangle[3] = height - rr;  
      triangle[4] = (width /2);
      triangle[5] = rr;
      
      //triangle[0] = width /2;
      //triangle[1] = rr;  
      //triangle[2] = (width /2) + r;
      //triangle[3] = (height /2) -rr;  
      //triangle[4] = (width /2) - r;
      //triangle[5] = (height /2) - rr;
    }
   
    distance1 = dist(triangle[0], triangle[1], triangle[2], triangle[3]);
    distance2 = dist(triangle[0], triangle[1], triangle[4], triangle[5]);
    distance3 = dist(triangle[4], triangle[5], triangle[2], triangle[3]);
  } while ((distance1 < DIST_MIN) && (distance2 < DIST_MIN) && (distance3 < DIST_MIN));
  noStroke();
  triangle(triangle[0], triangle[1], triangle[2], triangle[3], triangle[4], triangle[5]);
}

boolean playersTriangleMatch() {
  float DIST_TOLERANCE = 20;
  float[][] distanceMat = new float[players.length + 1][3 + 1];
  int[] minPoint = new int[]{-1, -1, -1};

  for (int i=0; i<players.length; i++)
    for (int j=0; j<3; j++) {
      distanceMat[i][j] = dist(players[i].getCoordinates().x, players[i].getCoordinates().y, triangle[2*j], triangle[(2*j)+1]);
      println("Distancia de player " + i + " a punto " + j + " = " + distanceMat[i][j]);
      if (distanceMat[i][j] < DIST_TOLERANCE)
        minPoint[i] = j;
    }
  println(minPoint[0] + " + " + minPoint[1] + " + " + minPoint[2] + " = " + minPoint[0] + minPoint[1] + minPoint[2]);
  if ((minPoint[0] == -1) || (minPoint[1] == -1) || (minPoint[2] == -1)) return false;
  if ((minPoint[0] != minPoint[1]) && (minPoint[0] != minPoint[2]) && (minPoint[1] != minPoint[2]))
    return true; //<>//
  else
    return false;
}
/*LOOP FUNCTIONS*/

void update() {
  players[0].move(mouseX, mouseY);
  players[1].move(width-mouseX, mouseY);//Mirroring for simulation
  players[2].move(width/2, height-mouseY);//Mirroring for simulation
}

void drawBackground(PGraphics pg) {
  //Draw the pg
  image(pg, 0, 0);
}

void drawPlayers() {
  for (int i=0; i<players.length; i++) {
    //Draw nodes on screen
    image(players[i].getPG(), 0, 0);
  }
}
void drawLinesBetweenPlayers() {
  for (int i=0; i<players.length; i++) {  
    for (int j=0; j<players.length; j++) {
      if (i != j) {
        float distancia = dist(players[i].getCoordinates().x, players[i].getCoordinates().y, players[j].getCoordinates().x, players[j].getCoordinates().y);
        if (distancia < DIST_MIN) {
          stroke(255);
          fill(255);
          float ancho = map(distancia, 0, width/2, 0, 10);
          ancho = constrain(ancho, 0, 50);
          if (ancho > 10) ancho = 10;
          strokeWeight(10-ancho);
          line(players[i].getCoordinates().x, players[i].getCoordinates().y, players[j].getCoordinates().x, players[j].getCoordinates().y);
        }
      }
    }
  }

  //
}

/*UTILITY FUNCTIONS*/

PGraphics generateBackgroundPlain(int _width, int _height) {


  //Generate the graphics buffer and initialize it
  PGraphics pg = createGraphics(_width, _height);
  pg.beginDraw();

  //Draw the corresponding background on the buffer
  pg.background(COLOR_LIGHT_GREY);

  //End rendering on graphics buffer
  pg.endDraw();

  return pg;
}

PGraphics cloneGraphics(PGraphics src) {

  //Create a new PG and initialize it
  PGraphics dest = createGraphics(src.width, src.height);
  dest.beginDraw();
  dest.endDraw();

  //Copy pixels
  src.loadPixels();
  dest.loadPixels();
  arrayCopy(src.pixels, dest.pixels);
  dest.updatePixels();

  return dest;
}