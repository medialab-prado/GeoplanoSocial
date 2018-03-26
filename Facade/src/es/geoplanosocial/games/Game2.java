package es.geoplanosocial.games;

import es.geoplanosocial.games.maranya.Arc;
import es.geoplanosocial.games.maranya.Maranya;
import es.geoplanosocial.games.maranya.Vertex;
import es.geoplanosocial.players.GlowNode;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.*;
import es.geoplanosocial.util.Color;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Default game layout
 * Created by guzman on 01/11/2017.
 */
public class Game2 extends Game {
    private static final String TITLE="Maraña bis";

    private static Maranya maranya = null;

    private static final double FRAME_INCREMENT = 3*Math.PI/180;
    private static double frame = 0;

    protected Game2() {
        super(TITLE);
    }

    @Override
    public void update() {

        ArrayList<GlowNode> actualPlayers = new ArrayList<>();

        for(Player p : players) {
            if (p.getState() == Player.State.PLAYING && p instanceof GlowNode) actualPlayers.add((GlowNode) p);
        }

        maranya.update(actualPlayers);

        if(maranya.toNextLevel())nextLevel();

        frame+=FRAME_INCREMENT;

    }

    @Override
    protected void draw() {
        boolean isResolved = maranya.isResolved();

        Arc[] arcs = maranya.getArcs();

        for(Arc arc:arcs){
            Vertex head = arc.getHead();
            Vertex tail = arc.getTail();
            int color = arc.isGlowing()?Color.WHITE:Color.GREY;
            GlowLine.setColor(color);
            GlowLine.lineG(head.getPoint().x, head.getPoint().y, tail.getPoint().x, tail.getPoint().y,arc.isGlowing()||isResolved);
        }

        Vertex[] vertices = maranya.getVertices();
        HashSet<Vertex> untangled=  maranya.getUntangledVertices();

        for(Vertex vertex:vertices){

            float glowExtension = GlowEllipse.DEFAULT_GLOW_EXTENSION;
            boolean isGlowing=false;
            int color = Color.GREY;

            if(untangled.contains(vertex)) {
                color = Color.WHITE;
                isGlowing = true;
            }

            if(vertex.isMagnetic()&&!isResolved) {
                color = (int)PApplet.map((float)Math.sin(frame),-1,1,127, 255);
                color = Utils.color(color, color, color);
                glowExtension = PApplet.map((float)Math.sin(frame),-1,1,1, GlowEllipse.DEFAULT_GLOW_EXTENSION);
                isGlowing = true;
            }

            GlowEllipse.setColor(color);
            GlowEllipse.setGlowExtension(glowExtension);
            GlowEllipse.ellipseG(vertex.getPoint().x, vertex.getPoint().y, Constants.VERTEX_NORMAL_RADIO,isGlowing||isResolved);
        }
    }

    @Override
    protected void setup() {

        maranya = new Maranya(4+getCurrentLevel()/3+players.size()/2,1+getCurrentLevel()/10+players.size()/3);
                                            //4 minimum + each 3 levels 1 more + 0-3 based on number of players
                                                                                                //4 minimum + each 10 levels 1 more + 0-2 based on number of players

        GlowLine.setPg(pg);
        GlowLine.setProcessing(processing);
        GlowLine.setColor(Color.WHITE);

        GlowEllipse.setPg(pg);
        GlowEllipse.setProcessing(processing);

        setDrawPlayersFront(true);

        frame =0;
    }

    @Override
    protected ArrayList<Player> setupPlayers() {
        ArrayList<Player> players=new ArrayList<>();
        for (Player p : Game.players){
            GlowNode pl =(GlowNode) Player.Factory.getPlayer(Player.Type.GLOW_NODE, Color.W1_ORANGE_NODE, p, processing);
            players.add(pl);
        }
        return players;
    }


    @Override
    public void addPlayers(ArrayList<Player> newPlayers) {
        for (Player p : newPlayers){
            Game.players.add(Player.Factory.getPlayer(Player.Type.NODE, Color.W1_ORANGE_NODE_ALPHA, p));
        }

    }
}
