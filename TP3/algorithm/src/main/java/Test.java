import utils.Particle;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static utils.Utils.exportToFile;
import static utils.Utils.generateParticles;

public class Test {
    public static void main(String[] args) {
        ej3();
    }

    private static void ej1(){
        int n = 100;
        double radio = 0.2, bigRadio = 0.7;
        double mass = 0.9, bigMass = 2;
        int L = 6;
        double speed = 2, bigSpeed = 0;
        int iterations = 50000;
        String pathSt = "src/main/resources/static_" + n + ".txt";
        String pathDy = "src/main/resources/dynamic_" + n + ".txt";

        List<Particle> particles = generateParticles(n, radio, mass, L, 0,speed, bigRadio, bigMass, bigSpeed, pathSt, pathDy);
        int runs = 1;
        int i = 0;
        List<Double> avgCollisionTimes = new ArrayList<>();
        List<Double> avgCollisionsPerSecond = new ArrayList<>();
        List<List<Double>> times = new LinkedList<>();
        while (i < runs) {
            times = BrownianSystem.algorithm(particles, L, iterations, pathDy);
            DoubleSummaryStatistics minTimes = times.get(0).stream().collect(Collectors.summarizingDouble(Double::doubleValue));
            avgCollisionTimes.add(minTimes.getAverage());
            avgCollisionsPerSecond.add(times.get(0).size() / times.get(1).get(times.get(1).size() - 1));
            i++;
        }
        exportToFile(times.get(0).stream().map(String::valueOf).collect(Collectors.toList()), "src/main/resources/times_" + n + ".txt");

//        System.out.println(avgCollisionTimes.stream().collect(Collectors.summarizingDouble(Double::doubleValue)).getAverage());
//        System.out.println(avgCollisionsPerSecond.stream().collect(Collectors.summarizingDouble(Double::doubleValue)).getAverage());
    }

    private static void ej2(){
        List<Integer> ns = new LinkedList<>();
        ns.add(100);
        ns.add(110);
        ns.add(125);
        double radio = 0.2, bigRadio = 0.7;
        double mass = 0.9, bigMass = 2;
        int L = 6;
        double speed = 2, bigSpeed = 0;
        int iterations = 50000;

        for (Integer n : ns) {
            String pathSt = "src/main/resources/static_" + n + ".txt";
            String pathDy = "src/main/resources/dynamic_" + n + ".txt";
            List<Particle> particles = generateParticles(n, radio, mass, L, 0,speed, bigRadio, bigMass, bigSpeed, pathSt, pathDy);
            List<List<Double>> times = BrownianSystem.algorithm(particles, L, iterations, pathDy);
            exportToFile(times.get(0).stream().map(String::valueOf).collect(Collectors.toList()), "src/main/resources/times_" + n + ".txt");

        }
    }

    private static void ej3(){
        int n=100;
        double radio = 0.2, bigRadio = 0.7;
        double mass = 0.9, bigMass = 2;
        int L = 6;
        double bigSpeed = 0;
        int iterations = 50000;
        List<Double> speeds = new LinkedList<>();
        speeds.add(2.0);
        speeds.add(3.0);
        speeds.add(4.0);
        for(int i = 0 ; i < speeds.size() ; i++){
            String pathSt = "src/main/resources/static_v_" + speeds.get(i).intValue() + ".txt";
            String pathDy = "src/main/resources/dynamic_v_" + speeds.get(i).intValue() + ".txt";
            List<Particle> particles = generateParticles(n, radio, mass, L, i == 0 ? 0 : speeds.get(i-1),speeds.get(i), bigRadio, bigMass, bigSpeed, pathSt, pathDy);
            BrownianSystem.algorithm(particles, L, iterations, pathDy);
        }
    }


}
