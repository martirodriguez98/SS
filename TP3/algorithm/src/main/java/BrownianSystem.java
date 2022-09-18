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
        int i = 0;
        List<Double> timeHistory = new LinkedList<>();
        timeHistory.add(0.0);
        while (i < iterations) {
            Collision minTimeCollision = calculateNextCollision(particles, L);
            timeHistory.add(minTimeCollision.getTime() + timeHistory.get(timeHistory.size() - 1));
            updateStates(minTimeCollision, particles, L, pathDy, timeHistory);
            i++;
        }
    }

    public static void updateStates(Collision collision, List<Particle> particles, int L, String pathDy, List<Double> timeHistory) {
        for (Particle particle : particles) {
            updateParticlePosition(particle, collision.getTime());
        }

        if (!collision.isWall()) {
            updateParticleVelocity(collision.getP1(), collision.getP2());
        } else {
            updateParticleVelocityWithWall(collision);
        }

        exportStates(particles, pathDy, timeHistory);
    }

    private static void updateParticleVelocity(Particle p1, Particle p2) {
        final double deltaX = p2.getPosition().getX() - p1.getPosition().getX();
        final double deltaY = p2.getPosition().getY() - p1.getPosition().getY();
        final double deltaVx = p2.getVx() - p1.getVx();
        final double deltaVy = p2.getVy() - p1.getVy();

        final double vr = deltaVx * deltaX + deltaVy * deltaY;

        final double sigma = p1.getRadio() + p2.getRadio();

        final double massSum = p1.getMass() + p2.getMass();

        final double j = (2 * p1.getMass() * p2.getMass() * vr) / (sigma * massSum);
        final double jx = j * deltaX / sigma;
        final double jy = j * deltaY / sigma;

        double newVx1 = p1.getVx() + jx / p1.getMass();
        double newVy1 = p1.getVy() + jy / p1.getMass();
        p1.setVx(newVx1);
        p1.setVy(newVy1);

        // Particle B new state
        double newVx2 = p2.getVx() - jx / p2.getMass();
        double newVy2 = p2.getVy() - jy / p2.getMass();
        p2.setVx(newVx2);
        p2.setVy(newVy2);
    }

    private static void updateParticlePosition(Particle particle, double minTime) {
        double posX1, posY1;
        posX1 = particle.getPosition().getX();
        posY1 = particle.getPosition().getY();
        particle.setPosition(new Position(posX1 + particle.getVx()*minTime, posY1 + particle.getVy()*minTime));
    }

    public static Collision calculateNextCollision(List<Particle> particleList, int L) {
        Collision minTimeCollision = null;
        double minTime = Double.MAX_VALUE;
        for (int i = 0; i < particleList.size(); i++) {
            for (int j = 0; j < particleList.size(); j++) {
                if (i != j) {
                    double particleTime = calculateCollisionTime(particleList.get(i), particleList.get(j));
                    WallCollision wallTime = calculateCollisionTimeWall(particleList.get(i), L);
                    if (Double.compare(particleTime, minTime) <= 0) {
                        minTime = particleTime;
                        minTimeCollision = new Collision(minTime, particleList.get(i), particleList.get(j));
                    }
                    if (Double.compare(wallTime.time, minTime) <= 0) {
                        minTime = particleTime;
                        minTimeCollision = new Collision(minTime, particleList.get(i), wallTime.wallType);
                    }
                }
            }
        }
        return minTimeCollision;
    }


    private static WallCollision calculateCollisionTimeWall(Particle p1, int L) {
        if (p1.getVx() == 0 && p1.getVy() == 0) {
            return new WallCollision(null, Double.MAX_VALUE);
        }

        //Check vertical walls
        Double closestTimeX = null;
        if (p1.getVx() != 0) {
            if (p1.getVx() > 0) {
                closestTimeX = (L - p1.getRadio() - p1.getPosition().getX()) / p1.getVx();
            } else {
                closestTimeX = (p1.getRadio() - p1.getPosition().getX()) / p1.getVx();
            }
        }

//        if (closestTimeX != null && closestTimeX < 0) {
//            throw new RuntimeException();
//        }

        //Check horizontal walls
        Double closestTimeY = null;
        if (p1.getVy() != 0) {
            if (p1.getVy() > 0) {
                closestTimeY = (L - p1.getRadio() - p1.getPosition().getY()) / p1.getVy();
            } else {
                closestTimeY = (p1.getRadio() - p1.getPosition().getY()) / p1.getVy();
            }
        }

        //if (closestTimeY != null && closestTimeY < 0) {
          //  throw new RuntimeException();
        //}

        // Check wall corner
        if (closestTimeX != null && closestTimeX.equals(closestTimeY)) {
            if (Double.compare(closestTimeX, 0) == 0 && Double.compare(closestTimeY, 0) == 0) {
                return new WallCollision(null, Double.MAX_VALUE);
            }
        }

        // If no vertical intersection then horizontal
        if (closestTimeX == null) {
            // Discard previous collision
            if (Double.compare(closestTimeY, 0) == 0) {
                return new WallCollision(null, Double.MAX_VALUE);
            }
            return new WallCollision(Walls.TOP, closestTimeY);
        }

        // If no horizontal intersection then vertical
        else if (closestTimeY == null) {
            // Discard previous collision
            if (Double.compare(closestTimeX, 0) == 0) {
                return new WallCollision(null, Double.MAX_VALUE);
            }
            return new WallCollision(Walls.LEFT, closestTimeX);
        }

        // Discard previous collision
        if (Double.compare(closestTimeX, 0) == 0) {
            closestTimeX = Double.POSITIVE_INFINITY;
        }

        // Discard previous collision
        if (Double.compare(closestTimeY, 0) == 0) {
            closestTimeY = Double.POSITIVE_INFINITY;
        }

        return closestTimeX < closestTimeY ?
                new WallCollision(Walls.LEFT, closestTimeX)
                :
                new WallCollision(Walls.TOP, closestTimeY);

    }

    private static void updateParticleVelocityWithWall(Collision collision) {
        Walls wall = collision.getWall();
        if (wall == Walls.TOP || wall == Walls.BOTTOM) {
            collision.getP1().setVx(-collision.getP1().getVx());
        } else {
            collision.getP1().setVy(-collision.getP1().getVy());
        }
    }

    private static class WallCollision {
        private final Walls wallType;
        private final double time;

        public WallCollision(Walls wallType, double time) {
            this.wallType = wallType;
            this.time = time;
        }

    }

    private static boolean bigParticleInWall(Particle particle, int L) {
        double posX = particle.getPosition().getX() + particle.getRadio();
        double posY = particle.getPosition().getY() + particle.getRadio();
        return posX == 0 || Math.abs(posX - L) <= 0.01 || posY == 0 || Math.abs(posY - L) <= 0.01; // TODO fijarse si el error es suficiente o es mucho
    }

    private static double calculateCollisionTime(Particle p1, Particle p2) {
        final double deltaX = p2.getPosition().getX() - p1.getPosition().getX();
        final double deltaY = p2.getPosition().getY() - p1.getPosition().getY();
        final double deltaVx = p2.getVx() - p1.getVx();
        final double deltaVy = p2.getVy() - p1.getVy();

        final double dvr = deltaVx * deltaX + deltaVy * deltaY;

        if (dvr >= 0) {
            return Double.MAX_VALUE;
        }

        final double rr = Math.pow(deltaX, 2) + Math.pow(deltaY, 2);
        final double vv = Math.pow(deltaVx, 2) + Math.pow(deltaVy, 2);
        final double sigma = p1.getRadio() + p2.getRadio();

        final double d = Math.pow(dvr, 2) - vv * (rr - Math.pow(sigma, 2));
        if (d < 0) {
            return Double.MAX_VALUE;
        }

        final double collisionTime = -(dvr + Math.sqrt(d)) / vv;


        try {
            if (collisionTime < 0) {
                throw new RuntimeException();
            }
        } catch (RuntimeException e) {

        }

        return collisionTime;
    }

    private static void exportStates(List<Particle> particles, String pathDy, List<Double> timeHistory) {
        File file = new File(pathDy);
        BufferedWriter bf = null;
        try {
            bf = new BufferedWriter(new FileWriter(file, true));

            bf.write(timeHistory.get(timeHistory.size() - 1) + "\n");
            for (Particle p : particles) {
                bf.write(p.getPosition().getX() + "\s" + p.getPosition().getY() + "\s" + 0 + "\s" + p.getVx() + "\s" + p.getVy() + "\n");
            }

            bf.flush();
        } catch (IOException e) {
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
