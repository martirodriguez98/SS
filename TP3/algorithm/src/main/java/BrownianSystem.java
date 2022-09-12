import utils.Collision;
import utils.Particle;
import utils.Position;
import utils.Walls;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


public class BrownianSystem {
    //tenemos las colisiones
    /*
    ahora tenemos que agarrar el tiempo menor de todas las colisiones y:
        - actualizar las colisiones de todas las que involucran a las del choque
        - a las demas se les resta el tiempo del choque
     */
    public static void algorithm(List<Particle> particles, int L, int iterations, String pathDy) {
        List<List<Collision>> nextCollisions = calculateNextCollision(particles, L); //1 time
        List<Double> times = new LinkedList<>();
        times.add(0.0);
        int i = 0;
        while (i < iterations){
            if(updateStates(nextCollisions,particles, L,times, pathDy)){
                return;
            }
            i++;
        }
    }

    public static boolean updateStates(List<List<Collision>> collisions,List<Particle> particles, int L,List<Double> times, String pathDy) {
        double minT = Double.MAX_VALUE;
        Collision minTimeCollision = null;
        final double EPSILON = 1.0E-6;
        for (List<Collision> collisionList : collisions) {
            for (Collision c : collisionList) {
                if (Math.abs(minT-c.getTime()) < EPSILON) {
                    minT = c.getTime();
                    minTimeCollision = c;
                }
            }
        }
        assert minTimeCollision != null;
        System.out.println("min time " + minT + " == " + minTimeCollision.getTime());

        times.add(times.get(times.size()-1) + minTimeCollision.getTime());
        System.out.println(times.get(times.size()-1));
        System.out.println("--------------------");
        //actualizar x e y de la minima
        updateParticlePosition(minTimeCollision.getP1(), minTimeCollision.getTime());
        if(!minTimeCollision.isWall()){
            updateParticlePosition(minTimeCollision.getP2(), minTimeCollision.getTime());
        }

        for (int i = 0; i < collisions.size(); i++) {
            if (minTimeCollision.isWall()) {
                if (i + 1 == minTimeCollision.getP1().getId()) {
                    updateParticleCollisions(collisions.get(i), L);
                }
            } else if (i + 1 == minTimeCollision.getP1().getId()) {
                updateParticleCollisions(collisions.get(i), L);
            } else if (i + 1 == minTimeCollision.getP2().getId()) {
                updateParticleCollisions(collisions.get(i), L);
            } else {
                for (Collision collision : collisions.get(i)) {
                    if (collision.getTime() != Double.MAX_VALUE) {
                        collision.minusTime(minTimeCollision.getTime());
                    }
                }
            }
        }
        for(Particle particle : particles){
            if (!particle.equals(minTimeCollision.getP1()) && !particle.equals(minTimeCollision.getP2()))
                updateParticlePosition(particle, minTimeCollision.getTime());
        }

        exportStates(particles, times.get(times.size()-1), pathDy);
        return bigParticleInWall(collisions.get(0).get(0).getP1(), L);
    }

    public static void updateParticleCollisions(List<Collision> collisions, int L) {
        for (Collision collision : collisions) {
            if (!collision.isWall()) {
                double time = calculateCollisionTime(collision.getP1(), collision.getP2());
                collision.setTime(time);
            } else {
                WallCollision wc = calculateCollisionTimeWall(collision.getP1(), L);
                collision.setTime(wc.time);
                collision.setWall(wc.wallType);
            }
        }

    }

    private static void updateParticlePosition(Particle particle, double minTime) {
        double posX1, posY1;
        posX1 = particle.getPosition().getX();
        posY1 = particle.getPosition().getY();
        particle.setPosition(new Position(posX1 + particle.getV() * Math.cos(particle.getTheta()) * minTime, posY1 + particle.getV() * Math.sin(particle.getTheta()) * minTime) );

    }

