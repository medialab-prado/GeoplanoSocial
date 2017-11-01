package es.geoplanosocial.tracker;

import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Types;
import es.geoplanosocial.util.Utils;
import processing.core.PApplet;

import java.awt.*;
import java.util.ArrayList;

import static es.geoplanosocial.util.Constants.*;


/**
 * Layer between the game and the points provider.
 * Translates blobs into players.
 * Takes decisions on when level and world changes.
 * Created by gbermejo on 14/05/17.
 */
public class Tracker {

    //Singleton
    private static final Tracker ourInstance = new Tracker();

    public static Tracker getInstance() {
        return ourInstance;
    }

    private Tracker() {
    }


    private static ArrayList<Player> players;
    private static TrackerCallback trackerCallback;
    private static BlobsProvider blobsProvider;



    public static void init(ArrayList<Player> players, TrackerCallback trackerCallback, BlobsProvider blobsProvider) {
        Tracker.players = players;
        Tracker.trackerCallback = trackerCallback;
        Tracker.blobsProvider = blobsProvider;

    }

    public void update(){

        //Update players
        updatePlayers();

        //Check if there is a change of world
        checkWorldChange();

        //Check if there is a change of level
        //checkLevelChange();

    }


    private void updatePlayers() {
        //Get current elements
        Blob[] elementsTracked = blobsProvider.fetchPositions();

        //Update locations and spot missing players
        updateLocations(elementsTracked);

        //Add new players as ghosts
        addPlayers(elementsTracked);


    }

    private void updateLocations(Blob[] elementsTracked) {

        //Updates existing ones
        //FIXME maybe hashmap but nested iteration of small lists.
        for (int i = players.size()-1;i>=0; i--){//Iterate backwards since removing elements

            Player p = players.get(i);
            boolean present = false;

            for(int j=0;j<elementsTracked.length;j++){

                Blob b=elementsTracked[j];

                if (b!=null && b.getId().equals(p.getId()) && p.isVisible()){//Target player

                    //Update player
                    p.update(b.getBoundingBox());

                    //Consumed blob
                    elementsTracked[j]=null;

                    //Mark as updated
                    present=true;
                    break;

                }
            }

            //Player disappeared
            if(!present){
                switch (p.getState()){
                    case PLAYING:
                        p.setState(Player.State.MISSING);
                        p.setOutTime();
                        break;
                    case GHOST:
                        players.remove(p);
                        break;
                }
            }
        }
    }

    private void addPlayers(Blob[] elementsTracked) {

        ArrayList<Player> newPlayers= new ArrayList<>();
        //Add players
        for(Blob b : elementsTracked){
            if(b!=null) {
                newPlayers.add(Player.Factory.getPlayer(b));
            }
        }
        if(newPlayers.size() >0)
            trackerCallback.newPlayers(newPlayers);
    }


    private void checkWorldChange(){

        if(players.size()<1)return;

        //If missing one and one ghost, swap them
        swapMissingPlayers();

        //Check ghost timers
        checkGhostPlayers();

        //Check out timers
        checkMissingPlayers();

    }


    private void swapMissingPlayers(){
        for (int i = players.size()-1;i>=0; i--){//Iterate backwards since removing elements
            Player missingPlayer = players.get(i);
            if(missingPlayer.getState()==Player.State.MISSING){
                for(Player ghostPlayer : players){
                    if(ghostPlayer.getState()==Player.State.GHOST){
                        //Perform the swap
                        players.remove(ghostPlayer);
                        missingPlayer.setId(ghostPlayer.getId());
                        missingPlayer.setBoundingBox(ghostPlayer.getBoundingBox());
                        missingPlayer.resetBoundaryTime();
                        missingPlayer.resetOutTime();
                        missingPlayer.setState(Player.State.PLAYING);
                        break;
                    }
                }
            }
        }
    }


    private void checkGhostPlayers(){
        long minTimestamp=System.currentTimeMillis();

        int ghosts = 0;
        for (int i = 0;i<players.size(); i++){
            Player p = players.get(i);
            if(p.getState()==Player.State.GHOST){
                ghosts++;
                long creationTime=p.getCreationTime();
                minTimestamp=creationTime<minTimestamp?creationTime:minTimestamp;//Get longest ghost player
            }
        }

        long maxWaiting=System.currentTimeMillis()-minTimestamp;//Longest waiting time

        if(maxWaiting>=WORLD_CHANGE_TIMER_IN && ghosts>=players.size()){//All ghosts and time has passed
            Utils.log("More players");
            changeWorld();
        }

    }


    private void checkMissingPlayers(){
        long maxTimestamp=0;

        int missing =0;
        for (int i = 0;i<players.size(); i++){
            Player p = players.get(i);
            if(p.getState()==Player.State.MISSING){
                missing++;
                long outTime=p.getOutTime();
                maxTimestamp=outTime>maxTimestamp?outTime:maxTimestamp;
            }
        }
        long minWaiting=System.currentTimeMillis()-maxTimestamp;//Shortest waiting time

        if(minWaiting>=WORLD_CHANGE_TIMER_OUT && missing>=players.size()){//All became missing and time has passed
            Utils.log("Less players");
            changeWorld();
        }

    }


    private void changeWorld(){
        trackerCallback.changeWorld();
    }



    private Types.Direction randomDirection(){
        return Types.Direction.values()[Utils.randomInt(0,Types.Direction.values().length-1)];
    }
}
