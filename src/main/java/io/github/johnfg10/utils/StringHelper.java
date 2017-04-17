package io.github.johnfg10.utils;

/**
 * Created by johnfg10 on 17/04/2017.
 */
public class StringHelper {
    public static String arrayToString(String[] strings){
        String string = "";
        for (String st:strings) {
            string = string + " " + st;
        }
        return string;
    }
}
