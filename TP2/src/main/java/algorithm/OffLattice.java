package algorithm;

import grid_utils.Grid;
import grid_utils.Particle;

import java.util.List;

import static grid_utils.Utils.generateParticlesList;

public class OffLattice {
    public static void runOffLattice(int particleAmount, double speed, Grid grid){
        List<Particle> particles = generateParticlesList(particleAmount, 1.0, 0.03);

    }
}
