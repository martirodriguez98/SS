package utils;

import java.io.*;
import java.util.*;

public class Utils {

    public static Map<Particle, R> generateParticles(int n, double mass, int L, int W, double minX, double maxX, double minY, double maxY) {
        Map<Particle, R> createdParticles = new HashMap<>();
        int createdParticlesCount = 0;
        for (int i = 0; i < n; i++) {
            double radio = randDouble(new Random(), 0.85, 1.15);
            Particle p = new Particle(createdParticlesCount, radio, mass);
            createdParticlesCount++;
            R state = generateState(p, createdParticles, minX, maxX, minY, maxY);
            createdParticles.put(p, state);
        }

        return createdParticles;
    }

    public static R generateState(Particle p, Map<Particle, R> particles, double minX, double maxX, double minY, double maxY) {
        Random randGen = new Random();
        boolean success = false;
        double x=0, y=0;
        while (!success) {
            success = true;
            x = minX + randGen.nextDouble() * (maxX - minX);
            y = minY + randGen.nextDouble() * (maxY - minY);

            for (Map.Entry<Particle, R> entry : particles.entrySet()) {
                Pair otherPos = entry.getValue().get(0);
                double positionX = Math.pow(otherPos.getX() - x, 2);
                double positionY = Math.pow(otherPos.getY() - y, 2);
                double radios = Math.pow(p.getRadio() + entry.getKey().getRadio(), 2);

                if (positionX + positionY <= radios) {
                    success = false;
                    break;
                }
            }
        }


        R r = new R();
        r.set(0, x,y);
        r.set(1, 0,0); //initial velocity is always 0
        return r;
    }

    public static double randDouble(final Random randomGen, final double min, final double max) {
        return min + randomGen.nextDouble() * (max - min);
    }

    public static void exportStates(PrintWriter pw, Map<Particle,R> particles, double time){
        pw.println(time);
        for (Map.Entry<Particle,R> entry : particles.entrySet()){
            R r = entry.getValue();
            pw.println(r.get(0).getX() + "," +r.get(0).getY() + "," + r.get(1).getX() + "," + r.get(1).getY());
        }
    }

    public static void exportStaticData(PrintWriter pw, Set<Particle> particles, int N){
        pw.println(N);
        for (Particle p : particles){
            pw.println(p.getId() + "," + p.getRadio() + "," + p.getMass());
        }

    }
}
