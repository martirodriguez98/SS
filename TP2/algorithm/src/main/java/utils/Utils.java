package utils;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class Utils {

    public static List<Particle> generateParticlesList(int n, double r, double speed, int L) {
        List<Particle> particles = new LinkedList<>();
        double theta, random, x, y;
        double MIN = 0;
        double MAX = Math.toRadians(360);
        Position pos;

        for (int i = 0; i < n; i++) {
            random = new Random().nextDouble();
            theta = MIN + (random * (MAX - MIN));
            random = new Random().nextDouble();
            x = (random * L);
            random = new Random().nextDouble();
            y = (random * L);
            pos = new Position(x, y);
            particles.add(new Particle(r, 0, i + 1, pos, speed, theta));
        }

        return particles;
    }

    public static void generateParticles(int n, boolean equalParticles, int L, double speed, String pathSt, String pathDy) {
        double MIN = -Math.PI;
        double MAX = Math.PI;
        double MAX_R = 1;
        List<String> staticFile = new LinkedList<>();
        List<String> dynamicFile = new LinkedList<>();
        double radio, theta;

        double random, x, y;
        staticFile.add(String.valueOf(n));
        staticFile.add(String.valueOf(L));
        dynamicFile.add(String.valueOf(0));
        for (int i = 0; i < n; i++) {
            if (equalParticles) {
                radio = 0;
            } else {
                random = new Random().nextDouble();
                radio = MIN + (random * (MAX_R - MIN));
            }
            staticFile.add("" + radio + "\s" + "0");
            random = new Random().nextDouble();
            x = (random * (L));
            random = new Random().nextDouble();
            y = (random * (L));
            random = new Random().nextDouble();
            theta = MIN + (random * (MAX - MIN));
            dynamicFile.add("" + x + "\s" + y + "\s" + 0 + "\s" + theta + "\s" + speed);

        }
        exportToFile(staticFile, pathSt);
        exportToFile(dynamicFile, pathDy);
    }

    public static <T> void exportParameters(List<T> parameters, String path) {
        File file = new File(path);
        BufferedWriter bf = null;

        try {
            // create new BufferedWriter for the output file
            bf = new BufferedWriter(new FileWriter(file,false));

            for (T parameter : parameters) {
                bf.write(parameter.toString() + "\n");
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

    public static InitialData parseFiles(File static_file, File dynamic_file) {
        List<Particle> particles = new LinkedList<>();
        int N = -1, L = -1;
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
                        Particle particle = particles.get(index - 1);
                        particle.setPosition(new Position(Double.parseDouble(tokens[0]), Double.parseDouble(tokens[1])));
                        particle.setTheta(Double.parseDouble(tokens[3]));
                        particle.setV(Double.parseDouble(tokens[4]));
                    }
                    index++;

                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
        if (N != -1 && L != -1 && !particles.isEmpty())
            return new InitialData(particles, N, L);
        return null;
    }

    public static void exportResults(int time, List<Particle> particles, File dynamic_file) {

        BufferedWriter bf = null;

        try {

            // create new BufferedWriter for the output file

            bf = new BufferedWriter(new FileWriter(dynamic_file, true));

            bf.write(String.valueOf(time));
            bf.newLine();
            // iterate map entries
            for (Particle p : particles) {

                // put key and value separated by a colon
                bf.write(p.getPosition().getX() + "\s" + p.getPosition().getY() + "\s" + 0 + "\s" + p.getTheta() + "\s" + p.getV());
                // new line
                bf.newLine();
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

    public static void exportCoeffList(List<String> coeff_list, String coeff_path) {
        BufferedWriter bf = null;
        try {
            bf = new BufferedWriter(new FileWriter(coeff_path, false));
            for (String line : coeff_list) {
                bf.write(line);
                bf.newLine();
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

