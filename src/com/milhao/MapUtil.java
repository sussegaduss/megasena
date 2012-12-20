package com.milhao;

import java.util.Collection;
import java.util.Map;

public class MapUtil {

    public double[] toDoubleMapArray(Map.Entry<Integer, Collection<Integer>> string) {
        Collection<Integer> integerCollection = string.getValue();
        int count = 0;
        double[] arrayOfDouble = new double[integerCollection.size()];
        for (Integer integer : integerCollection) {
            arrayOfDouble[count++] = integer;
        }
        return arrayOfDouble;
    }
}


