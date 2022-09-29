import java.util.LinkedList;
import java.util.List;

public class VerletOriginal {

    public static Results run(final Particle particle, final double finalTime, final double dt, double k, double gamma) {
        List<State> states = new LinkedList<>();
        double v,x;
        double force = -k * particle.getX() - gamma * particle.getVx();
        double prevPosX = euler(particle, force, dt);
        states.add(new State(0, particle.getX(),particle.getVx()));
        double t = dt;
        while (t < finalTime) {
            force = -k * particle.getX() - gamma * particle.getVx();
            x = 2 * particle.getX() - prevPosX + dt * dt * (force/particle.getM());
            prevPosX = particle.getX();
            particle.setX(x);
            particle.setVx((particle.getX() - prevPosX) / (2 * dt));
            states.add(new State(t, particle.getX(),particle.getVx()));
            t += dt;
        }
        return new Results(states);
    }

    //esto realmente calcula el anterior? o es el proximo?
    private static double euler(Particle particle,double f, double dt) {
        return particle.getX() + particle.getVx() * dt + dt * dt * (f/(2* particle.getM()));
    }

}