    public static List<List<Collision>> calculateNextCollision(List<Particle> particleList, int L) {
        List<List<Collision>> collisions = new LinkedList<>();
        for (int i = 0; i < particleList.size() - 1; i++) {
            collisions.add(new LinkedList<>());
            for (int j = i + 1; j < particleList.size(); j++) {
                double t = calculateCollisionTime(particleList.get(i), particleList.get(j));
                collisions.get(i).add(new Collision(t, particleList.get(i), particleList.get(j))); //if there's no collision t=Double.Max_VALUE
            }
            WallCollision wc = calculateCollisionTimeWall(particleList.get(i), L);
            collisions.get(i).add(new Collision(wc.time, particleList.get(i), wc.wallType));
        }
        return collisions;
    }

    private static WallCollision calculateCollisionTimeWall(Particle p1, int L) {
        double vx = p1.getV() * Math.cos(p1.getTheta());
        double vy = p1.getV() * Math.sin(p1.getTheta());
        double x = p1.getPosition().getX();
        double y = p1.getPosition().getY();
        double r = p1.getRadio();
        if (vx > 0) {
            return new WallCollision(Walls.RIGHT, (L - r - x) / vx);
        } else if (vx < 0) {
            return new WallCollision(Walls.LEFT, (0 - r - x) / vx);
        }
        if (vy > 0) {
            return new WallCollision(Walls.TOP, (L - r - y) / vy);
        } else {
            return new WallCollision(Walls.BOTTOM, (0 - r - y) / vy);
        }
    }

    private static class WallCollision {
        private Walls wallType;
        private double time;

        public WallCollision(Walls wallType, double time) {
            this.wallType = wallType;
            this.time = time;
        }

        public Walls getWallType() {
            return wallType;
        }

        public void setWallType(Walls wallType) {
            this.wallType = wallType;
        }

        public double getTime() {
            return time;
        }

        public void setTime(double time) {
            this.time = time;
        }
    }

    private static boolean bigParticleInWall(Particle particle, int L){
        double posX = particle.getPosition().getX() + particle.getRadio();
        double posY = particle.getPosition().getY() + particle.getRadio();
        return posX == 0 || Math.abs(posX - L) <= 0.01 || posY == 0 || Math.abs(posY - L) <= 0.01; // TODO fijarse si el error es suficiente o es mucho
    }

    private static double calculateCollisionTime(Particle p1, Particle p2) {
        double dx = p1.getPosition().getX() - p2.getPosition().getX();
        double dy = p1.getPosition().getY() - p2.getPosition().getY();
        double dvx = p1.getV() * Math.cos(p1.getTheta()) - p2.getV() * Math.cos(p2.getTheta());
        double dvy = p1.getV() * Math.sin(p1.getTheta()) - p2.getV() * Math.sin(p2.getTheta());
        double dvr = dx * dvx + dy * dvy;
        double sigma = p1.getRadio() + p2.getRadio();
        double dr = Math.pow(dx, 2) + Math.pow(dy, 2);
        double dv = Math.pow(dvx, 2) + Math.pow(dvy, 2);
        double d = Math.pow(dvr, 2) - dv * (dr - Math.pow(sigma, 2));
        if (dvr >= 0) {
            return Double.MAX_VALUE;
        }
        if (d < 0) {
            return Double.MAX_VALUE;
        }

        return -((dvr + Math.sqrt(d)) / (dv));

    }

    private static void exportStates(List<Particle> particles, double time, String pathDy){
        File file = new File(pathDy);
        BufferedWriter bf = null;
        try{
            bf = new BufferedWriter(new FileWriter(file, true));
            for(Particle p : particles ){
                bf.write(p.getPosition().getX() + "\s" + p.getPosition().getY() + "\s" + 0 + "\s" + p.getTheta() + "\s" + p.getV());
            }
            bf.flush();
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // always close the writer
                assert bf != null;
                bf.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

/*
0 1 2 3 4

-- 01 02 03 04
-- -- 12 13 14
-- -- -- 23 24
-- -- -- -- 34

 */
