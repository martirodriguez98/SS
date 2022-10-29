package utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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

    static Map<Particle, R> getNextRs(){
        return null;
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
