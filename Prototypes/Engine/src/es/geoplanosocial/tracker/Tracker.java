package es.geoplanosocial.tracker;

import es.geoplanosocial.factories.PlayerFactory;
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

        //Check if there is a change of players
        checkPlayersChange();

        //Check if there is a change of level
        checkLevelChange();

    }

    private void checkPlayersChange() {
        //Get current elements
        Blob[] elementsTracked = blobsProvider.fetchPositions();

        boolean addedPlayers=false;
        boolean removedPlayers=false;

        //Updates existing ones
        for (int i = players.size()-1;i>=0; i--){//Iterate backwards since removing elements

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
            if(!present){
                players.remove(p);
                removedPlayers=true;
            }
        }



        //Add players
        for(Blob b : elementsTracked){
            if(b!=null) {
                players.add(PlayerFactory.getPlayer(b));
                addedPlayers=true;
            }
        }

        //Check if players changed
        //FIXME maybe detect additions AND not OR deletions
        if(addedPlayers){
            trackerCallback.morePlayers();
        }else if(removedPlayers){
            trackerCallback.lessPlayers();
        }
    }



    private void checkLevelChange(){
        System.currentTimeMillis();

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
