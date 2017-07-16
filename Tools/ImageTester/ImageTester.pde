/**
 * ImageTester
 * Tool for making image tests. Acts as a carousel.
 */

/***********
 *CONSTANTS*
 ***********/

static final int SCREEN_WIDTH = 1024;
static final int SCREEN_HEIGHT = 768;
static final String SCREEN_RENDERER = P2D;
static final int FPS = 60;
static final int ANTI_ALIASING_LEVEL = 8;

static final boolean DRAW_FACADE_OUTLINE = true;

//Handy colors - Preventing magic numbers
static final int COLOR_WHITE=255;
static final int COLOR_BLACK=0;

static final int COLOR_LIGHT_GREY=170;// 2/3

final int COLOR_MAGENTA=color(255, 0, 255);


//Background related
static PGraphics BG;
static PImage img;

static String[] fileNames;
static int fileIndex=0;

static String DIRECTORY;

static final int LEVEL_WIDTH=192;
static final int LEVEL_HEIGHT=125;
static final int START_WORLD_X=40;
static final int START_WORLD_Y=72;

/**********************
 *PROCESSING FUNCTIONS*
 **********************/

void settings() {
  //Set screen size and renderer type
  size(SCREEN_WIDTH, SCREEN_HEIGHT, SCREEN_RENDERER);

}

void setup() {

  //Set global parameters

  frameRate(FPS);
  smooth(ANTI_ALIASING_LEVEL);//No more aliasing
  noCursor();//Ugly
  
  BG = generateBackground(width, height, SCREEN_RENDERER, DRAW_FACADE_OUTLINE);
  
  DIRECTORY = sketchPath("images");
  fileNames= listFileNames(DIRECTORY);
  printArray(fileNames);
  
  loadImage();
}

void draw() {

  //Draw elements
  
  drawBackground(BG);
  drawImage(BG);
  drawDebug(BG);

}

void keyPressed() {
  if (key == CODED) {
    switch(keyCode) {
      case UP:
      case RIGHT:
        changeIndex(1);
        loadImage();
        break;
      case DOWN:
      case LEFT:
        changeIndex(-1);
        loadImage();
        break;
      }
  }
}

void changeIndex(int interval){
  fileIndex = constrain(fileIndex+interval, 0, fileNames.length-1);
}

void loadImage(){
  img = loadImage(DIRECTORY+File.separator+fileNames[fileIndex]);
  img.resize(LEVEL_WIDTH, LEVEL_HEIGHT);
}

/***************
 *OWN FUNCTIONS*
 ***************/


/*LOOP FUNCTIONS*/

//Updates elements and properties
void update() {
  
}

void drawBackground(PGraphics pg) {
  //Draw the bg in the pg
  image(pg, 0, 0);
}

void drawImage(PGraphics pg) {
  image(img, START_WORLD_X, START_WORLD_Y);
}


void drawDebug(PGraphics pg) {
  textSize(32);
  textAlign(LEFT, TOP);
  text("["+fileIndex+"] "+ fileNames[fileIndex], 0, 0);
}

/*UTILITY FUNCTIONS*/
PGraphics generateBackground(int _width, int _height, String renderer, boolean outlineEnabled) {

  //Generate the graphics buffer and initialize it
  PGraphics pg = createGraphics(_width, _height, renderer);
  pg.beginDraw();

  //Draw the corresponding background on the buffer

  //Set plain background
  pg.background(COLOR_LIGHT_GREY);

  //Draw outline if required
  if (outlineEnabled) {
    pg.strokeWeight(1);//1 pixel
    pg.stroke(COLOR_MAGENTA);
    drawOutline(pg);
  }

  //End rendering on graphics buffer
  pg.endDraw();

  return pg;
}


String[] listFileNames(String dir) {
  File file = new File(dir);
  if (file.isDirectory()) {
    println("Inside");
    String names[] = file.list();
    return names;
  } else {
    println("Outside");
    return null;
  }
}


/*DRAW FUNCTIONS*/

//Draw facade outline. From template provided in Medialab-Prado.
//More info -> http://medialab-prado.es/article/fachada_digital_informacion_tecnica

//FIXME: outline external to game area?
void drawOutline(PGraphics pg) {

  //Big lines
  
  //Right side
  pg.line(231, 72, 231, 196);
  
  //Bottom
  pg.line(231, 196, 40, 196);
  
  //Left side
  pg.line(40, 196, 40, 72);

  //Steps
  
  //1 h
  pg.line(40, 72, 75, 72);

  //2 v
  pg.line(75, 72, 75, 56);

  //2 h
  pg.line(75, 56, 111, 56);

  //3 v
  pg.line(111, 56, 111, 40);

  //3 h
  pg.line(111, 40, 159, 40);

  //3 v
  pg.line(159, 40, 159, 56);

  //4 h
  pg.line(159, 56, 195, 56);

  //4 v
  pg.line(195, 56, 195, 72);

  //5 h
  pg.line(195, 72, 231, 72);
}