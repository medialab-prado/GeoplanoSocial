Circle circle1;
Circle circle2;
int maxDistance;

void setup() {
  size(1000, 700);
  smooth();
  noCursor();//Ugly
  // create an array and fill it with circles
  circle1 = new Circle(0,0,50);
  circle2 = new Circle(0,0,50);
  
}

void draw() {
  // clear background
  background(0, 48, 70);
  
  circle1.move(mouseX, mouseY);
  circle2.move(width-mouseX, height-mouseY);
  circle1.display();
  circle2.display();
  float distancia = dist(circle1.x, circle1.y, circle2.x, circle2.y);
  stroke(255);
  //fill(255);
  float ancho = map(distancia, 0, width, 0, 10);
  ancho = constrain(ancho, 0, 10);
  strokeWeight(10-ancho);
  line(circle1.x, circle1.y, circle2.x, circle2.y);
 
}

class Circle {
  float x, y, dia;

  Circle(float x, float y, float dia) {
    move( x,  y);
    this.dia = dia;
  }

  void move(float x, float y) {
    this.x =x;
    this.y =y;

  }

  void display() {
    // code for drawing the circles
    noStroke();
    fill(60, 60, 190, 60);
    ellipse(x, y, dia, dia);
    
  }
}