import processing.pdf.*;
import controlP5.*;
ControlP5 controlP5;

color bgColor = color(84,187,237);
int numberOfPoints = 5;
int outerRing = 30;
int spaceBetween = 10;

float controlValue1 = 0.5;
float controlValue2 = 0.5;
float controlValue3 = -1;
float controlValue4 = 0.5;
float controlValue5 = 0.5;

float[] controlValueArray = new float[numberOfPoints];
int[] radiusPoint = new int[numberOfPoints];

int p = 0;

void setup() {
  size(800,800);
  smooth();

  //controlP5 = new ControlP5(this);
  //ControlGroup l = controlP5.addGroup("Controls",10,20);
  //controlP5.addSlider("outerRing",0,350,0,10,150,10).setGroup(l);
  //controlP5.addSlider("numberOfCircles",20,40,0,30,150,10).setGroup(l);
  //controlP5.addSlider("spaceBetween",0,40,0,50,150,10).setGroup(l);
  //controlP5.addSlider("controlValue1",-1,1.5,0,70,150,10).setGroup(l);
  //controlP5.addSlider("controlValue2",-1,1.5,0,90,150,10).setGroup(l);
  //controlP5.addSlider("controlValue3",-1,1.5,0,110,150,10).setGroup(l);
  //controlP5.addSlider("controlValue4",-1,1.5,0,130,150,10).setGroup(l);
  //controlP5.addSlider("controlValue5",-1,1.5,0,150,150,10).setGroup(l);
}

void draw() {
  p = p + 1;
  background(bgColor);
  translate((width/2)+p,(height/2)+p);
  generateRadiusPoints();
  noFill();
  stroke(255);
  curveTightness(-0.3);
  beginShape();
    for (int j=0; j<numberOfPoints+3; j++) {
      if (j < numberOfPoints) {
        int rPoint = radiusPoint[j];
        curveVertex(sin((TWO_PI/numberOfPoints)*j)*rPoint,cos((TWO_PI/numberOfPoints)*j)*rPoint);
      } else {
        int rPoint = radiusPoint[(j-numberOfPoints)];
        curveVertex(sin((TWO_PI/numberOfPoints)*(j-numberOfPoints))*rPoint,
        cos((TWO_PI/numberOfPoints)*(j-numberOfPoints))*rPoint);
      }
    }
    endShape();
 
  //translate(-width/2,-height/2);
}

void keyPressed(){
  
  
}

String timestamp() {
  return "hola";
}

void generateRadiusPoints() {
  controlValueArray[0] = controlValue1;
  controlValueArray[1] = controlValue2;
  controlValueArray[2] = controlValue3;
  controlValueArray[3] = controlValue4;
  controlValueArray[4] = controlValue5;
 
    for (int j=0; j<numberOfPoints; j++) {
      radiusPoint[j] = int(outerRing-controlValueArray[j]*spaceBetween);
    }
}