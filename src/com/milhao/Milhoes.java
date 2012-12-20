package com.milhao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Milhoes {
/*
* URL PARA PEGAR RESULTADOS!!!
*
* http://www1.caixa.gov.br/loterias/loterias/megasena/megasena_pesquisa_new.asp?submeteu=sim&opcao=concurso&txtConcurso=1450
*
* MEGA DA VIRADA 2011 - Concurso 1350
* MEGA DA VIRADA 2012 - Concurso 1455
* */

    private static final String NUMBER_SEPARATOR = ",";

    private static final int ULTIMO_SORTEIO = 1452;
    static int pesoAMaisTempoNaoSai = 300;
    static int fatorMultiplicadorAMaisTempoNaoSai = 40;

    static int fatorMultiplicadorQuantasVezesSaiu = 4;

    static int pesoIntervaloDeDezenasBom = 2000;
    static int pesoIntervaloDeDezenasRuim = -200;

    static int numeroDeDezenas = 6;//MEGA SENA 6, Quina 5
    static int maiorDezena = 60;//MEGA SENA 60, Quina 80
    static int numeroDeJogosDesejado = 5;
    static int numeroDeGeracoes = 10000;

    private static Map<Integer, Integer> numeroDeVezesQSAIU = new HashMap<Integer, Integer>();

    private static Map<Integer, Integer> aQuantoTempoNaoSai = new HashMap<Integer, Integer>();

    private static Map<Integer, Integer> intervalosDeDezenas = new HashMap<Integer, Integer>();

    private static Collection<Jogo150Milhoes> numerosJaPremiados = new ArrayList<Jogo150Milhoes>();


    public static void main(String[] args) throws Exception {
        List<Jogo150Milhoes> milhoes = new ArrayList<Jogo150Milhoes>();
        for (int j = 0; j < numeroDeGeracoes; j++) {
            Collection<Integer> numbers = new ArrayList<Integer>();
            for (int i = 0; i < numeroDeDezenas; i++) {
                Integer round = new Long(Math.round((Math.random() * maiorDezena))).intValue();
                while (numbers.contains(round) || round == 0) {
                    round = new Long(Math.round((Math.random() * maiorDezena))).intValue();
                }
                numbers.add(round);
            }
            Jogo150Milhoes milhao = new Jogo150Milhoes();
            milhao.numerosMilionarios = numbers;
            milhoes.add(milhao);
        }
        milionarioGenetico(milhoes);
        Collections.sort(milhoes, new Compara());
        int count = 0;
        for (Jogo150Milhoes jogo150Milhoes : milhoes) {
            if (count == numeroDeJogosDesejado) {
                break;
            }
            count++;
            System.out.println("NUMEROS MILHONARIOS = [ " + jogo150Milhoes.numerosMilionarios + "]");
        }
    }

    public static void go() throws Exception {
        Set<String> lines = readFileLines();
//quantas vezes saiu
//quantasVezesADezenaJaSaiu(lines);
//	 //jogos ja premiados uma vez
//excluirJogosJaPremiados(lines);
//a quanto tempo o numero nao sai
//aQuantoTempoNaoSai(lines);
//	 //quais sao os intervalos de numeros q mais saem com relacao as dezenas 10, 20, 30 ... em cada jogo de '6' numeros
//intervalosDasDezenas(lines);
//	 for (Entry<Integer, Integer> string : aQuantoTempoNaoSai.entrySet()) {
//	 System.out.println(string.getKey() + " == >> " + string.getValue());
//	 }
	 calculaFrequenciaDeSorteioDeCadaNumero(lines);

//
//        Map<Integer, Collection<Integer>> distribuicao = normalDistribution(lines);

    }

    //TODO:
    private static void calculaFrequenciaDeSorteioDeCadaNumero(Set<String> lines) {
        ArrayForNumberCreator arrayForNumberCreator = new ArrayForNumberCreator();
        Map<Integer, double[]> intervaloDeCadaNumero = arrayForNumberCreator.getArrayToNumbers(lines, 1, ULTIMO_SORTEIO);
        arrayForNumberCreator.toStandardDeviationToString();
    }

    private static Set<String> readFileLines() throws IOException {
        FileReader reader = new FileReader(new File("/Users/joao/Desktop/todos_os_numeros.txt"));
        BufferedReader br = new BufferedReader(reader);
        String line;
        Set<String> lines = new LinkedHashSet<String>();
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }
        reader.close();
        System.out.println("Ultimo Concurso : " + lines.size());
        return lines;
    }

    private static Set<String> quantasVezesADezenaJaSaiu(Set<String> lines) throws IOException {
        for (int dezena = 1; dezena <= maiorDezena; dezena++) {
            int currentNumberCount = 0;
            for (String line : lines) {
                if (line.matches(RegexUtil.numeroSorteado(dezena))) {
                    currentNumberCount++;
                }
            }
            numeroDeVezesQSAIU.put(dezena, currentNumberCount);
        }
        return lines;
    }

    private static void intervalosDasDezenas(Set<String> lines) throws IOException {
        for (int i = 0; i < numeroDeDezenas - 1; i++) {
            List<Integer> maiorRepeticaoEntre1e10 = getRepeticaoEntre(lines, (i * 10) + 1, (i * 10) + 10);
            intervalosDeDezenas.put(i, maiorRepeticaoEntre1e10.get(numeroDeDezenas - 1));
        }
    }

    private static void aQuantoTempoNaoSai(Set<String> lines) throws IOException {
        for (int dezena = 1; dezena <= maiorDezena; dezena++) {//numeros de 1 a 60
            int contadorDeAQuantoTempoNaoSai = 0;
            for (String line : lines) {
                if (!line.matches(RegexUtil.numeroSorteado(dezena))) {
                    contadorDeAQuantoTempoNaoSai++;
                } else {
                    break;
                }
            }
            aQuantoTempoNaoSai.put(dezena, contadorDeAQuantoTempoNaoSai);
        }
    }

    private static void excluirJogosJaPremiados(Set<String> lines) throws IOException {
        for (int dezena = 1; dezena <= maiorDezena; dezena++) {
            for (String line : lines) {
                String[] numbers = line.split(NUMBER_SEPARATOR);
                Collection<Integer> dezenas = new ArrayList<Integer>();
                for (String string : numbers) {
                    dezenas.add(Integer.valueOf(string));
                }
                Jogo150Milhoes jogoJaPremiado = new Jogo150Milhoes();
                jogoJaPremiado.numerosMilionarios = dezenas;
                numerosJaPremiados.add(jogoJaPremiado);
            }
        }
    }

    private static List<Integer> getRepeticaoEntre(Set<String> lines, int lowerInterval, int higherInterval) throws IOException {

        int contadorDeDezenasN0Intervalo1 = 0, mapa1Vez = 0, mapa2Vez = 0, mapa3Vez = 0, mapa4Vez = 0, mapa5Vez = 0, mapa6Vez = 0;

        for (String line : lines) {
            for (int dezena = lowerInterval; dezena <= higherInterval; dezena++) {
                if (line.matches(RegexUtil.numeroSorteado(dezena))) {
                    String[] numbers = line.split(NUMBER_SEPARATOR);
                    for (String string : numbers) {
                        if (Integer.valueOf(string) <= dezena) {
                            contadorDeDezenasN0Intervalo1++;//nessa linha, quantos numeros entre '1 e 10' aparecem
                        }
                    }
                    if (contadorDeDezenasN0Intervalo1 == 1) mapa1Vez++;
                    if (contadorDeDezenasN0Intervalo1 == 2) mapa2Vez++;
                    if (contadorDeDezenasN0Intervalo1 == 3) mapa3Vez++;
                    if (contadorDeDezenasN0Intervalo1 == 4) mapa4Vez++;
                    if (contadorDeDezenasN0Intervalo1 == 5) mapa5Vez++;
                    if (contadorDeDezenasN0Intervalo1 == 6) mapa6Vez++;
                }
            }

        }
        List<Integer> maiorRepeticao = new ArrayList<Integer>();
        maiorRepeticao.add(mapa1Vez);
        maiorRepeticao.add(mapa2Vez);
        maiorRepeticao.add(mapa3Vez);
        maiorRepeticao.add(mapa4Vez);
        maiorRepeticao.add(mapa5Vez);
        maiorRepeticao.add(mapa6Vez);
        Collections.sort(maiorRepeticao);
        return maiorRepeticao;
    }

    public static void milionarioGenetico(Collection<Jogo150Milhoes> meusMilhoes) throws Exception {
        go();
//	 for (Jogo150Milhoes jogo150Milhoes : meusMilhoes) {
//	 jogo150Milhoes.pontuacao += pontuaNumerosMaisSorteados(jogo150Milhoes.numerosMilionarios);
//	 jogo150Milhoes.pontuacao += pontuaAMaisTempoNaoSaem(jogo150Milhoes.numerosMilionarios);
//	 jogo150Milhoes.pontuacao += excluiJogosQJaSairam(jogo150Milhoes);
//	 jogo150Milhoes.pontuacao += pontuaIntervalosDeDezenas(jogo150Milhoes.numerosMilionarios);
//	 }
    }

    private static Integer pontuaIntervalosDeDezenas(Collection<Integer> numerosMilionarios) {
        int[] intervalos = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        for (Integer integer : numerosMilionarios) {
            if (integer >= 1 && integer <= 10) intervalos[0] = intervalos[0]++;
            if (integer >= 11 && integer <= 20) intervalos[1] = intervalos[1]++;
            if (integer >= 21 && integer <= 30) intervalos[2] = intervalos[2]++;
            if (integer >= 31 && integer <= 40) intervalos[3] = intervalos[3]++;
            if (integer >= 41 && integer <= 50) intervalos[4] = intervalos[4]++;
            if (integer >= 51 && integer <= 60) intervalos[5] = intervalos[5]++;
            if (integer >= 61 && integer <= 70) intervalos[6] = intervalos[6]++;
            if (integer >= 71 && integer <= 80) intervalos[7] = intervalos[7]++;
        }
        for (int i = 0; i < numeroDeDezenas - 1; i++) {
            if (!(intervalosDeDezenas.get(i) == intervalos[i])) {//possui numeros nos intervalos corretos
                return pesoIntervaloDeDezenasRuim;
            }
        }
        return pesoIntervaloDeDezenasBom;
    }

    private static Integer excluiJogosQJaSairam(Jogo150Milhoes jogo150Milhoes) {
        if (numerosJaPremiados.contains(jogo150Milhoes)) {
            return -99999999;
        }
        return 0;
    }

    private static int pontuaAMaisTempoNaoSaem(Collection<Integer> numerosMilionarios) {
        Integer quantoTempo = 0;
        for (Integer milhao : numerosMilionarios) {
            quantoTempo = aQuantoTempoNaoSai.get(milhao) * fatorMultiplicadorAMaisTempoNaoSai;
        }
        return pesoAMaisTempoNaoSai - quantoTempo;
    }

    public static Integer pontuaNumerosMaisSorteados(Collection<Integer> meusNumeros) {
        Integer pontos = 0;
        for (Integer Integer1 : meusNumeros) {
            Integer integer = Milhoes.numeroDeVezesQSAIU.get(Integer1);
            pontos += integer * fatorMultiplicadorQuantasVezesSaiu;
        }
        return pontos;
    }

    public boolean equals(Object o) {
        return false;
    }
}

