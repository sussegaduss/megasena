package com.milhao;

import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.*;

public class SorteadosPorIntervaloDeConcurso {

    private Map<Integer, Collection<Integer>> distribuicaoDeCada = new HashMap<Integer, Collection<Integer>>();

    public Map<Integer, Collection<Integer>> normalDistribution(Set<String> lines, int ultimoSorteio) {
        Map<Integer, double[]> intervaloDeCadaNumero = new ArrayForNumberCreator().getArrayToNumbers(lines, 0, ultimoSorteio);

        for (Map.Entry<Integer, double[]> dezenaCorrente : intervaloDeCadaNumero.entrySet()) {
            Integer dezena = dezenaCorrente.getKey();
            Collection<Integer> array = distribuicaoDeCada.get(dezena);
            if (array == null) {
                array = new LinkedList<Integer>();
            }
            int count = 0;
            for (double sorteado : dezenaCorrente.getValue()) {
                if (sorteado == 0) {
                    count++;
                } else {
                    array.add(count);
                    count = 0;
                }
            }
            array.add(count);
            distribuicaoDeCada.put(dezena, array);
        }
        return distribuicaoDeCada;
    }

    public void toUtilString() {
        for (Map.Entry<Integer, Collection<Integer>> string : distribuicaoDeCada.entrySet()) {
            System.out.println("Numero >> " + string.getKey() + " NORMAL >> " + string.getValue());
        }
    }

    public void toVarianceToString() {
        for (Map.Entry<Integer, Collection<Integer>> string : distribuicaoDeCada.entrySet()) {
            double[] arrayOfDouble = new MapUtil().toDoubleMapArray(string);
            System.out.println("Numero >> " + string.getKey() + " VARIANCE >> " + StatUtils.variance(arrayOfDouble));
        }
    }

    public void toStandardDeviationToString() {
        for (Map.Entry<Integer, Collection<Integer>> string : distribuicaoDeCada.entrySet()) {
            SummaryStatistics stats = new SummaryStatistics();
            Collection<Integer> dezenaArray = string.getValue();
            for (double v : dezenaArray) {
                stats.addValue(v);
            }
            System.out.println("Numero >> " + string.getKey() + " DESVIO PADRAO >> " + stats.getStandardDeviation());
        }
    }

    public Map<Integer, Double> getVarianceMapForEachNumber() {
        Map<Integer, Double> varianciaDeCadaNumero = new HashMap<Integer, Double>();
        for (Map.Entry<Integer, Collection<Integer>> string : distribuicaoDeCada.entrySet()) {
            double[] arrayOfDouble = new MapUtil().toDoubleMapArray(string);
            varianciaDeCadaNumero.put(string.getKey(), StatUtils.variance(arrayOfDouble));
        }
        return new Sorting().ordenaPorOrdemDecrescente(varianciaDeCadaNumero);
    }

    public Map<Integer, Double> getStandardDeviation(int ultimoConcursoAConsiderar) {
        Map<Integer, Double> desvioPadrao = new HashMap<Integer, Double>();
        for (Map.Entry<Integer, Collection<Integer>> numero : distribuicaoDeCada.entrySet()) {
            SummaryStatistics stats = new SummaryStatistics();
            List<Integer> dezenaArray = new LinkedList<Integer>(numero.getValue());
            for (double v : dezenaArray.subList(ultimoConcursoAConsiderar, dezenaArray.size())) {
                stats.addValue(v);
            }
            desvioPadrao.put(numero.getKey(), stats.getStandardDeviation());
        }
        return new Sorting().ordenaPorOrdemDecrescente(desvioPadrao);
    }
}