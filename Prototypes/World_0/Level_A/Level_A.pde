/**
 * World 1 - Loneliness
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

//Nodes that represent the player will be of this size
static final float PLAYER_SIZE=20 * SCREEN_SCALE;


//Handy colors, preventing magic numbers
static final int COLOR_WHITE=255;
static final int COLOR_BLACK=0;


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
}

void draw() {

  drawBackground();

  drawPlayer();

  //saveFrame();//Imagemagick -> convert -delay 60,1000  -loop 0 *.tif World1.gif
}


/***************
 *OWN FUNCTIONS*
 ***************/

void drawBackground() {

  //Set plain background
  background(COLOR_WHITE); 

  //Set dots
  /*
  //Basic dots rows and cols
   float radius=PLAYER_SIZE/2.0f;
   fill(COLOR_BLACK);
   
   for (float x=0; x<width+PLAYER_SIZE; x+=PLAYER_SIZE) {
   for (float y=radius; y<height+PLAYER_SIZE; y+=PLAYER_SIZE) {
   ellipse(x, y, PLAYER_SIZE, PLAYER_SIZE);
   }
   }
   */


  //Dots rows and cols symmetric and alternating
  fill(COLOR_BLACK);

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

      ellipse(offsetedX, y, PLAYER_SIZE, PLAYER_SIZE);
    }
  }
}


void drawPlayer() {

  fill(COLOR_BLACK);
  ellipse(mouseX, mouseY, PLAYER_SIZE, PLAYER_SIZE);
}