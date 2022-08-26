import java.util.*;

import static algorithm.OffLattice.runAlgorithm;
import static utils.Utils.exportNoises;
import static utils.Utils.generateParticles;

public class test {
    public static void main(String[] args) {
        int N = 300;
        boolean equalParticles = true;
        int L = 25;
        double speed = 0.03;
        String static_path = "/Users/martirodriguez/Documents/ITBA/SS/SS/SS/TP2/algorithm/src/main/resources/static300.txt";
        String dynamic_path = "/Users/martirodriguez/Documents/ITBA/SS/SS/SS/TP2/algorithm/src/main/resources/dynamic300.txt";
        double n = 0;
        List<Double> noises = Arrays.asList(0.0, 0.1, 0.4, 1.0, 2.0, 6.0);
        for(int i=0 ; i < noises.size() ; i++){
            String coeff_path =  "/Users/martirodriguez/Documents/ITBA/SS/SS/SS/TP2/algorithm/src/main/resources/coeff_"+i+".txt";
            generateParticles(N, equalParticles, L, speed, static_path, dynamic_path);
            int iterations = 10000;
            double rc = 1;
            runAlgorithm(static_path, dynamic_path, noises.get(i), rc, coeff_path, iterations);
        }
        String noises_path = "/Users/martirodriguez/Documents/ITBA/SS/SS/SS/TP2/algorithm/src/main/resources/noises.txt";
        exportNoises(noises, noises_path);

    }
}
