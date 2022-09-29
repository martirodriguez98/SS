import java.util.LinkedList;
import java.util.List;

public class Beeman {

    public static Results run(final Particle particle, final double finalTime, final double dt, double k, double gamma) {
        List<State> states = new LinkedList<>();
        double v, x;
        double force = -k * particle.getX() - gamma * particle.getVx();
        double prevPosX = euler(particle, force, -dt);
        double prevVx = eulerVel(particle, force, -dt);
        double prevAcc = (-k * prevPosX - gamma * prevVx)/particle.getM();
        double t = 0;
        double newVx;
        double newAcc;
        while(t < finalTime){
            states.add(new State(t, particle.getX(),particle.getVx()));
            force = -k * particle.getX() - gamma * particle.getVx();
            x = particle.getX() + particle.getVx() * dt + (2.0/3) * (force/ particle.getM()) * dt * dt - (1.0/6) * prevAcc * dt * dt;

            //predict velocity
            force = -k * x - gamma * particle.getVx();
            v = particle.getVx() + (3.0/2) * (force/particle.getM()) * dt - (1.0/2) * prevAcc * dt;
            newAcc = (-k * x - gamma * v)/ particle.getM();

            //correct velocity
            force = -k * particle.getX() - gamma * particle.getVx();
            newVx = particle.getVx() + (1.0/3) * newAcc * dt + (5.0/6) * (force/ particle.getM()) * dt - (1.0/6) * prevAcc * dt;
            prevAcc = (-k * particle.getX() - gamma * particle.getVx())/particle.getM();

            particle.setVx(newVx);
            particle.setX(x);
            t+=dt;
        }
        return new Results(states);
    }

    private static double euler(Particle particle,double f, double dt) {
        return particle.getX() + particle.getVx() * dt + dt * dt * (f/(2* particle.getM()));
    }

    private static double eulerVel(Particle particle, double f, double dt) {
        return particle.getVx() + dt * f/ particle.getM();
    }
    

}
