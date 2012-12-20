package com.milhao;

import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ArrayForNumberCreator {

    private Map<Integer, double[]> intervaloDeCadaNumero = new HashMap<Integer, double[]>();

    public Map<Integer, double[]> getArrayToNumbers(Set<String> lines, int valueToPut, int ultimoSorteio) {
        int sorteio = 0;
        for (String line : lines) {
            for (int j = 1; j <= Milhoes.maiorDezena; j++) {//all numbers rating
                double[] array = intervaloDeCadaNumero.get(j);
                if (array == null) {
                    array = new double[ultimoSorteio];
                }
                if (line.matches(RegexUtil.numeroSorteado(j))) {
                    array[sorteio] = valueToPut == 1 ? valueToPut : sorteio;
                } else {
                    array[sorteio] = 0;
                }
                intervaloDeCadaNumero.put(j, array);
            }
            sorteio++;
        }
        return intervaloDeCadaNumero;
    }

    public void toUtilString() {
        for (Map.Entry<Integer, double[]> string : intervaloDeCadaNumero.entrySet()) {
            System.out.println("Numero >> " + string.getKey() + " NORMAL >> " + Arrays.toString(string.getValue()));
        }
    }

    public void toVarianceToString() {
        for (Map.Entry<Integer, double[]> string : intervaloDeCadaNumero.entrySet()) {
            System.out.println("Numero >> " + string.getKey() + " VARIANCE >> " + StatUtils.variance(string.getValue()));
        }
    }

    public void toStandardDeviationToString() {
        for (Map.Entry<Integer, double[]> string : intervaloDeCadaNumero.entrySet()) {
            SummaryStatistics stats = new SummaryStatistics();
            double[] dezenaArray = string.getValue();
            for (double v : dezenaArray) {
                stats.addValue(v);
            }
            System.out.println("Numero >> " + string.getKey() + " DESVIO PADRAO >> " + stats.getStandardDeviation());
        }
    }

    public Map<Integer, Double> getVariance() {
        Map<Integer, Double> varianciaDeCadaNumero = new HashMap<Integer, Double>();
        for (Map.Entry<Integer, double[]> string : intervaloDeCadaNumero.entrySet()) {
            varianciaDeCadaNumero.put(string.getKey(), StatUtils.variance(string.getValue()));
        }
        return new Sorting().ordenaPorOrdemDecrescente(varianciaDeCadaNumero);
    }

    public Map<Integer, Double> getStandardDeviation(int ultimoConcursoAConsiderar) {
        Map<Integer, Double> desvioPadrao = new HashMap<Integer, Double>();
        for (Map.Entry<Integer, double[]> numero : intervaloDeCadaNumero.entrySet()) {
            SummaryStatistics stats = new SummaryStatistics();
            double[] dezenaArray = numero.getValue();
            for (double v : Arrays.copyOfRange(dezenaArray, ultimoConcursoAConsiderar, dezenaArray.length)) {
                stats.addValue(v);
            }
            desvioPadrao.put(numero.getKey(), stats.getStandardDeviation());
        }
        return new Sorting().ordenaPorOrdemDecrescente(desvioPadrao);
    }
}