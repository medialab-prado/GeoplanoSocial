package es.geoplanosocial.players;

import es.geoplanosocial.tracker.Blob;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

import java.awt.*;
import java.util.ArrayList;

/**
 * Generic player
 * Created by gbermejo on 14/05/17.
 */
public class Player {

    public enum Type {
        NODE,
        EXTENDED_NODE,
        SQUARE,
        GLOW_NODE
    }

    public enum State {
        GHOST,
        PLAYING,
        MISSING
    }

    private String id;
    private Rectangle boundingBox;

    private State state;

    private long creationTime;
    private long boundaryTime;//On boundaries of play area
    private long outTime;//Out of play area

    public Player(String id, Rectangle position) {
        setId(id);
        setBoundingBox(position);

        this.creationTime = System.currentTimeMillis();

        resetBoundaryTime();
        resetOutTime();

        state=State.GHOST;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public Rectangle getCenteredBoundingBox() {
        return new Rectangle((int)(boundingBox.getCenterX()-boundingBox.width),(int)(boundingBox.getCenterY()-boundingBox.height),boundingBox.width,boundingBox.height);
    }

    public void setBoundingBox(Rectangle boundingBox) {
        this.boundingBox = boundingBox;
    }

    public long getCreationTime() {
        return creationTime;
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

    public boolean isPlaying(){
        return this.state==State.PLAYING;
    }

    public boolean isSurrounded( ArrayList<Player> players) {
        Polygon enclosure = new Polygon();
        for(Player p: players){
            if(p.isVisible() && p!=this){
                enclosure.addPoint(p.getBoundingBox().x, p.getBoundingBox().y);
            }
        }
        return enclosure.contains(this.getBoundingBox());
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



    /**
     * Factory for generating players.
     * Created by gbermejo on 20/05/17.
     */
    public static class Factory{
        public static Player getPlayer(Blob blob){
            return new Player(blob.getId(), blob.getBoundingBox());
        }

        public static Player getPlayer(Type type, int color, Player player) {
            return getPlayer(type,color,player,null);
        }

        public static Player getPlayer(Type type, int color, Player player, PApplet processing){

            Player p;

            switch (type){
                case NODE:
                    p=new Node(color,player);
                    break;
                case EXTENDED_NODE:
                    p=new ExtendedNode(color,player);
                    break;
                case SQUARE:
                    p=new Square(color,player);
                    break;
                case GLOW_NODE:
                    p= new GlowNode(color, player, processing);
                    break;
                default:
                    p=getPlayer(new Blob(player.getId(), player.getBoundingBox()));
            }
            p.setState(player.getState());
            return p;
        }
    }
}
