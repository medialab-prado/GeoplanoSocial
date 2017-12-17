package es.geoplanosocial;

import processing.core.PApplet;

public class Main {

    public static final Object lock = new Object();

    public static void main(String[] args) {

        boolean running = true;

                while(running){
                    System.out.println("Launching GeoPlano Social");
                    PApplet.main("es.geoplanosocial.engine.Engine");
                    try{
                        synchronized (lock) {
                            lock.wait();
                        }
                    }catch(InterruptedException e){
                        System.err.println("Killing GeoPlano Social, we'll see you next time");
                        running=false;
                    }
                }


    }
}
