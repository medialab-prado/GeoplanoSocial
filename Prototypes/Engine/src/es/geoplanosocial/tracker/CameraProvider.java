package es.geoplanosocial.tracker;

import es.geoplanosocial.util.Utils;
import oscP5.OscEventListener;
import oscP5.OscMessage;
import oscP5.OscP5;
import oscP5.OscStatus;
import processing.core.PApplet;

import java.awt.*;
import java.util.ArrayList;

import static es.geoplanosocial.util.Constants.*;
import static es.geoplanosocial.util.Configuration.*;

/**
 * Gets tracking info by OSC from an external program (sensor4games).
 * Created by gbermejo on 26/06/2017.
 */
public class CameraProvider implements BlobsProvider, OscEventListener {

    private OscP5 oscP5;
    private ArrayList<Blob> players;


    public CameraProvider() {
        oscP5 = new OscP5(this, OSC_PORT);
        oscP5.addListener(this);
        this.players = new ArrayList<>();
    }

    @Override
    public Blob[] fetchPositions() {
        return players.toArray(new Blob[players.size()]);
    }

    @Override
    public void oscEvent(OscMessage oscMessage) {
        //Utils.log("oscEvent: "+oscMessage);

        if (oscMessage.checkAddrPattern("/GameBlobAllIn")) {
            int numberOfPlayers = oscMessage.get(0).intValue();

            //Blob[] positions = new Blob[numberOfPlayers];

            //FIXME we do not want to be continuously clearing and recreating
            players.clear();

            //Read OSC info
            for (int i = 0; i< numberOfPlayers; i++) {
                try{
                    float x = oscMessage.get(1+i*4+0).floatValue(); // X position [0..1]
                    float y = oscMessage.get(1+i*4+1).floatValue();  // Y position [0..1]
                    int id = oscMessage.get(1+i*4+2).intValue();
                    //int time = oscMessage.get(1+i*4+3).intValue();
                    players.add(createBlob(id, x, y));

                }catch (Exception e){
                    //Utils.log("Error parsing OSC");
                }


            }
        }
    }

    @Override
    public void oscStatus(OscStatus oscStatus) {
        Utils.log("oscStatus: "+oscStatus);
    }


    private Blob createBlob(int id, float x, float y) {
        //Utils.log("Player["+id+"] -> ("+x*LEVEL_WIDTH+", "+y*LEVEL_HEIGHT+")");
        return new Blob(String.valueOf(id), new Rectangle(PApplet.round(x*LEVEL_WIDTH), PApplet.round(y*LEVEL_HEIGHT), playerSize, playerSize));
    }
}
