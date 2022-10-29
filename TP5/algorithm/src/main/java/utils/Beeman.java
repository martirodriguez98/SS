package utils;


import CellIndexMethod.Grid;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static utils.BeemanUtils.euler;
import static utils.BeemanUtils.getStartingAcc;

public class Beeman {

    public static Results run(final Particle particle, final double finalTime, final double dt, double k, double gamma) {
        List<State> states = new LinkedList<>();
        double v, x;
        double force = -k * particle.getPosition().getX() - gamma * particle.getVx();
        double prevPosX = euler(particle, force, -dt);
        double prevVx = eulerVel(particle, force, -dt);
        double prevAcc = (-k * prevPosX - gamma * prevVx) / particle.getMass();
        double t = 0;
        double newVx;
        double newAcc;
        while (t < finalTime) {
            states.add(new State(t, particle.getPosition().getX(), particle.getVx()));
            force = -k * particle.getPosition().getX() - gamma * particle.getVx();
            x = particle.getPosition().getX() + particle.getVx() * dt + (2.0 / 3) * (force / particle.getMass()) * dt * dt - (1.0 / 6) * prevAcc * dt * dt;

            //predict velocity
            force = -k * x - gamma * particle.getVx();
            v = particle.getVx() + (3.0 / 2) * (force / particle.getMass()) * dt - (1.0 / 2) * prevAcc * dt;
            newAcc = (-k * x - gamma * v) / particle.getMass();

            //correct velocity
            force = -k * particle.getPosition().getX() - gamma * particle.getVx();
            newVx = particle.getVx() + (1.0 / 3) * newAcc * dt + (5.0 / 6) * (force / particle.getMass()) * dt - (1.0 / 6) * prevAcc * dt;
            prevAcc = (-k * particle.getPosition().getX() - gamma * particle.getVx()) / particle.getMass();

            particle.setVx(newVx);
            particle.getPosition().setX(x);
            t += dt;
        }
        return new Results(states);
    }

    public static Results run(final Map<Particle, R> initialRs, final double finalTime,
                              final double dt, final int w, final double A, final double gravity,
                              final double kn, final double kt) {

        getStartingAcc(initialRs, gravity);
        //todo save states

        Map<Particle, R> prevRs = euler(initialRs, -dt, gravity);
        //todo hacer

        Map<Particle, R> currRs = initialRs;

//        int bestM = Grid.getBestGrid(l - 30, )
//        Grid grid = new Grid();
        //todo crear la grid

    }

    private static double calculateForce(List<Particle> particleList, Particle p) {
        return 0.0;
    }




}
