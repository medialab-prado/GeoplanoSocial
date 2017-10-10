import processing.video.*;

Movie movie;

void setup() {
  size(640, 480);
  background(0);
  movie = new Movie(this, "geoPlano2.mp4");
  movie.loop();
}

void movieEvent(Movie m) {
  m.read();
}


float amount = 0.0f;

final float SCALE_MIN = 1.0f;
final float SCALE_MAX = 3.0f;

final float SCALE_FACTOR = 0.05f;

void draw() {

  amount += mousePressed ? SCALE_FACTOR : -SCALE_FACTOR;
  amount = constrain(amount, 0, 1.0);
  float s = lerp(SCALE_MIN, SCALE_MAX, amount);
  
  float scaleChange = s - SCALE_MIN;
  
  translate(-mouseX*scaleChange, -mouseY*scaleChange);
  scale(s);
  
  image(movie, 0, 0, width, height);
}