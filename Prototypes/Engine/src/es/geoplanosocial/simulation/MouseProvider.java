package es.geoplanosocial.simulation;


import es.geoplanosocial.tracker.Blob;
import es.geoplanosocial.tracker.BlobsProvider;
import processing.core.PApplet;

import java.awt.*;

import static es.geoplanosocial.util.Constants.LEVEL_HEIGHT;
import static es.geoplanosocial.util.Constants.LEVEL_WIDTH;

/**
 * Simulates a provider by using the position of the mouse
 * Created by gbermejo on 14/05/17.
 */
public class MouseProvider implements BlobsProvider{

    private int numberOfPlayers = 0;
    private int sizeOfPlayers = 0;

    private final PApplet processing;

    public MouseProvider(PApplet processing, int numberOfPlayers, int sizeOfPlayers) {
        this.processing = processing;
        setSizeOfPlayers(sizeOfPlayers);
        setNumberOfPlayers(numberOfPlayers);
    }

    public int getSizeOfPlayers() {
        return sizeOfPlayers;
    }

    public void setSizeOfPlayers(int sizeOfPlayers) {
        this.sizeOfPlayers = sizeOfPlayers;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {

        this.numberOfPlayers = numberOfPlayers;
        if(numberOfPlayers<1)this.numberOfPlayers =1;
    }

    @Override
    public Blob[] fetchPositions() {
        Blob[] positions = new Blob[numberOfPlayers];
        switch (numberOfPlayers){
            case 5:
                positions[0]=createBlob(1, processing.mouseX, processing.mouseY);
                positions[1]=createBlob(2, 20, 20);
                positions[2]=createBlob(3, 30, 100);//Mirror x axis
                positions[3]=createBlob(4,170, 110);//Mirror y axis
                positions[4]=createBlob(5,50, 80);//Mirror y axis
            case 4:
                positions[0]=createBlob(1, processing.mouseX, processing.mouseY);
                positions[1]=createBlob(2, 20, 20);
                positions[2]=createBlob(3, 20, 100);//Mirror x axis
                positions[3]=createBlob(4,100, 100);//Mirror y axis

//                positions[1]=createBlob(2,LEVEL_WIDTH- processing.mouseX,LEVEL_HEIGHT- processing.mouseY);//Mirror both axes
//                positions[2]=createBlob(3, processing.mouseX,LEVEL_HEIGHT- processing.mouseY);//Mirror x axis
//                positions[3]=createBlob(4,LEVEL_WIDTH- processing.mouseX, processing.mouseY);//Mirror y axis
                break;
            case 3:
                positions[0]=createBlob(1, processing.mouseX, processing.mouseY);
                positions[1]=createBlob(2,LEVEL_WIDTH- processing.mouseX, processing.mouseY);//Mirror y axis
                positions[2]=createBlob(3,LEVEL_WIDTH/2,LEVEL_HEIGHT- processing.mouseY);//Mirror x axis and center
                break;
            case 2:
                positions[0]=createBlob(1, processing.mouseX, processing.mouseY);
                positions[1]=createBlob(2,LEVEL_WIDTH- processing.mouseX,LEVEL_HEIGHT- processing.mouseY);//Mirror both axes
                break;
            case 1:
                positions[0]=createBlob(1, processing.mouseX, processing.mouseY);
                break;
            default:

        }
        return positions;
    }

    private Blob createBlob(int id, int x, int y){
        return new Blob(String.valueOf(id), new Rectangle(x,y,sizeOfPlayers,sizeOfPlayers));
    }
}
