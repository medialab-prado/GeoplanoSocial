package es.geoplanosocial.players;

import processing.core.PGraphics;

/**
 * Created by gbermejo on 11/06/17.
 */
public abstract class VisiblePlayer extends Player{

    private int color;

    public VisiblePlayer(int color, Player player) {
        super(player.getId(), player.getBoundingBox());
        this.color=color;
    }

    public abstract void draw(PGraphics pg);

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
