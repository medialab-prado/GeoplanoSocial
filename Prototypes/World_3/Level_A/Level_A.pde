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
static final int COLOR_WHITE=255;
static final int COLOR_LIGHT_GREY=170;// 2/3
final int COLOR_RED=color(255, 0, 0);
final int COLOR_GREEN=color(0, 255, 0);
final int COLOR_BLUE=color(0, 0, 255);


static final int PLAYERS_NUMBER = 3;
static final float PLAYERS_RADIUS = 100.0f;
final int[] PLAYERS_COLORS = new int[]{COLOR_RED, COLOR_GREEN, COLOR_BLUE};

//Background related
static PGraphics BG;

//Players
static Node [] players;

/**********************
 *PROCESSING FUNCTIONS*
 **********************/

//Runs before setup, allows set screen size with variables ;)
//XXX: Maybe needed to change renderer in the future
void settings() {
  size(SCREEN_WIDTH, SCREEN_HEIGHT);
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
}

void draw() {
  //long startTime = System.nanoTime();

  //Make a clone of bg
  PGraphics pg = cloneGraphics(BG);

  //Update elements
  update();

  //Draw bg and elements
  drawPlayers(pg);
  drawIntersection(pg);
  drawScene(pg);

  //println("Duration\t"+ (System.nanoTime() - startTime));

  //saveFrame();//Imagemagick -> convert -delay 60,1000  -loop 0 *.tif World1.gif
}


/***************
 *OWN FUNCTIONS*
 ***************/


/*LOOP FUNCTIONS*/

void update() {
  players[0].move(mouseX, mouseY);
  //players[1].move(width-mouseX, height-mouseY);
  players[1].move(width-mouseX, mouseY);//Mirroring for simulation
  players[2].move(width/2, height-mouseY);//Mirroring for simulation
}

void drawScene(PGraphics pg) {
  //Draw the pg
  image(pg, 0, 0);
}

void drawPlayers(PGraphics pg) {
  for (int i=0; i<players.length; i++)
    players[i].draw(pg);
}
void drawIntersection(PGraphics pg) {

  for (int i=0; i<players.length; i++)
    for (int j=i+1; j<players.length; j++)
      drawIntersection(pg, players[i], players[j]);
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