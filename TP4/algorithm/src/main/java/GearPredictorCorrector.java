import java.util.LinkedList;
import java.util.List;

public class GearPredictorCorrector {
    public static Results run(final Particle particle, final double finalTime, final double dt, double k, double gamma){
        List<State> states = new LinkedList<>();
        double t = 0;
        double[] alphas = {3.0/16, 251.0/360, 1.0, 11.0/18, 1.0/6, 1.0/60};
        double force = -k * particle.getX() - gamma * particle.getVx();
        List<Double> derivatives = new LinkedList<>();
        derivatives.add(particle.getX());
        derivatives.add(particle.getVx());
        derivatives.add(force/particle.getM());
        derivatives.add((-k * derivatives.get(1) - gamma * derivatives.get(2))/ particle.getM());
        derivatives.add((-k * derivatives.get(2) - gamma * derivatives.get(3))/ particle.getM());
        derivatives.add((-k * derivatives.get(3) - gamma * derivatives.get(4))/ particle.getM());
        double da, dr2;
        while(t <= finalTime){
            states.add(new State(t, particle.getX(), particle.getVx()));
            List<Double> newDer = gearPredictor(derivatives, dt);
            da = (-k * newDer.get(0) - gamma * newDer.get(1))/ particle.getM() - newDer.get(2);
            dr2 = da * dt * dt / 2;
            derivatives = gearCorrector(newDer, dt, alphas, dr2);
            particle.setX(derivatives.get(0));
            particle.setVx(derivatives.get(1));
            t += dt;
        }
        return new Results(states);
    }

    private static List<Double> gearPredictor(List<Double> der, double dt){
        List<Double> newDer = new LinkedList<>();
        newDer.add(der.get(0) + der.get(1) * dt + der.get(2) * dt * dt /2 + der.get(3) * Math.pow(dt,3)/6
                + der.get(4) * Math.pow(dt,4)/24 + der.get(5)*Math.pow(dt,5)/120);
        newDer.add(der.get(1) + der.get(2) * dt + der.get(3) * dt * dt /2 + der.get(4) * Math.pow(dt,3)/6 + der.get(5) * Math.pow(dt,4)/24);
        newDer.add(der.get(2) + der.get(3) * dt + der.get(4) * dt * dt /2 + der.get(5) * Math.pow(dt,3)/6);
        newDer.add(der.get(3) + der.get(4) * dt + der.get(5) * dt * dt /2);
        newDer.add(der.get(4) + der.get(5) * dt);
        newDer.add(der.get(5));
        return newDer;
    }

    private static List<Double> gearCorrector(List<Double> der, double dt, double[] alpha, double dr2){
        List<Double> newDer = new LinkedList<>();
        newDer.add(der.get(0) + (alpha[0] * dr2 * 1));
        newDer.add(der.get(1) + (alpha[1] * dr2 * 1) / dt);
        newDer.add(der.get(2) + (alpha[2] * dr2 * 2) / (dt*dt));
        newDer.add(der.get(3) + (alpha[3] * dr2 * 6) / (dt*dt*dt));
        newDer.add(der.get(4) + (alpha[4] * dr2 * 24) / (dt*dt*dt*dt));
        newDer.add(der.get(5) + (alpha[5] * dr2 * 120) / (dt*dt*dt*dt*dt));
        return newDer;
    }

}
