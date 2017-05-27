package es.geoplanosocial.players;

import processing.core.PConstants;
import processing.core.PGraphics;

import java.awt.*;

/**
 * Abstract generic player
 * Created by gbermejo on 14/05/17.
 */
public abstract class Player {

    public enum State {
        GHOST,
        PLAYING,
        MISSING
    }

    private String id;
    private Rectangle boundingBox;

    private State state;

    private long boundaryTime;//On boundaries of play area
    private long outTime;//Out of play area

    public Player(String id, Rectangle position) {
        setId(id);
        setBoundingBox(position);

        resetBoundaryTime();
        resetOutTime();

        state=State.GHOST;
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    private void setBoundingBox(Rectangle boundingBox) {
        this.boundingBox = boundingBox;
    }


    public long getBoundaryTime() {
        return boundaryTime;
    }

    public void setBoundaryTime() {
        this.boundaryTime = System.currentTimeMillis();
    }

    public void resetBoundaryTime() {
        this.boundaryTime = 0;
    }


    public long getOutTime() {
        return outTime;
    }

    public void setOutTime() {
        this.outTime = System.currentTimeMillis();
    }

    public void resetOutTime() {
        this.outTime = 0;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }


    public boolean isVisible(){
        return this.state==State.GHOST || this.state==State.PLAYING;
    }

    public Point getLocation() {
        return boundingBox.getLocation();
    }


    public void update(Rectangle current){
        setBoundingBox(current);
    }

    public PGraphics debug(PGraphics pg){
        pg.beginDraw();
        pg.textSize(5);
        pg.textAlign(PConstants.CENTER, PConstants.CENTER);
        pg.text(id, boundingBox.x, boundingBox.y);

        pg.endDraw();
        return pg;
    }

    public abstract void draw(PGraphics pg);

}
