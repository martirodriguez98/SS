import java.util.LinkedList;
import java.util.List;

public class Beeman {

    public static Results run(final Particle particle, final double finalTime, final double dt, double k, double gamma) {
        List<State> states = new LinkedList<>();
        double v, x;
        double force = -k * particle.getX() - gamma * particle.getVx();
        double prevPosX = euler(particle, force, dt);
    }
    
    

}
