import java.util.LinkedList;
import java.util.List;

public class AnalyticMethod {
    public static Results run(final Particle particle, final double finalTime, final double dt, double k, double gamma, final double A){
        List<State> states = new LinkedList<>();
        double t = 0;
        while(t < finalTime){
            states.add(new State(t, particle.getX(), particle.getVx()));
            particle.setX(A * Math.exp(-(gamma/(2* particle.getM())) * t) * Math.cos(Math.pow((k/ particle.getM() - Math.pow(gamma,2)/(4 * Math.pow(particle.getM(), 2))),0.5) * t));
            t += dt;
        }
        return new Results(states);
    }
}
