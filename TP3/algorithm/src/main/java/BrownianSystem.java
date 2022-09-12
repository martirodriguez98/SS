import utils.Collision;
import utils.Particle;
import utils.Position;
import utils.Walls;

import java.util.LinkedList;
import java.util.List;


public class BrownianSystem {
    //tenemos las colisiones
    /*
    ahora tenemos que agarrar el tiempo menor de todas las colisiones y:
        - actualizar las colisiones de todas las que involucran a las del choque
        - a las demas se les resta el tiempo del choque
     */
    public static void algorithm(List<Particle> particles, int L) {
        List<List<Collision>> nextCollisions = calculateNextCollision(particles, L);
        updateStates(nextCollisions, L);

    }

    public static void updateStates(List<List<Collision>> collisions, int L) {
        double minT = Double.MAX_VALUE;
        Collision minTimeCollision = null;
        for (List<Collision> collisionList : collisions) {
            for (Collision c : collisionList) {
                if (c.getTime() < minT) {
                    minT = c.getTime();
                    minTimeCollision = c;
                }
            }
        }
        assert minTimeCollision != null;
        //actualizar x e y de la minima
        updateParticlePosition(minTimeCollision);

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

    private static void updateParticlePosition(Collision collision) {
        double posX1, posY1;
        posX1 = collision.getP1().getPosition().getX();
        posY1 = collision.getP1().getPosition().getY();
        Particle p1 = collision.getP1();
        collision.getP1().setPosition(new Position(posX1 + p1.getV() * Math.cos(p1.getTheta()) * collision.getTime(), posY1 + p1.getV() * Math.sin(p1.getTheta()) * collision.getTime()) );
        double posX2, posY2;
        if (!collision.isWall()){
            posX2 = collision.getP2().getPosition().getX();
            posY2 = collision.getP2().getPosition().getY();
            Particle p2 = collision.getP2();
            collision.getP1().setPosition(new Position(posX2 + p2.getV() * Math.cos(p2.getTheta()) * collision.getTime(), posY2 + p2.getV() * Math.sin(p2.getTheta()) * collision.getTime()) );
        }
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
}

/*
0 1 2 3 4

-- 01 02 03 04
-- -- 12 13 14
-- -- -- 23 24
-- -- -- -- 34

 */
