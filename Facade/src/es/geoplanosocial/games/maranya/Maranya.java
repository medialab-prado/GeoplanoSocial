package es.geoplanosocial.games.maranya;

import es.geoplanosocial.players.GlowNode;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import static es.geoplanosocial.util.Constants.*;
import static es.geoplanosocial.util.Utils.randomInt;

/**
 * Created by guzman on 17/12/2017.
 */
public class Maranya {

    private static final int MIN_VERTICES = 4;
    private static final int MAX_VERTICES = 16;

    private static final int MIN_ACTIVE = 1;
    private static final int MAX_ACTIVE = 4;


    private static final int MIN_GENERATE_DIST = 13;

    private Vertex[] vertices = null;
    private Arc [] arcs = null;

    private int numberActive=1;

    public Maranya(int numberVertices, int numberActive) {

        //Sanity checks
        numberVertices=numberVertices>=MIN_VERTICES?numberVertices:MIN_VERTICES;
        numberVertices=numberVertices<MAX_VERTICES?numberVertices:MAX_VERTICES;


        numberActive = numberActive>=MIN_ACTIVE?numberActive:MIN_ACTIVE;
        numberActive = numberActive<MAX_ACTIVE?numberActive:MAX_ACTIVE;

        this.numberActive =numberActive;

        vertices = new Vertex[numberVertices];
        arcs = new Arc[vertices.length - 1];//n-1

        //Create tangled mess
        boolean tangled=false;
        while (!tangled) {
            //Create vertices
            int index = 0;
            while(index < vertices.length){
                int x = randomInt(PLAY_AREA_OFFSET_X, LEVEL_WIDTH-PLAY_AREA_OFFSET_X*2);
                int y =  randomInt(PLAY_AREA_OFFSET_Y, LEVEL_HEIGHT-PLAY_AREA_OFFSET_Y*2);
                Vertex candidate = new Vertex(x,y);

                boolean suitable=true;
                for(int j=0;j<index;j++){
                    Vertex v = vertices[j];
                    double distance = candidate.getPoint().distance(v.getPoint());
                    if(distance<MIN_GENERATE_DIST){
                        suitable=false;
                        break;
                    }
                }
                if(suitable)vertices[index++]=candidate;
            }

            //Create arcs
            for (int i = 0; i < arcs.length; i++) {
                arcs[i] = new Arc(vertices[i], vertices[i + 1]);
            }

            //Check vertices-arcs distance
            boolean suitable = true;
            for(int i = 0; i < arcs.length && suitable; i++){
                Arc arc = arcs[i];
                Vertex head=arc.getHead();
                Vertex tail = arc.getTail();
                for(Vertex vertex: vertices){
                    if(vertex!=head && vertex!=tail && arc.getLine().ptSegDist(vertex.getPoint())<MIN_GENERATE_DIST) {
                        suitable = false;
                        break;
                    }
                }
            }

            //Finish if tangled
            tangled = suitable && getTangledArcs().size()>0;

        }

    }

    private void setMagnetic() {
        HashSet<Arc> tangledArcs = getTangledArcs();
        if(tangledArcs.size()>0) {
            //Detect candidates
            HashSet<Vertex>  magneticCandidates= new HashSet<>();
            for(Arc arc : tangledArcs){
                Vertex head = arc.getHead();
                Vertex tail = arc.getTail();
                if(!head.isMagnetic())magneticCandidates.add(head);
                if(!tail.isMagnetic())magneticCandidates.add(tail);
            }

            while(magneticCandidates.size()>0 && getMagneticVertices().size()<numberActive){
                //Select one of the candidates
                int selected = new Random().nextInt(magneticCandidates.size());
                int i = 0;
                for(Vertex v : magneticCandidates) {
                    if (i == selected) {
                        v.setMagnetic(true);
                        magneticCandidates.remove(v);
                        break;
                    }
                    i++;
                }
            }
        }
    }

