import java.util.LinkedList;
import java.util.List;

public class VerletOriginal {

    public static Results run(final Particle particle, final double finalTime, final double dt, double k, double gamma) {
        List<State> states = new LinkedList<>();
        double x;
        double force = -k * particle.getX() - gamma * particle.getVx();
        double prevPosX = euler(particle, force, -dt);
        double t = 0;
        while (t <= finalTime) {
            states.add(new State(t, particle.getX(),particle.getVx()));
            force = -k * particle.getX() - gamma * particle.getVx();
            x = 2 * particle.getX() - prevPosX + (dt * dt * (force/particle.getM()));

            if(t!=0){
                particle.setVx((x - prevPosX) / (2 * dt));
            }
            prevPosX = particle.getX();
            particle.setX(x);
            t += dt;
        }
        return new Results(states);
    }

    private static double euler(Particle particle,double f, double dt) {
        return particle.getX() + particle.getVx() * dt + dt * dt * (f/(2* particle.getM()));
    }

}
