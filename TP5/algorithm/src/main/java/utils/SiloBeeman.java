package utils;


import CellIndexMethod.Grid;

import java.io.PrintWriter;
import java.util.*;

import static utils.BeemanUtils.*;
import static utils.Utils.exportFlow;
import static utils.Utils.exportStates;

public class SiloBeeman {
    private static double RESPAWN_MIN_H = 40;
    private static double RESPAWN_MAX_H = 70;


    public static void run(final Map<Particle, R> initialRs, final double finalTime,
                              final double dt, final int l, final int w, final int d, final double A, final double gravity,
                              final double kn, final double kt, final double omega, PrintWriter pw, PrintWriter pwFlow) {

        getStartingAcc(initialRs, gravity);

        Map<Particle, R> prevRs = euler(initialRs, -dt, gravity);

        Map<Particle, R> currRs = initialRs;
        Set<Particle> particlesLeft = new HashSet<>();

        double maxRadius = initialRs.keySet().stream().map(Particle::getRadio).max(Double::compare).orElseThrow();
        int bestM = Grid.getBestGrid(l - (int)(RESPAWN_MAX_H-RESPAWN_MIN_H), maxRadius); //30 is the restriction given
        int bestN = Grid.getBestGrid(w, maxRadius);
        Grid grid = new Grid(bestM, bestN, l - (int)(RESPAWN_MAX_H-RESPAWN_MIN_H), w);

        int exportIterationsStep = 100;
        int iters = 0;

        for(double t = dt; t < finalTime; t+=dt, iters+=1){
            if(iters % exportIterationsStep == 0) {
                exportStates(pw, currRs, t);
            }

            Map<Particle, R> nextRs = getNextRs(grid, omega, t, dt, A, kn, kt, gravity, prevRs, currRs, w, d); //todo ver que poner

            Set<Particle> particlesLeavingNow = new HashSet<>();
            final Map<Particle, R> respawnedParticles = respawnParticles(currRs, particlesLeavingNow, particlesLeft, w, RESPAWN_MIN_H,RESPAWN_MAX_H,l/10.0); //todo hacer que se muevan
            getStartingAcc(respawnedParticles, gravity);

            nextRs.putAll(respawnedParticles);

            if (particlesLeavingNow.size() != 0){
                exportFlow(t, pwFlow);
            }
            //todo compute caudal
            /*
            ventanas de 10/20 particulas para el error
             */



            prevRs = currRs;
            prevRs.putAll(euler(respawnedParticles, -dt, gravity));
            currRs = nextRs;
        }
    }

}
