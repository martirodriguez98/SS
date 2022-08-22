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

        HashMap<Integer, Set<Particle>> neighbours;
        int T = 100;
        for(int t = 0 ; t < T ; t++){
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
        }

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

    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int L = Integer.parseInt(args[1]);
        double n = Double.parseDouble(args[2]);
        double rc = Double.parseDouble(args[3]);
        List<Particle> particles = generateParticlesList(N,1,0.03, L);
        double max_radio = particles.stream().max(Comparator.comparing(Particle::getRadio)).get().getRadio();
        int M = (int) Math.floor(L / (rc + 2 * max_radio));
        offLatticeMethod(particles,L, M, rc, true,n);

    }
}
