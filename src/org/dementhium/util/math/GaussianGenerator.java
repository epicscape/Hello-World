package org.dementhium.util.math;

import java.util.Random;

/**
 * @author 'Mystic Flow
 */
public class GaussianGenerator {

    public static Double nextValue(Random r, double mean, double standard) {
        return r.nextGaussian() * mean + standard;
    }
}