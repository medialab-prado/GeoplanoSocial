/**
 * World 2 - Meeting
 * Level C 
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

//Energy to be shared among players
static final float PLAYERS_ENERGY = 100.0f;

//Background related
static PGraphics BG;

//Players
static Player p1;
static Player p2;

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
  BG=generateBackgroundFromImage("frame.png", width, height);

  //Generate players
  p1 = new Player(PLAYERS_ENERGY, COLOR_WHITE, true);
  p2 = new Player(0, COLOR_WHITE, false);
}

void draw() {
  //long startTime = System.nanoTime();

  //Make a clone of bg
  PGraphics pg = cloneGraphics(BG);

  //Update elements
  update();

  //Draw bg and elements
  drawBackground(pg);
  drawPlayers(pg);

  //println("Duration\t"+ (System.nanoTime() - startTime));

  //saveFrame();//Imagemagick -> convert -delay 60,1000  -loop 0 *.tif World1.gif
}


/***************
 *OWN FUNCTIONS*
 ***************/


/*LOOP FUNCTIONS*/

void update() {
  p1.move(mouseX, mouseY);
  p2.move(width-mouseX, height-mouseY);//Mirroring for simulation

  //Energy transference
  p1.checkTransference(p2);
}

void drawBackground(PGraphics pg) {
  //Draw the pg
  image(pg, 0, 0);
}

void drawPlayers(PGraphics pg) {
  p1.draw(pg);
  p2.draw(pg);
  drawLights(pg);
}

/*UTILITY FUNCTIONS*/

PGraphics generateBackgroundFromImage(String fileName, int _width, int _height) {

  //Load image and resize it
  PImage img = loadImage(fileName);
  img.resize(_width, _height);

  //Generate the graphics buffer and initialize it
  PGraphics pg = createGraphics(_width, _height);
  pg.beginDraw();

  //Draw the corresponding background on the buffer
  pg.image(img, 0, 0);

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


/*DRAW FUNCTIONS*/

public void drawLights(PGraphics pg){
  
  loadPixels();
  for (int x = 0; x < pg.width; x++) {
    for (int y = 0; y < pg.height; y++)
    {
      int index = x + y * pg.width;

      float r = red(pg.pixels[index]);
      float g = green(pg.pixels[index]);
      float b = blue(pg.pixels[index]);

      float energyP1 = p1.getEnergy();
      float energyP2 = p2.getEnergy();

      PVector coordinatesP1 = p1.getCoordinates();
      PVector coordinatesP2 = p2.getCoordinates();

      float d1 = dist(x, y, coordinatesP1.x, coordinatesP1.y);
      float d2 = dist(x, y, coordinatesP2.x, coordinatesP2.y);

      float brightnessP1 = 255 * (energyP1 - d1) / energyP1;
      float brightnessP2 = 255 * (energyP2 - d2) / energyP2;

      float maxBrightness = max(brightnessP1, brightnessP2);
      maxBrightness=constrain(maxBrightness, -255, 255);

      r += maxBrightness;
      g += maxBrightness;
      b += maxBrightness;



      r=constrain(r, 0, 255);
      g=constrain(g, 0, 255);
      b=constrain(b, 0, 255);

      int c = color(r, g, b);

      if (maxBrightness>-255) {
        if (brightnessP1>brightnessP2)c = color(r, g, 255*maxBrightness/255);
        else c = color(255*maxBrightness/255, g, b);
      }

      pixels[index] = c;
    }
  }
  updatePixels();
}