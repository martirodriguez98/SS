package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Utils {

    public static List<Particle> generateParticles(int n, double mass, int L, int W, double minX, double maxX, String pathSt, String pathDy) {

        List<String> staticFile = new LinkedList<>();
        List<String> dynamicFile = new LinkedList<>();
        Random randGen = new Random();

        staticFile.add(String.valueOf(n));
        staticFile.add(String.valueOf(L));
        dynamicFile.add(String.valueOf(0));

        List<Particle> createdParticles = new ArrayList<>();

        int createdParticlesCount = 0;
        boolean success = false;
        while (createdParticlesCount < n) {
            while (!success) {
                success = true;
                double radio = Particle.randDouble(randGen, 0.85, 1.15);
                Particle p = Particle.randomParticle(randGen, createdParticlesCount, radio, L, W, mass);
                for (Particle particle : createdParticles) {
                    double positionX = Math.pow(p.getPosition().getX() - particle.getPosition().getX(), 2);
                    double positionY = Math.pow(p.getPosition().getY() - particle.getPosition().getY(), 2);
                    double radios = Math.pow(p.getRadio() + particle.getRadio(), 2);
                    if (positionX + positionY <= radios) {
                        success = false;
                        break;
                    }
                }
                if (success) {
                    createdParticles.add(p);
                    createdParticlesCount++;
                    staticFile.add("" + radio + "\s" + mass);
                    dynamicFile.add("" + p.getPosition().getX() + "\s" + p.getPosition().getY() + "\s" + 0 + "\s" + p.getVx() + "\s" + p.getVy());
                }
            }
            success = false;
        }
        exportToFile(staticFile, pathSt);
        exportToFile(dynamicFile, pathDy);
        return createdParticles;
    }

    public static void exportToFile(List<String> list, String path) {
        File file = new File(path);
        BufferedWriter bf = null;

        try {
            // create new BufferedWriter for the output file
            bf = new BufferedWriter(new FileWriter(file));

            for (String lt : list) {
                bf.write(lt.toString() + "\n");
            }
            bf.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // always close the writer
                assert bf != null;
                bf.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
