package es.geoplanosocial.tracker;

import es.geoplanosocial.factories.PlayerFactory;
import es.geoplanosocial.players.Player;
import processing.core.PGraphics;

import java.util.ArrayList;

/**
 * Created by gbermejo on 14/05/17.
 */
public class Tracker {

    //Singleton
    private static Tracker ourInstance = new Tracker();

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

}
