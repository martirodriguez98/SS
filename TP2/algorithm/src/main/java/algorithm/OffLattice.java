package algorithm;

import utils.*;

import java.io.*;
import java.util.*;

import static utils.CellIndexMethod.cellIndexMethod;
import static utils.Utils.*;

public class OffLattice {
    public static void offLatticeMethod(List<Particle> particles, int L, int M, double rc, boolean periodicGrid, double n, File dynamicFile, String coeff_path){
        HashMap<Integer, Set<Particle>> neighbours;
        int T = 100;
        double order_coeff = get_order_coeff(particles);
        List<String> coeff_list = new LinkedList<>();
        coeff_list.add("0\s" + order_coeff);
        int t = 1;
        while (t < 8000){
            neighbours = cellIndexMethod(particles, L, M, rc, periodicGrid);
            for(Particle p : particles){
                double avg_delta = get_average_theta(p,neighbours.get(p.getId()), n);
                Position old_pos = p.getPosition();
                double v = p.getV();
                double new_x = (old_pos.getX() + v * Math.cos(p.getTheta())) % L;
                double new_y = (old_pos.getY() + v * Math.sin(p.getTheta())) % L;
                if(new_x < 0){
                    new_x += L;
                }
                if(new_y < 0){
                    new_y += L;
                }
                Position new_pos = new Position(new_x, new_y);
                p.setPosition(new_pos);
                p.setTheta(avg_delta);
            }
            order_coeff = get_order_coeff(particles);
            coeff_list.add(t + "\s" +order_coeff);
            exportResults(t, particles, dynamicFile);
            t++;
        }
        exportCoeffList(coeff_list, coeff_path);
    }

    public static double get_order_coeff(List<Particle> particles){
        double v = particles.get(0).getV();
        double vx = 0, vy = 0;
        for(Particle p : particles){
            vx += Math.cos(p.getTheta()) * p.getV();
            vy += Math.sin(p.getTheta()) * p.getV();
        }
        double v_sum = Math.sqrt(Math.pow(vx,2) + Math.pow(vy,2));
        return v_sum / (particles.size() * v);
    }

    public static double get_average_theta(Particle particle, Set<Particle> particles, double n){
        double min = -n/2;
        double max = n/2;
        Random r = new Random();
        double noise = min + ((max - min) * r.nextDouble());
        double sum_sin = 0, sum_cos = 0;
        //sumo el propio theta de la particula primero
        sum_sin += Math.sin(particle.getTheta());
        sum_cos += Math.cos(particle.getTheta());
        for (Particle p : particles){
            sum_sin += Math.sin(p.getTheta());
            sum_cos += Math.cos(p.getTheta());
        }
        sum_sin = sum_sin / (particles.size() + 1);
        sum_cos = sum_cos / (particles.size() + 1);
        return Math.atan2(sum_cos, sum_sin) + noise;
    }


    public static void runAlgorithm(String static_path, String dynamic_path, double n, double rc, String coeff_path) {

        File static_file = new File(static_path);
        File dynamic_file = new File(dynamic_path);

        InitialData initialData = parseFiles(static_file, dynamic_file);
        if (initialData == null) {
            throw new IllegalArgumentException("Incorrect files");
        }
        double max_radio = initialData.getParticles().stream().max(Comparator.comparing(Particle::getRadio)).get().getRadio();
        int M = (int) Math.floor(initialData.getL() / (rc));
        offLatticeMethod(initialData.getParticles(), initialData.getL(), M, rc, true,n,dynamic_file, coeff_path);

    }
}
