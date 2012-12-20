package com.milhao;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class DesvioPadrao {

    private static void compute(Set<String> lines, int ultimoSorteio) {
        Map<Integer, double[]> intervaloDeCadaNumero = new ArrayForNumberCreator().getArrayToNumbers(lines, 1, ultimoSorteio);
        for (Map.Entry<Integer, double[]> d : intervaloDeCadaNumero.entrySet()) {
            System.out.println("KEY >>" + d.getKey() + " VALUE " + Arrays.toString(d.getValue()));
            SummaryStatistics stats = new SummaryStatistics();
            double[] dValue = d.getValue();
            for (double v : Arrays.copyOfRange(dValue, 1250, dValue.length)) {
                stats.addValue(v);
            }
            System.out.println("KEY >>" + d.getKey() + " DESVIO " + stats.getStandardDeviation());

        }
    }
}
