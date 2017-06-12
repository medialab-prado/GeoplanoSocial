package es.geoplanosocial.simulation;


import es.geoplanosocial.tracker.Blob;
import es.geoplanosocial.tracker.BlobsProvider;
import es.geoplanosocial.util.Utils;
import processing.core.PApplet;

import java.awt.*;
import java.util.ArrayList;

import static es.geoplanosocial.util.Constants.*;
import static java.lang.Math.abs;

/**
 * Simulates a provider by using the position of the mouse. Moves one player at a time.
 * Created by gbermejo on 11/06/17.
 */
public class MouseSelectionProvider implements BlobsProvider {

    private ArrayList<Blob> players;
    private int selectedPlayer = 0;
    private int sizeOfPlayers = 0;

    private final PApplet processing;

    public MouseSelectionProvider(PApplet processing, int numberOfPlayers, int sizeOfPlayers) {
        this.processing = processing;
        this.players = new ArrayList<>();

        setSizeOfPlayers(sizeOfPlayers);
        setNumberOfPlayers(numberOfPlayers);
    }


    public void setSizeOfPlayers(int sizeOfPlayers) {
        this.sizeOfPlayers = sizeOfPlayers;
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    public void setNumberOfPlayers(int numberOfPlayers) {

        int difference = numberOfPlayers - players.size();

        if(difference>=0){
            //Add players or stay the same
            addPlayers(difference);
        }else{
            //Remove players
            removePlayer(abs(difference));
        }

    }

    private void addPlayers(int number){
        for(int i=0;i<number;i++)
            players.add(createBlob(players.size()+i, Utils.randomInt(0,LEVEL_WIDTH), Utils.randomInt(0,LEVEL_HEIGHT)));
    }

    private void removePlayer(int number){

        for(int i=0;i<number;i++) {
            if (players.size() > 1) {
                players.remove(players.size() - 1);
            }
        }

        setSelectedPlayer(selectedPlayer);
    }

    public void setSelectedPlayer(int selectedPlayer) {
        this.selectedPlayer = processing.constrain(selectedPlayer-1, 0, players.size()-1);
        Utils.log("Selected player: "+(this.selectedPlayer+1));
    }

    @Override
    public Blob[] fetchPositions() {

        //Update position of selected
        players.get(selectedPlayer).getBoundingBox().x=PApplet.constrain(processing.mouseX, 0, LEVEL_WIDTH);
        players.get(selectedPlayer).getBoundingBox().y=PApplet.constrain(processing.mouseY, 0, LEVEL_HEIGHT);

        //Return array with previous positions
        return players.toArray(new Blob[players.size()]);
    }

    private Blob createBlob(int id, int x, int y) {
        return new Blob(String.valueOf(id), new Rectangle(x, y, sizeOfPlayers, sizeOfPlayers));
    }
}
