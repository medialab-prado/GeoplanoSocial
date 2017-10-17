
import java.awt.Color;

int level = 1;

void setup() {
  size(960, 625);
  smooth(8);

  noCursor();

  ellipseMode(CENTER);
  strokeCap(PROJECT);

  colorPlayer=randomPlayerColor();
}

final float DEGREES = 360.0f;

void drawCircle(int level) {
  float step = DEGREES/level;
  for (int a=0; a<=DEGREES; a += step) {
    int c = Color.HSBtoRGB(a/DEGREES, 1, 1);
    noFill();
    strokeWeight(playerWidth);
    stroke(c);
    arc(width/2, height/2, height*0.9, height*0.9, radians(a), radians(a+step));
  }
}

int colorPlayer;
int playerWidth = 35;
boolean canCheck= false;

void drawPlayer() {
  noStroke();
  fill(colorPlayer);
  ellipse(mouseX, mouseY, playerWidth, playerWidth);
}


void draw() {

  background(0);
  drawCircle(level);

  checkCollision();


  drawPlayer();
}


int randomPlayerColor() {
  float position = (int)random(level)+1.0f;
  int c = Color.HSBtoRGB(position/level, 1, 1);
  return c;
}

void checkCollision() {
  loadPixels();
  int p = pixels[mouseY*width+mouseX];
  if (p==-16777216) {
    canCheck = true;
    return;
  }
  if (canCheck) {
    //println(colorPlayer+" vs "+p);
    float diff = abs(hue(p)-hue(colorPlayer));
    if (diff<5) {
      level++;
    } else {
      level=1;
    }
    colorPlayer=randomPlayerColor();
    canCheck= false;
    println(diff);
  }
}

void keyPressed() {
  if (key == ENTER) {
    level++;
  }
}