import java.util.*;

import static algorithm.OffLattice.runAlgorithm;
import static utils.Utils.*;

public class test {
    public static void main(String[] args) {
//        test_one();
        test_many();
    }

    public static void test_one() {
        int N = 300;
        boolean equalParticles = true;
        int L = 5;
        double speed = 0.03;
        String static_path = "/Users/martirodriguez/Documents/ITBA/SS/SS/SS/TP2/algorithm/src/main/resources/static300.txt";
        String dynamic_path = "/Users/martirodriguez/Documents/ITBA/SS/SS/SS/TP2/algorithm/src/main/resources/dynamic300.txt";
        double noise = 0.3;
        String coeff_path = "/Users/martirodriguez/Documents/ITBA/SS/SS/SS/TP2/algorithm/src/main/resources/simulation_5_03.txt";
        generateParticles(N, equalParticles, L, speed, static_path, dynamic_path);
        int iterations = 2000;
        double rc = 1;
        runAlgorithm(static_path, dynamic_path, noise, rc, coeff_path, iterations);
    }

    public static void test_many() {
        int N = 300;
        boolean equalParticles = true;
        double speed = 0.03;
        String static_path = "/Users/martirodriguez/Documents/ITBA/SS/SS/SS/TP2/algorithm/src/main/resources/static300.txt";
        String dynamic_path = "/Users/martirodriguez/Documents/ITBA/SS/SS/SS/TP2/algorithm/src/main/resources/dynamic300.txt";
        double n;
        /*
        List<Double> noises = Arrays.asList(0.0, 0.1, 0.4, 1.0, 2.0, 6.0);
        for(int i=0 ; i < noises.size() ; i++){
            String coeff_path =  "/Users/martirodriguez/Documents/ITBA/SS/SS/SS/TP2/algorithm/src/main/resources/coeff_"+i+".txt";
            generateParticles(N, equalParticles, L, speed, static_path, dynamic_path);
            int iterations = 10000;
            double rc = 1;
            runAlgorithm(static_path, dynamic_path, noises.get(i), rc, coeff_path, iterations);
        }
         */
        List<Double> noises = new ArrayList<>();
        List<Integer> ls = new LinkedList<>();
        for(int i = 1 ; i < 51 ; i++){
            System.out.println(i);
            n = 1.2;
//            for (int i = 0; n < 2 * Math.PI; i++, n += 0.1) {
            String coeff_path = "/Users/martirodriguez/Documents/ITBA/SS/SS/SS/TP2/algorithm/src/main/resources/" + "L" + i + "_coeff_" +  "12"+ ".txt";
            generateParticles(N, equalParticles, i, speed, static_path, dynamic_path);
            int iterations = 2000;
            double rc = 1;
            runAlgorithm(static_path, dynamic_path, n, rc, coeff_path, iterations);
//            noises.add(n);
//            }
            ls.add(i);
        }

        String noises_path = "/Users/martirodriguez/Documents/ITBA/SS/SS/SS/TP2/algorithm/src/main/resources/noises.txt";
        String l_path = "/Users/martirodriguez/Documents/ITBA/SS/SS/SS/TP2/algorithm/src/main/resources/ls.txt";
        exportParameters(noises, noises_path);
        exportParameters(ls, l_path);
    }

}


