package edu.iu.c212.programs;

import java.util.ArrayList;
import java.util.List;

public class SawPrimePlanks {

    /**
     * gets the plank lengths from the inventory
     * @param longPlankLength length of the uncut plank
     * @return the length of the cut planks
     */
    public static List<Integer> getPlankLengths(int longPlankLength){
        List<Integer> result = new ArrayList<>();
        int sawPlankLength = sawPlank(longPlankLength);
        for(int i=0; i<(longPlankLength/sawPlankLength); i++)
            result.add(sawPlankLength);
        return result;
    }

    /**
     * saws the planks into lengths of a prime number
     * @param plankLength length of the plank
     * @return length of the sawed plank
     */
    public static int sawPlank(int plankLength) {
        int tNumb = plankLength;
        if (tNumb <= 1) {
            return tNumb;
        } else {
            // Check if number is divisible by any number from 2 to its square root
            for (int i = 2; i <= Math.sqrt(tNumb); i++) {
                if (tNumb % i == 0) {
                    tNumb = tNumb/i;
                    i=1;
                }
            }
            return tNumb;
        }
    }
}