class Jogo150Milhoes implements Comparable<Jogo150Milhoes> {

    public int pontuacao;
    public Collection<Integer> numerosMilionarios = new ArrayList<Integer>();

    public int compareTo(Jogo150Milhoes o) {
        return this.pontuacao > o.pontuacao ? -1 : this.pontuacao < o.pontuacao ? 1 : 0;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((numerosMilionarios == null) ? 0 : numerosMilionarios.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Jogo150Milhoes other = (Jogo150Milhoes) obj;
        if (numerosMilionarios == null) {
            if (other.numerosMilionarios != null) return false;
        } else if (!numerosMilionarios.equals(other.numerosMilionarios)) return false;
        return true;
    }
}

class Compara implements Comparator<Jogo150Milhoes> {
    public int compare(Jogo150Milhoes arg0, Jogo150Milhoes arg1) {
        return arg0.pontuacao > arg1.pontuacao ? 1 : arg0.pontuacao < arg1.pontuacao ? -1 : 0;
    }
}
/**
 *
 * SUBSTITUICOES PARA REALIZAR NO HTML DO ARQUIVO BAIXADO DA CAIXA
 *


 primeira substituicao...

 <td>.*,\d{2}</td>\n\n<td>\d{1,9}</td>\n\n<td>.*,\d{2}</td>\n\n<td>\d{1,9}</td>\n\n<td>.*,\d{2}</td>\n\n<td>\d{1,9}</td>\n\n<td>.*,\d{2}</td>\n\n<td>[a-zA-Z].*[a-zA-Z]</td>\n\n<td>.*,\d{2}</td>\n\n<td>.*,\d{2}</td>\n\n<td>.*,\d{2}</td>\n\n</tr>

 por NADA

 =====================

 <tr.*>\n\n<td>(\d{1,5})</td>\n\n<td>\d{2}/\d{2}/\d{4}</td>\n\n

 por

 CONCURSO_\1\n

 ====================

 (CONCURSO_\d{1,5})\n<td>(\d{1,2})</td>\n\n<td>(\d{1,2})</td>\n\n<td>(\d{1,2})</td>\n\n<td>(\d{1,2})</td>\n\n<td>(\d{1,2})</td>\n\n<td>(\d{1,2})</td>

 por

 "\2","\3","\4","\5","\6","\7"

 ============================
 substituir
 "\n\n\n\n

 por

 "\n
 =====

 substituir

 "

 por NADA
 **/