// the bot moves and pushes a object to a target
//
// http://forum.processing.org/topic/pvector-orintation-robot
// or http://forum.processing.org/topic/help-on-angle-stuff-sumo-bot-orintation-towards-object-to-push
// the bot moves and pushes a object to a target
Bot myBot = new Bot();
PVector targetPos = new PVector(0, 0);
PVector objectPos = new PVector(0, 0);
//
final float distToPush = 77;
void setup () {
  size(800, 800);
  targetPos.x=200;
  targetPos.y=200; 
  objectPos.x=600;
  objectPos.y=600;
  myBot.botPos.x=400;
  myBot.botPos.y=600;
} // func
void draw () {
  background(0);
  // blue target
  fill(0, 0, 255);
  rect(targetPos.x-6, targetPos.y-6, 6, 6); // or ellipse
  text("target", targetPos.x-17, targetPos.y-12);
  // draw bot
  myBot.drawBot() ;
  // green object
  fill(0, 255, 0);
  ellipse(objectPos.x, objectPos.y, 11, 11);
  // check target
  if (objectPos.dist(targetPos)< 4.1) {
    fill(255, 255, 0);
    text ("Target reached.", width-200, 200);
  }
  // move bot
  myBot.exec(); //<>//
} // func
//
// =============================================================
class Bot {
  // robot / bot pos
  PVector botPos = new PVector(0, 0);
  // bot direction given by botAngle 
  float botAngle = 180;
  // states
  final int botStatePushObject=3;
  // the state for movement
  int botState=0;
  void drawBot() {
    // draw bot in red
    fill(255, 0, 0);
    ellipse(botPos.x, botPos.y, 11, 11);
    // show bot direction given by botAngle 
    // as a fork at his front
    fill(0);
    stroke(255);
    PVector directionPos = new PVector(0, 0);
    directionPos.x= botPos.x +  cos(radians(botAngle-20)) * 17;
    directionPos.y= botPos.y +  sin(radians(botAngle-20)) * 17;
    line(botPos.x, botPos.y,
    directionPos.x, directionPos.y);
    directionPos.x= botPos.x +  cos(radians(botAngle+20)) * 17;
    directionPos.y= botPos.y +  sin(radians(botAngle+20)) * 17;
    line(botPos.x, botPos.y,
    directionPos.x, directionPos.y);
    noStroke();
  } // method
  void exec () {
    println(botState);
    // depending on botState he is
    // doing his actions
    float angle;
    float dx, dy;
    switch(botState) {
    case 0:
      // turn before drive
      PVector gotoPos = new PVector(0, 0); // robot go to target //<>//
      dx=objectPos.x-targetPos.x;
      dy=objectPos.y-targetPos.y; 
      gotoPos.x= objectPos.x +  dx*.1;
      gotoPos.y= objectPos.y +  dy*.1;
      // green
      fill(0, 255, 188);
      ellipse(gotoPos.x, gotoPos.y, 3, 3);     
      if (turnTowardsObject(gotoPos, botPos)) {
        botState=1;
      }     
      break;
    case 1:
      // drive
      gotoPos = new PVector(0, 0); // robot go to point to start pushing from //<>//
      dx=objectPos.x-targetPos.x;
      dy=objectPos.y-targetPos.y; 
      gotoPos.x= objectPos.x +  dx*.1;
      gotoPos.y= objectPos.y +  dy*.1;
      // green
      fill(0, 255, 188);
      ellipse(gotoPos.x, gotoPos.y, 3, 3);
      if (driveTo(gotoPos, false)) {
        botState=2;
      }
      break;
    case 2:
      // turn towards object
      if (turnTowardsObject  (objectPos, botPos)) { //<>//
        botState=botStatePushObject;
      }
      break;
    case botStatePushObject:
      if (driveTo(objectPos, false)) //<>//
        botState = 4;
      break;
    case 4:     
      if (driveTo(targetPos, true)) //<>//
        botState = 5;
      break;
    case 5:
      println("Target reached."); //<>//
      botState = 6;
      break;
    case 6:
      PVector centerOfScreen=new PVector(width/2, height/2); //<>//
      if (driveTo(centerOfScreen, false))
        botState = 7;
      break; 
    case 7:
      PVector centerOfScreenTop=new PVector(width/2, 30); //<>//
      if (turnTowardsObject(centerOfScreenTop, botPos))
      {
        botState=8;
      }     
      break;
    case 8:
      //do nothing
      break;
    default:
      // error
      println("Unknown state +++++++++++++++++++");
      break;
    } // switch
    //
  } // method
  boolean driveTo(PVector targetPos, boolean isObjectToPush)
  {
    PVector whatToAdd = new PVector(0, 0);
    // drive
    whatToAdd.x=(targetPos.x - botPos.x) *.01;
    whatToAdd.y=(targetPos.y - botPos.y) *.01;
    if (whatToAdd.x < 0 ) {
      if (whatToAdd.x > -0.1 ) {
        whatToAdd.x = -0.1 ;
      }
    }
    else
    {
      if (whatToAdd.x < 0.1 ) {
        whatToAdd.x = 0.1 ;
      }
    }
    if (whatToAdd.y < 0 ) {
      if (whatToAdd.y > -0.1 ) {
        whatToAdd.y = -0.1 ;
      }
    }
    else
    {
      if (whatToAdd.y < 0.1 ) {
        whatToAdd.y = 0.1 ;
      }
    }
    //
    botPos.x+= whatToAdd.x ;
    botPos.y+= whatToAdd.y ;
    if (isObjectToPush) {
      if (botPos.dist (objectPos)<11.4) {
        objectPos.x+= whatToAdd.x ;
        objectPos.y+= whatToAdd.y ;
      }
      if (botPos.dist (targetPos)<11.0) {
        return true;
      }
      else
      {
        return false;
      }
    }
    else
    {
      if (botPos.dist(targetPos)<8.2) {
        return true;
      }
      else
      {
        return false;
      }
    }
  } // method
  boolean turnTowardsObject(PVector gotoPos, PVector botPos)
  {
    float angle;
    angle = circularAngleBetween(gotoPos, botPos);
    if (  botAngle < degrees (angle) ) {
      botAngle++;
    }
    else if (  botAngle > degrees (angle) ) {
      botAngle--;
    } 
    //
    angle = circularAngleBetween( gotoPos, botPos );
    if (round(botAngle) == round(degrees(angle)))
    {
      return true;
    }
    else
    {
      return false;
    }
  }
  // tools within class
  float circularAngleBetween (PVector myPVector1, PVector myPVector2) {
    // from http://forum.processing.org/topic/pvector-anglebetween
    // with thanks.
    // delivers an angle in rad where myPVector1 is considered as a
    // point being the center of a circle and myPVector2 a point being on the
    // circumference.
    //
    float a = atan2(myPVector1.y-myPVector2.y, myPVector1.x-myPVector2.x);
    if (a<0) {
      a+=TWO_PI;
    }
    return a;
  } // func
  //
} // class