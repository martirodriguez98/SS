import utils.Collision;
import utils.Particle;
import utils.Walls;

import java.util.LinkedList;
import java.util.List;


public class BrownianSystem {
    private final int L;
    //tenemos las colisiones
    /*
    ahora tenemos que agarrar el tiempo menor de todas las colisiones y:
        - actualizar las colisiones de todas las que involucran a las del choque
        - a las demas se les resta el tiempo del choque
     */
    public static void algorithm(List<Particle> particles, int L){
        List<List<Collision>> nextCollisions = calculateNextCollision(particles, L);
        updateStates(nextCollisions);

    }
    public static void updateStates(List<List<Collision>> collisions){
        double minT = Double.MAX_VALUE;
        Collision minTimeCollision;
        for(List<Collision> collisionList : collisions){
            for(Collision c : collisionList){
                if(c.getTime() < minT){
                    minT = c.getTime();
                    minTimeCollision = c;
                }
            }
        }

        for (List<Collision> particleCollisions : collisions){
            for (Collision collision : particleCollisions){
                if(isInvolved(collision, minTimeCollision)){

                }
            }
        }


    }

    public static boolean isInvolved(Collision col, Collision minCol){
        
    }


    public static List<List<Collision>> calculateNextCollision(List<Particle> particleList, int L){
        List<List<Collision>> collisions = new LinkedList<>();
        for (int i = 0 ; i < particleList.size() ; i++){
            collisions.add(new LinkedList<>());
            for (int j = i + 1 ; j < particleList.size() ; j++){
                double t = calculateCollisionTime(particleList.get(i),particleList.get(j));
                if (t != Double.MAX_VALUE){
                    collisions.get(i).add(new Collision(t, particleList.get(i),particleList.get(j)));
                }
            }
            calculateCollisionTimeWall(particleList.get(i), collisions.get(i),L);
        }
        return collisions;
    }

    private static void calculateCollisionTimeWall(Particle p1, List<Collision> particleCollisions, int L){
        double vx = p1.getV() * Math.cos(p1.getTheta());
        double vy = p1.getV() * Math.sin(p1.getTheta());
        double x = p1.getPosition().getX();
        double y = p1.getPosition().getY();
        double r = p1.getRadio();
        if(vx > 0){
            particleCollisions.add(new Collision((L-r-x)/vx, p1, Walls.RIGHT));
        }else if(vx < 0){
            particleCollisions.add(new Collision((0-r-x)/vx, p1, Walls.LEFT));
        }
        if(vy > 0){
            particleCollisions.add(new Collision((L-r-y)/vy, p1, Walls.TOP));
        }else if(vy < 0){
            particleCollisions.add(new Collision((0-r-y)/vy, p1, Walls.BOTTOM));
        }
    }

    private static double calculateCollisionTime(Particle p1, Particle p2){
        double dx = p1.getPosition().getX() - p2.getPosition().getX();
        double dy = p1.getPosition().getY() - p2.getPosition().getY();
        double dvx = p1.getV() * Math.cos(p1.getTheta()) - p2.getV() * Math.cos(p2.getTheta());
        double dvy = p1.getV() * Math.sin(p1.getTheta()) - p2.getV() * Math.sin(p2.getTheta());
        double dvr = dx*dvx + dy*dvy;
        double sigma = p1.getRadio() + p2.getRadio();
        double dr = Math.pow(dx,2)+ Math.pow(dy,2);
        double dv = Math.pow(dvx,2)+Math.pow(dvy,2);
        double d = Math.pow(dvr,2) - dv * (dr - Math.pow(sigma,2));
        if (dvr >= 0){
            return Double.MAX_VALUE;
        }
        if(d < 0){
            return Double.MAX_VALUE;
        }
        return -((dvr + Math.sqrt(d))/ (dv));
    }
}

/*
0 1 2 3 4

-- 01 02 03 04
-- -- 12 13 14
-- -- -- 23 24
-- -- -- -- 34

 */
