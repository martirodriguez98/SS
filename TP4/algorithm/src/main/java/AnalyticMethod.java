import java.util.LinkedList;
import java.util.List;

public class AnalyticMethod {
    public static Results run(final Particle particle, final double finalTime, final double dt, double k, double gamma, final double A){
        List<State> states = new LinkedList<>();
        double t = 0;
        double first, second;
        double m = particle.getM();
        while(t < finalTime){
            first = A * Math.exp(-(gamma/(2* m)) * t);
            second = Math.cos(Math.pow((k/m - (gamma * gamma)/(4 * m * m)),0.5) * t);
            states.add(new State(t, first * second, 0));
            t += dt;
        }
        return new Results(states);
    }
}
