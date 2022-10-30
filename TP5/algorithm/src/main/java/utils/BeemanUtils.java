package utils;

import CellIndexMethod.CellIndexMethod;
import CellIndexMethod.Grid;

import java.util.*;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class BeemanUtils {

    static void getStartingAcc(Map<Particle, R> states, double gravity){
        states.forEach((particle, r) -> r.set(2, 0.0, -gravity));
    }

    static Map<Particle, R> euler(Map<Particle,R> initialRs, double dt, double gravity){

        final Map<Particle, R> states = new HashMap<>();

        initialRs.forEach(((particle, r) -> {
            R eulerR = new R();
            Pair r0 = r.get(0);
            Pair r1 = r.get(1);
            Pair r2 = r.get(2);

            //r0
            double r0X = r0.getX() + dt * r1.getX() + (dt * dt / (2 * particle.getMass())) * r2.getX() * particle.getMass();
            double r0Y = r0.getY() + dt * r1.getY() + (dt * dt / (2 * particle.getMass())) * r2.getY() * particle.getMass();
            eulerR.set(0, r0X, r0Y);

            //r1
            double r1X = r1.getX() + (dt / particle.getMass()) * r2.getX() * particle.getMass();
            double r1Y = r1.getY() + (dt / particle.getMass()) * r2.getY() * particle.getMass();
            eulerR.set(1, r1X, r1Y);

            //r2
            eulerR.set(2, 0.0, -gravity);
            states.put(particle, eulerR);
        }));
        return states;
    }

    static Map<Particle, R> getNextRs(Grid grid, double omega, double t, double dt, double A, double kn, double kt, double gravity, Map<Particle, R> prevRs, Map<Particle, R> currRs, int w, double d){

        Map<Particle, R> nextRs = new HashMap<>();

        //calculate silo vibration
        double wallR0 = wallVibrationR0(A, omega, t);
        double wallR1 = wallVibrationR1(A, omega, t);

        //now iterate over prev and curr R's
        Iterator<Map.Entry<Particle, R>> currRsIt = currRs.entrySet().iterator();
        Iterator<Map.Entry<Particle, R>> prevRsIt = prevRs.entrySet().iterator();

        while(currRsIt.hasNext() && prevRsIt.hasNext()){

            Map.Entry<Particle, R> prev = prevRsIt.next();
            Map.Entry<Particle, R> curr = currRsIt.next();

            R nextR = new R();
            R prevR = prev.getValue();
            R currR = curr.getValue();
            Particle p = prev.getKey();

            //calculate next r0
            double r0x = currR.get(0).getX() + currR.get(1).getX() * dt + ((2/3.0) * currR.get(2).getX() - (1/6.0) * prevR.get(2).getX())*dt*dt;
            double r0y = currR.get(0).getY() + currR.get(1).getY() * dt + ((2/3.0) * currR.get(2).getY() - (1/6.0) * prevR.get(2).getY())*dt*dt;
            nextR.set(0, r0x, r0y);

            //predict velocity
            double predR1X = currR.get(1).getX() + ((3/2.0) * currR.get(2).getX() - (1/2.0) * prevR.get(2).getX()) * dt;
            double predR1Y = currR.get(1).getY() + ((3/2.0) * currR.get(2).getY() - (1/2.0) * prevR.get(2).getY()) * dt;
            nextR.set(1, predR1X, predR1Y);

            nextRs.put(p, nextR);
        }

        //restart iterators
        currRsIt = currRs.entrySet().iterator();
        prevRsIt = prevRs.entrySet().iterator();

        //get neighbours
        Map<Particle, Set<Particle>> neighbours = CellIndexMethod.findNeighbours(grid, nextRs);
        
    }

    private static double wallVibrationR0(final double A, final double omega, final double t){
        return A * sin(omega * t);
    }

    private static double wallVibrationR1(final double A, final double omega, final double t){
        return A * omega * cos(omega * t);
    }

    static Map<Particle,R> respawnParticles(Map<Particle, R> currRs, Set<Particle> particlesLeavingNow, Set<Particle> particlesLeft, int w, double respawnMinH, double respawnMaxH, double respawnDist){

        Map<Particle, R> respawnedParticles = new HashMap<>();
        currRs.forEach((particle, r) -> {

            Pair pos = r.get(0);
            //check if particle already left silo

            if(pos.getY() < -particle.getRadio() && !particlesLeft.contains(particle)){
                particlesLeavingNow.add(particle);
                particlesLeft.add(particle);
            }
            //check if particle crossed treshold
            if(pos.getY() - particle.getRadio() < -respawnDist) {
                R newR = Utils.generateState(particle, currRs, 0 + particle.getRadio(), w - particle.getRadio(), respawnMinH + particle.getRadio(), respawnMaxH - particle.getRadio()); //todo ponerle las cositas
                particlesLeft.remove(particle);
                respawnedParticles.put(particle, newR);
            }
        });
        return respawnedParticles;
    }
}
