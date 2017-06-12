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


static final int PLAYERS_NUMBER = 2;
static final float PLAYERS_RADIUS = 100.0f;
final int[] PLAYERS_COLORS = new int[]{COLOR_RED, COLOR_GREEN, COLOR_BLUE};

//Background related
static PGraphics BG;

//Players
static Node [] players;

//Intersection
static PGraphics pgIntersection;

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
  pgIntersection = createGraphics(width, height);
}

void draw() {
  //long startTime = System.nanoTime();

  //Make a clone of bg
  PGraphics pg = cloneGraphics(BG);

  //Update elements
  update();

  //Draw bg and elements
  drawBackground(pg);
  drawPlayers();
  drawIntersection(pgIntersection);

  //println("Duration\t"+ (System.nanoTime() - startTime));

  //saveFrame();//Imagemagick -> convert -delay 60,1000  -loop 0 *.tif World1.gif
}


/***************
 *OWN FUNCTIONS*
 ***************/


/*LOOP FUNCTIONS*/

void update() {
  players[0].move(mouseX, mouseY);
  players[1].move(width-mouseX, mouseY);//Mirroring for simulation
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
void drawIntersection(PGraphics pg) {


  //Clear intersection PGraphics
  pg.beginDraw();
  pg.background(0.0f, 1.0f);
  pg.endDraw();

  //Draw intersection in pgraphics
  drawIntersections(pg, players, false);

  //Draw it on screen
  image(pg, 0, 0);
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