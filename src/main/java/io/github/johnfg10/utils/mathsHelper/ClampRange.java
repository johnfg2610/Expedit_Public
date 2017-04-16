package io.github.johnfg10.utils.mathsHelper;

/**
 * Created by johnfg10 on 30/03/2017.
 */
public class ClampRange {
    public static int clampRange(int aInt, int max, int min){
        if (aInt < min)
            aInt = min;
        else if (aInt > max)
            aInt = max;
        return aInt;
    }
}
