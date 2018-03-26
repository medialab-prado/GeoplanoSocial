package es.geoplanosocial.games.maranya;


import java.awt.geom.Line2D;

/**
 * Created by guzman on 17/12/2017.
 */
public class Arc {
    private Vertex head;
    private Vertex tail;

    private Line2D line;

    private boolean glowing;

    public Arc(Vertex head, Vertex tail) {
        this.head = head;
        this.tail = tail;
        computeLine();
        this.glowing=false;
    }

    public Vertex getHead() {
        return head;
    }

    public Vertex getTail() {
        return tail;
    }

    public boolean isGlowing() {
        return glowing;
    }

    public void setGlowing(boolean glowing) {
        this.glowing = glowing;
    }

    public Line2D getLine() {
        return line;
    }

    private void computeLine(){
        line = new Line2D.Float(head.getPoint(),tail.getPoint());
    }

    public void update(){
        if(head.isMagnetic() || tail.isMagnetic())computeLine();
    }

    public static boolean isCollision(Arc arc1, Arc arc2){
        return arc1.getLine().intersectsLine(arc2.getLine());
    }
}
