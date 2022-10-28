package utils;

import java.util.HashMap;
import java.util.Map;

public class BeemanUtils {
    public static void calculateCurrentForces(Map<Particle, R> currentRs, Map<Particle, R> prevRs, double dt){

        for(Map.Entry<Particle,R> entry : currentRs.entrySet()){
            entry.getValue().get(2) = calculateForce(currentRs, entry.getKey());
            Particle p = entry.getKey();
            double x = p.getPosition().getX() + p.getVx() * dt
                    + (2.0/3) * (entry.getValue().get(2).getX() / p.getMass()) * dt * dt
                    - (1.0/6) * prevRs.get(p).get(2).getX() * dt * dt;

            double y = p.getPosition().getY() + p.getVy() * dt
                    + (2.0/3) * (entry.getValue().get(2).getY() / p.getMass()) * dt * dt
                    - (1.0/6) * prevRs.get(p).get(2).getY() * dt * dt;
            entry.getValue().set(0,x,y);
        }
    }

    public static Map<Particle, R> predictVelocities(Map<Particle, R>  currentRs, Map<Particle, R> prevRs, double dt){
        Map<Particle, R> predictedV = new HashMap<>();
        for (Map.Entry<Particle, R> entry : currentRs.entrySet()){
            R r = new R();
            Pair r2 = calculateForce(currentRs);
            r.set(2,r2.getX(), r2.getY() );
            double r1x = entry.getValue().get(1).getX() + (3.0/2) * (r2.getX() / entry.getKey().getMass()) * dt
                    - (1.0 / 2) * prevRs.get(entry.getKey()).get(2).getX() * dt;
            double r1y = entry.getValue().get(1).getY() + (3.0/2) * (r2.getY() / entry.getKey().getMass()) * dt
                    - (1.0 / 2) * prevRs.get(entry.getKey()).get(2).getY() * dt;
            r.set(1, r1x, r1y);

            prevRs.get(entry.getKey()).set();
        }
    }

    public static Map<Particle, R>correctVelocities(Map<Particle, R>  currentRs, Map<Particle, R> predictedVelocities, Map<Particle, R> prevRs){

    }

    public static void updateParticles(Map<Particle, R>  currentRs, Map<Particle, R> correctedVelocities){

    }
}
