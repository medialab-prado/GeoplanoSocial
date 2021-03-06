package es.geoplanosocial.util;

import static es.geoplanosocial.util.Utils.randomInt;

/**
 * Global Enums
 * Created by gbermejo on 19/04/17.
 */
public class Types {


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

        public static Direction getRandom() {
            return values()[randomInt(0, values().length-1)];
        }

    }
}
