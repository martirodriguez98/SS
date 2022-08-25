import static algorithm.OffLattice.runAlgorithm;
import static utils.Utils.generateParticles;

public class test {
    public static void main(String[] args) {
        int N = 300;
        boolean equalParticles = true;
        int L = 25;
        double speed = 0.03;
        String static_path = "/Users/martirodriguez/Documents/ITBA/SS/SS/SS/TP2/algorithm/src/main/resources/static300.txt";
        String dynamic_path = "/Users/martirodriguez/Documents/ITBA/SS/SS/SS/TP2/algorithm/src/main/resources/dynamic300.txt";
        String coeff_path = "/Users/martirodriguez/Documents/ITBA/SS/SS/SS/TP2/algorithm/src/main/resources/coeffList.txt";
        generateParticles(N, equalParticles, L, speed, static_path, dynamic_path);
        double n = 0.1;
        double rc = 1;
        runAlgorithm(static_path, dynamic_path, n, rc, coeff_path);
    }
}
