package es.geoplanosocial.players;

import es.geoplanosocial.levels.world1.a.Node1A;
import es.geoplanosocial.levels.world2.a.Node2A;
import es.geoplanosocial.levels.world2.b.Node2B;
import es.geoplanosocial.tracker.Blob;
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
        SQUARE,
        NODE1A,
        NODE2A,
        NODE2B
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

        public static Player getPlayer(Player.Type type, int color, Player player){

            Player p;

            switch (type){
                case NODE:
                    p=new Node(color,player);
                    break;
                case SQUARE:
                    p=new Square(color,player);
                    break;
                case NODE1A:
                    p=new Node1A(color,player);
                    break;
                case NODE2A:
                    p=new Node2A(color, player);
                    break;
                case NODE2B:
                    p=new Node2B(false, color, player);
                    break;
                default:
                    p=getPlayer(new Blob(player.getId(), player.getBoundingBox()));
            }
            p.setState(player.getState());
            return p;
        }
    }
}
