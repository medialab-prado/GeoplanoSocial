package es.geoplanosocial.tracker;

import es.geoplanosocial.players.Player;
import es.geoplanosocial.util.Types;

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


    private static final Rectangle playArea = new Rectangle(PLAY_AREA_OFFSET,PLAY_AREA_OFFSET,LEVEL_WIDTH-2*PLAY_AREA_OFFSET,LEVEL_HEIGHT-2*PLAY_AREA_OFFSET);


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
        checkLevelChange();

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
        for (int i = 0;i<players.size(); i++){

            Player p = players.get(i);
            boolean present = false;

            for(int j=0;j<elementsTracked.length;j++){

                Blob b=elementsTracked[j];

                if (b!=null && b.getId().equals(p.getId())){//Target player

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
            if(!present && p.isVisible()){
                p.setState(Player.State.MISSING);
                p.setOutTime();
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
        trackerCallback.newPlayers(newPlayers);
    }


    private void checkWorldChange(){

        //If missing one and one ghost, swap them
        swapMissingPlayers();

        //FIXME maybe detect additions OR not AND deletions

        //Check ghost timers
        checkGhostPlayers();

        //Check out timers
        checkMissingPlayers();

    }


    private void swapMissingPlayers(){
        for (int i = players.size()-1;i>=0; i--){//Iterate backwards since removing elements
            Player missingPlayer = players.get(i);
            if(missingPlayer.getState()==Player.State.MISSING){
                for(int j=0;j<i;j++){
                    Player ghostPlayer = players.get(j);
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

        for (int i = 0;i<players.size(); i++){
            Player p = players.get(i);
            if(p.getState()==Player.State.GHOST){
                long creationTime=p.getCreationTime();
                minTimestamp=creationTime<minTimestamp?creationTime:minTimestamp;//Get longest ghost player
            }
        }

        long maxWaiting=System.currentTimeMillis()-minTimestamp;//Longest waiting time

        if(maxWaiting>=WORLD_CHANGE_TIMER_IN){
            changeWorld();
        }

    }


    private void checkMissingPlayers(){
        long now = System.currentTimeMillis();

        boolean shouldChange = false;
        for (int i = 0;i<players.size(); i++){
            Player p = players.get(i);
            if(p.getState()==Player.State.MISSING){
                long outTime=p.getOutTime();
                if(now-outTime>=WORLD_CHANGE_TIMER_OUT){
                    //Just one player issues the change
                    shouldChange = true;
                    break;
                }
            }
        }

        if(shouldChange){
            changeWorld();
        }

    }

    private void changeWorld(){

        //Cleanup players
        for (int i = players.size()-1;i>=0; i--){//Iterate backwards since removing elements
            Player p = players.get(i);

            //Set all ghosts as playing
            if(p.getState()==Player.State.GHOST){
                p.setState(Player.State.PLAYING);
            }

            //Remove all missing
            if(p.getState()==Player.State.MISSING){
                players.remove(p);
            }
        }


        trackerCallback.changeWorld();
    }




    private void checkLevelChange(){

        int wantToChange=0;

        long minTimestamp=System.currentTimeMillis();

        for (Player p : players){
            if (!playArea.contains(p.getLocation())){
                wantToChange++;
                long boundaryTime=p.getBoundaryTime();
                if(boundaryTime==0)p.setBoundaryTime();//Start timer
                else minTimestamp=boundaryTime<minTimestamp?boundaryTime:minTimestamp;//Get longest waiting player
            }else{
                if(p.getBoundaryTime()!=0)p.resetBoundaryTime();
            }
        }

        long maxWaiting=System.currentTimeMillis()-minTimestamp;//Longest waiting time

        //Utils.log("Level swap in: "+ (PLAY_AREA_TIMER-maxWaiting));

        float percentageChange= wantToChange/players.size();

        //Initial conditions for change of level
        if(percentageChange>=PLAY_AREA_MIN_PERCENTAGE && maxWaiting>=PLAY_AREA_TIMER*wantToChange){

            int[] directionCounter=new int[Types.Direction.values().length];

            //Players aiming each direction
            for (Player p : players){
                directionCounter[closestDirection(p).getNumber()]++;
            }

            boolean tie = false;
            int min =0;
            Types.Direction changeDirection=null;

            //Compute global maximum
            for(int i=0;i<directionCounter.length;i++){
                if(directionCounter[i]==min){
                    tie=true;
                }else if(directionCounter[i]>min){
                    changeDirection=Types.Direction.values()[i];
                    min=directionCounter[i];
                    tie=false;//Reset
                }
            }

            if(!tie){//There is a unique global maximum so change in that direction
                trackerCallback.changeLevel(changeDirection);
            }
        }



    }

    private Types.Direction closestDirection(Player player){

        Point location = player.getLocation();

        Point[] playAreaPoints = new Point[]{
                new Point(location.x,0),//UP
                new Point(location.x,LEVEL_HEIGHT),//DOWN
                new Point(0,location.y),//LEFT
                new Point(LEVEL_WIDTH,location.y)//RIGHT
        };

        double minDistance = Double.MAX_VALUE;
        Types.Direction direction = Types.Direction.RIGHT;

        for (int i =0; i<playAreaPoints.length;i++){
            Point reference = playAreaPoints[i];
            double tempDistance = reference.distance(location);
            if(tempDistance<minDistance){
                minDistance=tempDistance;
                direction= Types.Direction.values()[i];
            }
        }
        return direction;
    }
}
