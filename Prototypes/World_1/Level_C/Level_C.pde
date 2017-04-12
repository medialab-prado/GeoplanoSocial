/**
 * World 1 - Loneliness
 * Level C - (queso) fundido
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

//Nodes that represent the player will be of this size
static final float PLAYER_SIZE=20 * SCREEN_SCALE;


//Handy colors, preventing magic numbers
static final int COLOR_WHITE=255;
static final int COLOR_BLACK=0;
static final int COLOR_GRAY=100;

//Background related
static final int BG_MODE=2;
static PGraphics BG;

//latest mouse/player position
static float mouseX_prev;
static float mouseY_prev;

// moving mouse/player coloring
static int dynamicPlayerColor = COLOR_WHITE;
static final int DEGRADE_RATE = 1;

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
  noStroke();
  noCursor();//Ugly

  BG=generateBackground(BG_MODE, width, height);
}

void draw() {
  //long startTime = System.nanoTime();

  drawBackground();
  drawPlayer();

  //println("Duration\t"+ (System.nanoTime() - startTime));

  // saveFrame();//Imagemagick -> convert -delay 60,1000  -loop 0 *.tif World1.gif
}


/***************
 *OWN FUNCTIONS*
 ***************/
PGraphics generateBackground(int index, int _width, int _height) {

  //Generate the graphics buffer and initialize it
  PGraphics pg = createGraphics(_width, _height);
  pg.beginDraw();

  //Draw the corresponding background on the buffer
  switch(index) {
  case 0:
    generateBackground0(pg);
    break;
  case 1:
  case 2:
    generateBackground2(pg);
    break;
  default:
    generateBackground1(pg);
    break;

  }

  //End rendering on graphics buffer
  pg.endDraw();

  return pg;
}


void generateBackground0(PGraphics pg) {

  //Set plain background
  pg.background(COLOR_WHITE); 

  //Set dots

  //Basic dots rows and cols
  float radius=PLAYER_SIZE/2.0f;
  pg.fill(COLOR_BLACK);

  for (float x=0; x<width+PLAYER_SIZE; x+=PLAYER_SIZE) {
    for (float y=radius; y<height+PLAYER_SIZE; y+=PLAYER_SIZE) {
      pg.ellipse(x, y, PLAYER_SIZE, PLAYER_SIZE);
    }
  }
}

void generateBackground1(PGraphics pg) {

  //Set plain background
  pg.background(COLOR_WHITE); 

  //Set dots
  //Dots rows and cols symmetric and alternating
  pg.fill(COLOR_BLACK);

  float radius=PLAYER_SIZE/2.0f;

  float excessX=width%PLAYER_SIZE;
  float excessY=height%PLAYER_SIZE;

  float offsetX=excessX/2.0f;
  float offsetY=excessY/2.0f;

  for (float x=offsetX; x<width+PLAYER_SIZE; x+=PLAYER_SIZE) {
    for (float y=offsetY; y<height+PLAYER_SIZE; y+=PLAYER_SIZE) {

      float offsetedX=x;
      //Shift odd rows
      if (((int)(y/PLAYER_SIZE))%2==1) {
        offsetedX-=radius;
      }

      pg.ellipse(offsetedX, y, PLAYER_SIZE, PLAYER_SIZE);
    }
  }
}

void generateBackground2(PGraphics pg) {

  //Set plain background
  pg.background(COLOR_GRAY); 



}

void drawBackground() {
  //Draw the bg image buffer
  image(BG, 0, 0);
}

void drawPlayer() {
  if ((mouseX_prev == mouseX) && (mouseX_prev == mouseX)) {
    dynamicPlayerColor = dynamicPlayerColor - DEGRADE_RATE;
    if (dynamicPlayerColor < COLOR_GRAY)
      dynamicPlayerColor = COLOR_GRAY;
  }
  else {
    dynamicPlayerColor = dynamicPlayerColor + DEGRADE_RATE;
    if (dynamicPlayerColor > COLOR_WHITE)
      dynamicPlayerColor = COLOR_WHITE;
  }
  
  fill(dynamicPlayerColor);
  ellipse(mouseX, mouseY, PLAYER_SIZE, PLAYER_SIZE);
  mouseX_prev = mouseX;
  mouseY_prev = mouseY;
}