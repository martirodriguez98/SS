import utils.Particle;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import static utils.Utils.generateParticles;

public class Test {
    public static void main(String[] args) {
        int n = 100;
        double radio = 0.2, bigRadio = 0.7;
        double mass = 0.9, bigMass = 2;
        int L = 6;
        double speed = 2, bigSpeed = 0;
        String pathSt = "src/main/resources/static_" + n + ".txt";
        String pathDy = "src/main/resources/dynamic_" + n + ".txt";

        List<Particle> particles = generateParticles(n, radio, mass, L, speed, bigRadio, bigMass, bigSpeed, pathSt, pathDy);
        int iterations = 10000;
        int runs = 1;
        int i =0;
        List<Double> avgCollisionTimes = new ArrayList<>();
        List<Double> avgCollisionsPerSecond = new ArrayList<>();
        while(i < runs){
            List<List<Double>> times = BrownianSystem.algorithm(particles, L, iterations, pathDy);
            DoubleSummaryStatistics minTimes = times.get(0).stream().collect(Collectors.summarizingDouble(Double::doubleValue));
            avgCollisionTimes.add(minTimes.getAverage());
            avgCollisionsPerSecond.add(times.get(0).size() / times.get(1).get(times.get(1).size()-1));
            i++;
        }
        System.out.println(avgCollisionTimes.stream().collect(Collectors.summarizingDouble(Double::doubleValue)).getAverage());
        System.out.println(avgCollisionsPerSecond.stream().collect(Collectors.summarizingDouble(Double::doubleValue)).getAverage());
    }
}
