package es.geoplanosocial.games.maranya;

import java.awt.geom.Point2D;

/**
 * Created by guzman on 17/12/2017.
 */
public class Vertex {

    private static final long WAIT_INTERVAL = 4000; //Millis

    private Point2D.Float point;
    private boolean glowing;
    private boolean magnetic;

    private Long timer =null;

    public Vertex(float x, float y) {
        this.point = new Point2D.Float(x, y);
        setMagnetic(false);
    }

    public Point2D.Float getPoint() {
        return point;
    }

    public void setPoint(float x, float y) {
        setX(x);
        setY(y);
    }

    public void setX(float x) {
        this.point.x = x;
    }

    public void setY(float y) {
        this.point.y = y;
    }

    public boolean isGlowing() {
        return glowing;
    }

    public void setGlowing(boolean glowing) {
        this.glowing = glowing;
    }

    public boolean isMagnetic() {
        return magnetic;
    }

    public void setMagnetic(boolean magnetic) {
        this.magnetic = magnetic;
        //setGlowing(magnetic);
    }

    public void setTimer(Long timer) {
        if(isMagnetic()){
            if(this.timer==null)this.timer = timer;
            if(timer==null)this.timer=null;
        }
        else this.timer =null;
    }

    public void update(){
        if(isMagnetic()&&timer!=null){
            long waiting=System.currentTimeMillis()-timer;
            if(waiting>=WAIT_INTERVAL){
                setMagnetic(false);
            }
        }
    }

}
