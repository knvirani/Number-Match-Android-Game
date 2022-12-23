package com.fourshape.numbermatch.utils;

import java.util.Random;

public class RandNumber {
    
    public static int get (int min, int max) {
        return new Random().nextInt( (max - min) + 1 ) + min;
    }
    
}
