import utils.Particle;

import java.util.List;

import static utils.Utils.generateParticles;

public class Test {
    public static void main(String[] args) {
        int n = 3;
        double radio = 0.2, bigRadio = 0.7;
        double mass = 0.9, bigMass = 2;
        int L = 6;
        double speed = 2, bigSpeed = 0;
        String pathSt = "src/main/resources/static_" + n + ".txt";
        String pathDy = "src/main/resources/dynamic_" + n + ".txt";

        List<Particle> particles = generateParticles(n, radio, mass, L, speed, bigRadio, bigMass, bigSpeed, pathSt, pathDy);
        int iterations = 100;
        BrownianSystem.algorithm(particles, L, iterations, pathDy);
    }
}
