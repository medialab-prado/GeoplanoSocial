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

// generados para Level1A
int numberOfPoints = 5;
int outerRing = 30;
int spaceBetween = 10;

float controlValue1 = -1;
float controlValue2 = 0.5;
float controlValue3 = 0.5;
float controlValue4 = 0.5;
float controlValue5 = 0.5;

float[] controlValueArray = new float[numberOfPoints];
int[] radiusPoint = new int[numberOfPoints];

float rotation = 0;

float playerAngle = 180;

PVector targetPos = new PVector(0, 0);


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

  controlValueArray[0] = controlValue1;
  controlValueArray[1] = controlValue2;
  controlValueArray[2] = controlValue3;
  controlValueArray[3] = controlValue4;
  controlValueArray[4] = controlValue5;
  
  targetPos.x = SCREEN_WIDTH / 2;
  targetPos.y = SCREEN_HEIGHT / 2;
}

void draw() {
  //long startTime = System.nanoTime();

  drawBackground();
  drawPlayer();

  //println("Duration\t"+ (System.nanoTime() - startTime));

  saveFrame();//Imagemagick -> convert -delay 60,1000  -loop 0 *.tif World1.gif
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
  } else {
    dynamicPlayerColor = dynamicPlayerColor + DEGRADE_RATE;
    if (dynamicPlayerColor > COLOR_WHITE)
      dynamicPlayerColor = COLOR_WHITE;
  }

  fill(dynamicPlayerColor);
  // ellipse(mouseX, mouseY, PLAYER_SIZE, PLAYER_SIZE);
  generateDeformedCircle(mouseX, mouseY);
  mouseX_prev = mouseX;
  mouseY_prev = mouseY;
}

void generateDeformedCircle(float locX, float locY) {
    float dx, dy;
    PVector playerPos = new PVector(0, 0);
    playerPos.x = locX;
    playerPos.y = locY;

  generateRadiusPoints();
  translate(locX, locY);
  
  
  float a = PVector.angleBetween(targetPos, playerPos);
  println(degrees(a)); 
    
      if (turnTowardsObject(targetPos, playerPos)) {
        println("faced");
      }
      else {
                println("NO faced");
      }
  
  
  
  
  
  
  
  // float r = random(-0.2, 0.2);
  //rotation = rotation + r;
  //if (rotation > TWO_PI) rotation = TWO_PI;
  //else if (rotation < 0) rotation = 0;
  //rotation = rotation + 45;
  //if (rotation == 360) rotation = 0;
  //rotate(rotation);
  noFill();
  stroke(255);
  curveTightness(-0.3);
  beginShape();
  for (int j=0; j<numberOfPoints+3; j++) {
    if (j < numberOfPoints) {
      int rPoint = radiusPoint[j];
      curveVertex(sin((TWO_PI/numberOfPoints)*j)*rPoint, cos((TWO_PI/numberOfPoints)*j)*rPoint);
    } else {
      int rPoint = radiusPoint[(j-numberOfPoints)];
      curveVertex(sin((TWO_PI/numberOfPoints)*(j-numberOfPoints))*rPoint, 
        cos((TWO_PI/numberOfPoints)*(j-numberOfPoints))*rPoint);
    }
  }
  endShape();
}



void generateRadiusPoints() {
  //controlValueArray[0] = controlValue1;
  //controlValueArray[1] = controlValue2;
  //controlValueArray[2] = controlValue3;
  //controlValueArray[3] = controlValue4;
  //controlValueArray[4] = controlValue5;

  for (int j=0; j<numberOfPoints; j++) {
    // float r = random(-0.1, 0.1);
    float r = -3;
    controlValueArray[0] = controlValueArray[0] + r;
    if ((controlValueArray[0] + r) > 0.5) controlValueArray[0] = 0.5;
    else if ((controlValueArray[0] + r) < -3) controlValueArray[0] = -3;
    // controlValueArray[1] = controlValueArray[1] + random(-0.2,0.2);
    radiusPoint[j] = int(outerRing-controlValueArray[j]*spaceBetween);
  }
}

  boolean turnTowardsObject(PVector gotoPos, PVector botPos)
  {
    float angle;
    angle = round(degrees(circularAngleBetween(gotoPos, botPos)));
    if (  playerAngle < angle ) {
      playerAngle = playerAngle + 4;
    }
    else if (  playerAngle >  angle ) {
      playerAngle = playerAngle - 4;
    } 

        println("A: " + angle + " P: " + playerAngle);

    //
    //angle = circularAngleBetween( gotoPos, botPos );
    if (round(playerAngle) == round(degrees(angle)))
    {
      return true;
    }
    else
    {
       rotate(radians(playerAngle));
      return false;
      
    }
  }
  
    float circularAngleBetween (PVector myPVector1, PVector myPVector2) {
    // from http://forum.processing.org/topic/pvector-anglebetween
    // with thanks.
    // delivers an angle in rad where myPVector1 is considered as a
    // point being the center of a circle and myPVector2 a point being on the
    // circumference.
    //
    float a = atan2(myPVector1.y-myPVector2.y, myPVector1.x-myPVector2.x) - (TWO_PI/4);
    if (a<0) {
      a+=TWO_PI;
    }
    return a;
  } // func