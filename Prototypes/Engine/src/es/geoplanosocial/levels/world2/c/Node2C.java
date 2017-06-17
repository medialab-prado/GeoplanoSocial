package es.geoplanosocial.levels.world2.c;

import es.geoplanosocial.players.Node;
import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Utils;
import processing.core.PApplet;
import processing.core.PVector;

/**
 * Node with an energy component
 * Created by gbermejo on 14/06/17.
 */
public class Node2C extends Node{
    private static final float TRANSFER_COEFF = 0.25f;
    private static final float MIN_ENERGY = 2.5f;

    private float energy;//Also represents node radius
    private boolean giving;//If is the one giving energy


    public Node2C(boolean giving, int color, Player player) {

        super(color, player);

        this.giving = giving;
        setEnergy(getBoundingBox().width/2.0f);
    }


    //Checks and transfers energy if needed
    public void checkTransference(Node2C p) {
        if(Utils.isCircleCollision(p.getLocation(),p.energy,this.getLocation(),energy)){//Collision between nodes
            transfer();
            p.transfer();
            if (energy <= MIN_ENERGY) {
                energy = MIN_ENERGY;
                giving = false;
                p.setGiving(true);
            } else if (p.getEnergy() <= MIN_ENERGY) {
                p.setEnergy(MIN_ENERGY);
                p.setGiving(false);
                giving = true;
            }
        }
    }

    //Transfers energy
    //XXX done a linear interchange maybe change to exponential depending on distance?
    public void transfer() {
        int sign = giving ? -1 : 1;
        this.setEnergy(getEnergy() + sign * TRANSFER_COEFF);
    }


    //Getters and setters
    public float getEnergy() {
        return energy;
    }


    public void setGiving(boolean giving) {
        this.giving = giving;
    }

    public void setEnergy(float energy) {
        this.energy = energy;
        int e = Math.round(this.energy*2);
        this.getBoundingBox().setSize(e, e);
    }
}
