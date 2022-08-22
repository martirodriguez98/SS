package grid_utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class Utils {

    public static List<Particle> generateParticlesList(int n,double r, double speed){
        List<Particle> particles = new LinkedList<>();
        double theta, random;
        double MIN = 0;
//        double MAX = 2 * Math.PI;
        double MAX = Math.toRadians(360);

        for (int i = 0 ; i < n ; i++){
            random = new Random().nextDouble();
            theta = MIN + (random *(MAX-MIN));
        }
        return particles;

    }

    public static void generateParticles(int n,boolean equalParticles,int L, String pathSt, String pathDy){
        double MIN = 0.1;
        double MAX = 3;
        List<String> staticFile = new LinkedList<>();
        List<String> dynamicFile = new LinkedList<>();
        double radio;
        int maxX = 100;
        int maxY = 100;

        double random, x, y;
        staticFile.add(String.valueOf(n));
        staticFile.add(String.valueOf(100));
        dynamicFile.add(String.valueOf(0));
        for(int i = 0 ; i < n ; i++){
            if(equalParticles){
                radio = 0.25;
            }else {
                random = new Random().nextDouble();
                radio = MIN + (random *(MAX-MIN));
            }
            staticFile.add("" + radio + "\s" + "0");
            random = new Random().nextDouble();
            x = (random *(maxX));
            random = new Random().nextDouble();
            y = (random *(maxY));
            dynamicFile.add("" + x + "\s" + y);
        }
        exportToFile(staticFile,pathSt);
        exportToFile(dynamicFile,pathDy);
    }

    public static void exportToFile(List<String> list, String path){
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

    public static void main(String[] args) {
        System.out.println(Math.toRadians(360));
//        generateParticles(Integer.parseInt(args[0]), Boolean.parseBoolean(args[1]),Integer.parseInt(args[2]) ,args[3],args[4]);
    }


}

