

PImage fondo;
//For testing purposes, will be 1 on the facade
static final int SCREEN_SCALE=4;
//Nodes that represent the player will be of this size
static final float PLAYER_SIZE=20 * SCREEN_SCALE;
//Handy colors, preventing magic numbers
static final int COLOR_BLACK=0;
//-----------------------------------
void setup() {
  size(1024, 768);//custom size
  //fullScreen();
  fondo = loadImage("fondo.jpg");
    //Parameters
  noStroke();
  noCursor();//Ugly
}

//-----------------------------------
void draw() {
  background(fondo);


  drawPlayer();

  //saveFrame();//Imagemagick -> convert -delay 60,1000  -loop 0 *.tif World1.gif


 
}

//-----------------------------------



void drawPlayer() {

  fill(COLOR_BLACK);
  ellipse(mouseX, mouseY, PLAYER_SIZE, PLAYER_SIZE);
}