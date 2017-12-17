package es.geoplanosocial;

import processing.core.PApplet;

/**
 * Created by guzman on 16/12/2017.
 */
public class DisposeHandler {

    public DisposeHandler(PApplet pa) {
        pa.registerMethod("dispose", this);
    }

    public void dispose() {
        System.out.println("Closing sketch");
        synchronized (Main.lock) {
            Main.lock.notify();
        }
    }
}