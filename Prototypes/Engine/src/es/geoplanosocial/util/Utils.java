package es.geoplanosocial.util;

/**
 * Utility methods
 * Created by gbermejo on 19/04/17.
 */
class Utils {


    static int color(int r, int g, int b) {
        if (r > 255) r = 255;
        else if (r < 0) r = 0;
        if (g > 255) g = 255;
        else if (g < 0) g = 0;
        if (b > 255) b = 255;
        else if (b < 0) b = 0;

        return 0xff000000 | (r << 16) | (g << 8) | b;
    }

}
