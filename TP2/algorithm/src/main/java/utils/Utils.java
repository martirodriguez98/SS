package utils;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class Utils {

    public static List<Particle> generateParticlesList(int n,double r, double speed, int L){
        List<Particle> particles = new LinkedList<>();
        double theta, random, x, y;
        double MIN = 0;
        double MAX = Math.toRadians(360);
        Position pos;

        for (int i = 0 ; i < n ; i++){
            random = new Random().nextDouble();
            theta = MIN + (random *(MAX-MIN));
            random = new Random().nextDouble();
            x = (random * L);
            random = new Random().nextDouble();
            y = (random * L);
            pos = new Position(x,y);
            particles.add(new Particle(r,0,i + 1,pos, speed, theta));
        }

        return particles;
    }

    public static void generateParticles(int n,boolean equalParticles,int L, String pathSt, String pathDy){
        double MIN = 0.1;
        double MAX = 3;
        List<String> staticFile = new LinkedList<>();
        List<String> dynamicFile = new LinkedList<>();
        double radio, theta;
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
            random = new Random().nextDouble();
            theta = MIN + (random *(MAX-MIN));
            dynamicFile.add("" + x + "\s" + y + "\s" + 0 + "\s" + theta);

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
        generateParticles(Integer.parseInt(args[0]), Boolean.parseBoolean(args[1]),Integer.parseInt(args[2]) ,args[3],args[4]);
    }


    public static InitialData parseFiles(File static_file, File dynamic_file){
        List<Particle> particles = new LinkedList<>();
        int N=-1,L=-1;
        try {
            Scanner myReader = new Scanner(static_file);
            int index = 0;
            while (myReader.hasNextLine()) {
                if (index == 0) {
                    N = Integer.parseInt(myReader.nextLine());
                } else if (index == 1) {
                    L = Integer.parseInt(myReader.nextLine());
                } else {
                    String data = myReader.nextLine();
                    String[] tokens = data.split("\s+");
                    Particle p = new Particle(Double.parseDouble(tokens[0]), Double.parseDouble(tokens[1]), index - 1);
                    particles.add(p);
                }
                index++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        if (!particles.isEmpty()) {
            try {
                int index = 0;
                Scanner myReader = new Scanner(dynamic_file);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    String[] tokens = data.split("\s+");
                    if (tokens.length == 1) {
                        //time
                        index = 0;
                    } else {
                        particles.get(index - 1).setPosition(new Position(Double.parseDouble(tokens[0]), Double.parseDouble(tokens[1])));
                    }
                    index++;

                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
        if (N!=-1 && L!=-1 && !particles.isEmpty())
            return new InitialData(particles, N, L);
        return null;
    }

}

