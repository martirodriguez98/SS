import java.util.LinkedList;
import java.util.List;

public class Beeman {

    public static Results run(final Particle particle, final double finalTime, final double dt, double k, double gamma) {
        List<State> states = new LinkedList<>();
        double v, x;
        double force = -k * particle.getX() - gamma * particle.getVx();
        double prevPosX = euler(particle, force, dt);
        double prevVx = eulerVel(particle, force, dt);
        double prevAcc = (-k * prevPosX - gamma * prevVx)/particle.getM();
        states.add(new State(0, particle.getX(),particle.getVx()));
        double t = dt;
        double newVx;
        double newAcc;
        while(t < finalTime){
            force = -k * particle.getX() - gamma * particle.getVx();
            x = particle.getX() + particle.getVx() * dt + (2*force/3) * (force/ particle.getM()) * dt * dt - (force/6) * prevAcc * dt * dt;

            //predict velocity
            force = -k * x - gamma * particle.getVx();
            v = particle.getVx() + (3*force/2) * (force/particle.getM() * dt - (force/2) * prevAcc * dt);
            newAcc = (-k * x - gamma * v)/ particle.getM();

            //correct velocity
            force = -k * particle.getX() - gamma * particle.getVx();
            newVx = v + (3*force/2) * newAcc * dt + (5*force/6) * (force/ particle.getM()) * dt - (force/6) * prevAcc * dt;
            prevAcc = (-k * particle.getX() - gamma * particle.getVx())/particle.getM();

            particle.setVx(newVx);
            particle.setX(x);
            states.add(new State(t, particle.getX(),particle.getVx()));
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
