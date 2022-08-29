import java.util.*;

import static algorithm.OffLattice.runAlgorithm;
import static utils.Utils.*;

public class test {
    public static void main(String[] args) {
        int N = 300;
        boolean equalParticles = true;
//        int L = 5;
        double speed = 0.03;
        String static_path = "/Users/martirodriguez/Documents/ITBA/SS/SS/SS/TP2/algorithm/src/main/resources/static300.txt";
        String dynamic_path = "/Users/martirodriguez/Documents/ITBA/SS/SS/SS/TP2/algorithm/src/main/resources/dynamic300.txt";
        double n;
        /*T
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
        List<Integer> Ls = Arrays.asList(5,10,15,20);

        for(Integer L : Ls) {
            System.out.println(L);
            n = 0;
            for (int i = 0; n < 2 * Math.PI; i++, n += 0.1) {
                String coeff_path = "/Users/martirodriguez/Documents/ITBA/SS/SS/SS/TP2/algorithm/src/main/resources/" + L + "_coeff_" + i + ".txt";
                generateParticles(N, equalParticles, L, speed, static_path, dynamic_path);
                int iterations = 2500;
                double rc = 1;
                runAlgorithm(static_path, dynamic_path, n, rc, coeff_path, iterations);
                noises.add(n);
            }
        }

        String noises_path = "/Users/martirodriguez/Documents/ITBA/SS/SS/SS/TP2/algorithm/src/main/resources/noises.txt";
        String l_path = "/Users/martirodriguez/Documents/ITBA/SS/SS/SS/TP2/algorithm/src/main/resources/ls.txt";
        exportParameters(noises, noises_path);
        exportParameters(Ls,l_path);
    }
}