    public Vertex[] getVertices() {
        return vertices;
    }

    public Arc[] getArcs() {
        return arcs;
    }

    public void update(ArrayList<GlowNode> actualPlayers) {
        //TODO

        HashSet<Vertex> magneticVertices = getMagneticVertices();

        HashSet<Vertex> movedVertices = new HashSet<>();

        //Move vertices
        for(GlowNode player : actualPlayers) {
            boolean isCarrying = false;
            for (Vertex vertex : magneticVertices) {
                Rectangle r =player.getCenteredBoundingBox();
                if(vertex.isMagnetic() && r.contains(vertex.getPoint())){
                    vertex.setPoint((float) r.getCenterX(),(float)r.getCenterY());
                    magneticVertices.remove(vertex);
                    movedVertices.add(vertex);
                    isCarrying=true;
                    break;
                }
            }
            player.setGlowing(isCarrying);
        }




        //Check if untangled and set glowing
        for (Arc arc: arcs){

            HashSet<Arc> tangledArcs = getTangledArcs();
            boolean before = tangledArcs.contains(arc);

            //Update aka set new line if moved
            arc.update();

            tangledArcs = getTangledArcs();
            boolean after = tangledArcs.contains(arc);

            //If state changed set timer
            if(before!=after){
                Long time = System.currentTimeMillis();
                if(after){
                    time=null; //Returned to tangle state
                }
                arc.getHead().setTimer(time);
                arc.getTail().setTimer(time);
            }

            /*
            if(tangledArcs.size()<=0) {
                arc.setGlowing(true);
                arc.getHead().setGlowing(true);
                arc.getTail().setGlowing(true);
            }
            */

            arc.setGlowing(!after);
            //arc.getTail().setGlowing(arc.isGlowing());
            //arc.getHead().setGlowing(arc.isGlowing());

        }



        //Drop nodes
        for (Vertex vertex: vertices){
            vertex.update();
        }


        /*
        //Set glowing nodes
        for (Arc arc: arcs){
            if(arc.isGlowing()){
                arc.getHead().setGlowing(true);
                arc.getTail().setGlowing(true);
            }
        }

        //Set not glowing nodes
        for (Arc arc: arcs){
            if(!arc.isGlowing()){
                arc.getHead().setGlowing(false);
                arc.getTail().setGlowing(false);
            }
        }
        */

        if(isResolved()){
            magneticVertices = getMagneticVertices();
            for(Vertex vertex:magneticVertices){
                vertex.setTimer(System.currentTimeMillis());
            }
        }

        //Set new magnetic vertices
        setMagnetic();
    }

    public boolean isResolved(){
        return getTangledArcs().size()<=0;
    }

    public boolean toNextLevel(){
        return isResolved() && getMagneticVertices().size()<=0;
    }

    private HashSet<Arc> getTangledArcs(){
        HashSet<Arc> tangled= new HashSet<>();
        for(int i=0;i<arcs.length;i++){
            Arc a1 = arcs[i];
            for(int j=i+2;j<arcs.length;j++){//Adjacent arc always collides in the intersection point so +2
                Arc a2 = arcs[j];
                if(Arc.isCollision(a1,a2)){
                    tangled.add(a1);
                    tangled.add(a2);
                }
            }
        }
        return tangled;
    }

    private HashSet<Vertex> getMagneticVertices(){
        HashSet<Vertex>  magneticVertices= new HashSet<>();
        for(Vertex v : vertices){
            if(v.isMagnetic())magneticVertices.add(v);
        }
        return magneticVertices;
    }

    public HashSet<Vertex> getUntangledVertices(){
        HashSet<Vertex>  untangledVertices= new HashSet<>();
        for(Vertex vertex : vertices){
            for(Arc arc:arcs){
                if(arc.isGlowing()&& (arc.getHead()==vertex ||arc.getTail()==vertex))
                    untangledVertices.add(vertex);
            }
        }
        return untangledVertices;
    }
}
