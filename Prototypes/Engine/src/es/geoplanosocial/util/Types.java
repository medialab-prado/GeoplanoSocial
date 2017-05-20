package es.geoplanosocial.util;

/**
 * Global Enums
 * Created by gbermejo on 19/04/17.
 */
public class Types {
    public enum Axis {
        X,
        Y,
        Z
    }
    public enum Level {
        A,
        B,
        C
    }
    public enum Player {
        NODE
    }

    public enum Direction {
        UP(0),
        DOWN(1),
        LEFT(2),
        RIGHT(3);

        private final int number;

        Direction(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }
    }
}
