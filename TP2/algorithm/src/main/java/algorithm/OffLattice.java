package algorithm;

import utils.Cell;
import utils.Grid;
import utils.Particle;
import utils.Position;

import java.util.*;

import static utils.CellIndexMethod.cellIndexMethod;
import static utils.Utils.generateParticlesList;

public class OffLattice {
    public static void offLatticeMethod(List<Particle> particles, int L, int M, double rc, boolean periodicGrid, double n){
        for (Particle p : particles){
            System.out.println(p.getInfo());
        }
        HashMap<Integer, Set<Particle>> neighbours;
        int T = 100;
        for(int t = 0 ; t < T ; t++){
            neighbours = cellIndexMethod(particles, L, M, rc, periodicGrid);

            for(Particle p : particles){
                double avg_delta = get_average_theta(neighbours.get(p.getId()), n);
                Position old_pos = p.getPosition();
                double v = p.getV();
                Position new_pos = new Position(old_pos.getX() + v * Math.cos(p.getTheta()), old_pos.getY() + v * Math.sin(p.getTheta()));
                p.setPosition(new_pos);
                p.setTheta(avg_delta);
                if (p.getId() == 1){
                    System.out.println(p.getPosition());
                }
            }
        }

    }

    public static double get_average_theta(Set<Particle> particles, double n){
        double min = -n/2;
        double max = n/2;
        Random r = new Random();
        double noise = min + (max - min) * r.nextDouble();
        double sum_sin = 0, sum_cos = 0;
        for (Particle p : particles){
            sum_sin += Math.sin(p.getTheta());
            sum_cos += Math.cos(p.getTheta());
        }
        sum_sin = sum_sin / particles.size();
        sum_cos = sum_cos / particles.size();
        return Math.atan2(sum_cos, sum_sin) + noise;
    }

    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int L = Integer.parseInt(args[1]);
        double n = Double.parseDouble(args[2]);
        double rc = Double.parseDouble(args[3]);
        List<Particle> particles = generateParticlesList(N,1,0.03);
        double max_radio = particles.stream().max(Comparator.comparing(Particle::getRadio)).get().getRadio();
        int M = (int) Math.floor(L / (rc + 2 * max_radio));
        System.out.println("L: " + L + " N: " + N + " n: " + n + " rc: " + rc + " M: " + M);
        offLatticeMethod(particles,L, M, rc, true,n);

    }
}
