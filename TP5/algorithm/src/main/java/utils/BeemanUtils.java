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

    static Pair getAcc(Particle p, Set<Particle> neighbours, Map<Particle, R> currRs, double d, int w, double gravity, double kn, double kt, double wallR0, double wallR1){
        //will use forces to calculate acceleration

        Pair particleR0 = currRs.get(p).get(0);
        Pair particleR1 = currRs.get(p).get(1);

        double fx=0, fy=0;

        // if it collides with other particles
        for(Particle particle : neighbours){
            R pR = currRs.get(particle);
            if(particle != p){
                //calculate particle collision forces
                Pair collisionF = calculateCollisionForce(p, particle, particleR0, particleR1, pR.get(0), pR.get(1), kn, kt);
                fx += collisionF.getX();
                fy += collisionF.getY();
            }
        }
        // if it collides with a wall
        //bottom wall
        if(particleR0.getY() <= p.getRadio()){
            //is the particle inside the opening?
            if(checkIfOpening(particleR0, w ,d)){
                //right side of opening
                R rightSideR = new R();
                rightSideR.set(0, w/2.0 + d/2.0, 0);
                rightSideR.set(1, 0, wallR1);
                Pair rightSideForce = calculateCollisionForce(p, null, particleR0, particleR1, rightSideR.get(0), rightSideR.get(1), kn ,kt);

                //left side of opening
                R leftSideR = new R();
                leftSideR.set(0, w/2.0 - d/2.0, 0);
                leftSideR.set(1, 0, wallR1);
                Pair leftSideForce = calculateCollisionForce(p, null, particleR0, particleR1, leftSideR.get(0), leftSideR.get(1), kn ,kt);

                fx += rightSideForce.getX() + leftSideForce.getX();
                fy += rightSideForce.getY() + leftSideForce.getY();
            }
            else{
                R bottomWallR = new R();
                bottomWallR.set(0, particleR0.getX(), wallR0);
                bottomWallR.set(1, 0, wallR1);
                Pair bottomWallForce = calculateCollisionForce(p, null, particleR0, particleR1, bottomWallR.get(0), bottomWallR.get(1), kn, kt);

                fx += bottomWallForce.getX();
                fy += bottomWallForce.getY();
            }
        }

        //left wall
        if(particleR0.getX() <= p.getRadio()){
            R lWall = new R();
            lWall.set(0, 0, particleR0.getY());
            lWall.set(1, 0, wallR1);
            Pair force = calculateCollisionForce(p, null, particleR0, particleR1, lWall.get(0), lWall.get(1), kn, kt);
            fx += force.getX();
            fy += force.getY();
        }

        //right wall
        if(particleR0.getX() + p.getRadio() >= w){
            R rWall = new R();
            rWall.set(0, w, particleR0.getY());
            rWall.set(1, 0, wallR1);
            Pair force = calculateCollisionForce(p, null, particleR0, particleR1, rWall.get(0), rWall.get(1), kn, kt);
            fx += force.getX();
            fy += force.getY();
        }

        //gravity
        fy -= p.getMass() * gravity;
        return new Pair(fx/p.getMass(), fy/p.getMass());
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

        //now correct predictions
        while(prevRsIt.hasNext() && currRsIt.hasNext()){
            Map.Entry<Particle, R> prev = prevRsIt.next();
            Map.Entry<Particle, R> curr = currRsIt.next();

            R nextR = new R();
            R prevR = prev.getValue();
            R currR = curr.getValue();
            Particle p = prev.getKey();

            //get current particle's neighbours
            Set<Particle> pNeighbours = neighbours.get(p);
            //find particle's acceleration
            Pair predR2 = getAcc(p, pNeighbours, nextRs, d, w, gravity, kn, kt, wallR0, wallR1);

            //correct velocity
            double correctedR1X = currR.get(1).getX() + ((1/3.0) * predR2.getX() + (5/6.0) * currR.get(2).getX() - (1/6.0) * prevR.get(2).getX()) * dt;
            double correctedR1Y = currR.get(1).getY() + ((1/3.0) * predR2.getY() + (5/6.0) * currR.get(2).getY() - (1/6.0) * prevR.get(2).getY()) * dt;

            nextR.set(1, correctedR1X, correctedR1Y);
        }

        for(Particle p : currRs.keySet()){
            R nextR = nextRs.get(p);
            //get current particle's neighbours
            Set<Particle> pNeighbours = neighbours.get(p);
            //find particle's acceleration
            Pair correctedR2 = getAcc(p, pNeighbours, nextRs, d, w, gravity, kn, kt, wallR0, wallR1);

            nextR.set(2, correctedR2.getX(), correctedR2.getY());
            nextRs.put(p, nextR);
        }
        return nextRs;
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

    static Pair calculateCollisionForce(Particle p1, Particle p2, Pair p1R0, Pair p1R1, Pair p2R0, Pair p2R1, double kn, double kt){
        double fx=0, fy=0;

        double dR0X = p2R0.getX() - p1R0.getX();
        double dR0Y = p2R0.getY() - p1R0.getY();
        double dist = Math.hypot(dR0X, dR0Y);
        double radiusDist = p1.getRadio() + (p2 == null ? 0.0 : p2.getRadio());
        double touching = radiusDist - dist;

        //use particle collision force equations
        if(touching >= 0){
            //normal
            double enX = dR0X / dist;
            double enY = dR0Y / dist;
            //tangential
            double etX = -enY;
            double etY = enX;

            double fn = -kn * touching;

            double dR1X = p1R1.getX() - p2R1.getX();
            double dR1Y = p1R1.getY() - p2R1.getY();

            double ft = -kt * touching * (dR1X * etX + dR1Y * etY);

            fx += fn * enX + ft * etX;
            fy += fn * enY + ft * etY;
        }
        return new Pair(fx, fy);
    }

    private static boolean checkIfOpening(Pair pR0, int w, double d){
        return (pR0.getX() > (w/2.0 - d/2.0)) && (pR0.getX() < (w/ 2.0 + d/2.0));
    }
}
