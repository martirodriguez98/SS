import java.util.*;

public class GearPredictorCorrector {
    private static final double G = 6.693 * Math.pow(10, -11);
    public static Results run(final Particle particle, final double finalTime, final double dt, double k, double gamma) {
        List<State> states = new LinkedList<>();
        double t = 0;
        double[] alphas = {3.0 / 16, 251.0 / 360, 1.0, 11.0 / 18, 1.0 / 6, 1.0 / 60};
        double force = -k * particle.getX() - gamma * particle.getVx();
        List<Double> derivatives = new LinkedList<>();
        derivatives.add(particle.getX());
        derivatives.add(particle.getVx());
        derivatives.add(force / particle.getM());
        derivatives.add((-k * derivatives.get(1) - gamma * derivatives.get(2)) / particle.getM());
        derivatives.add((-k * derivatives.get(2) - gamma * derivatives.get(3)) / particle.getM());
        derivatives.add((-k * derivatives.get(3) - gamma * derivatives.get(4)) / particle.getM());
        double da, dr2;
        while (t <= finalTime) {
            states.add(new State(t, particle.getX(), particle.getVx()));
            List<Double> newDer = gearPredictor(derivatives, dt);
            da = (-k * newDer.get(0) - gamma * newDer.get(1)) / particle.getM() - newDer.get(2);
            dr2 = da * dt * dt / 2;
            derivatives = gearCorrector(newDer, dt, alphas, dr2);
            particle.setX(derivatives.get(0));
            particle.setVx(derivatives.get(1));
            t += dt;
        }
        return new Results(states);
    }

    private static List<Double> gearPredictor(List<Double> der, double dt) {
        List<Double> newDer = new LinkedList<>();
        newDer.add(der.get(0) + der.get(1) * dt + der.get(2) * dt * dt / 2 + der.get(3) * Math.pow(dt, 3) / 6
                + der.get(4) * Math.pow(dt, 4) / 24 + der.get(5) * Math.pow(dt, 5) / 120);
        newDer.add(der.get(1) + der.get(2) * dt + der.get(3) * dt * dt / 2 + der.get(4) * Math.pow(dt, 3) / 6 + der.get(5) * Math.pow(dt, 4) / 24);
        newDer.add(der.get(2) + der.get(3) * dt + der.get(4) * dt * dt / 2 + der.get(5) * Math.pow(dt, 3) / 6);
        newDer.add(der.get(3) + der.get(4) * dt + der.get(5) * dt * dt / 2);
        newDer.add(der.get(4) + der.get(5) * dt);
        newDer.add(der.get(5));
        return newDer;
    }

    private static List<Double> gearCorrector(List<Double> der, double dt, double[] alpha, double dr2) {
        List<Double> newDer = new LinkedList<>();
        newDer.add(der.get(0) + (alpha[0] * dr2 * 1));
        newDer.add(der.get(1) + (alpha[1] * dr2 * 1) / dt);
        newDer.add(der.get(2) + (alpha[2] * dr2 * 2) / (dt * dt));
        newDer.add(der.get(3) + (alpha[3] * dr2 * 6) / (dt * dt * dt));
        newDer.add(der.get(4) + (alpha[4] * dr2 * 24) / (dt * dt * dt * dt));
        newDer.add(der.get(5) + (alpha[5] * dr2 * 120) / (dt * dt * dt * dt * dt));
        return newDer;
    }

    public static Results runMissionGear(Map<String, Particle> particlesMap, final double finalTime, final double dt) {
        double t = 0;
        double[] alphas = {3.0 / 16, 251.0 / 360, 1.0, 11.0 / 18, 1.0 / 6, 1.0 / 60};

        final Map<Particle, List<State>> states = new HashMap<>();
        //initial states
        for(Particle p : particlesMap.values()){
            states.putIfAbsent(p, new LinkedList<>());
            states.get(p).add(new State(0, p.getX(), p.getY(), p.getVx(), p.getVy()));
        }

        int iterations = 0;

        final Map<Particle, R> initialRs = calculateInitialRs(states);


        for (Particle p : particlesMap.values()) {
            Pair forces = p.getForce(particlesMap.values(), G);
            List<Double> derivativesX = new LinkedList<>();
            derivativesX.add(p.getX());
            derivativesX.add(p.getVx());
            derivativesX.add(forces.getX() / p.getM());


            List<Double> derivativesY = new LinkedList<>();

        }

    }

    private static Map<Particle, R> calculateInitialRs(Map<Particle, List<State>> initialStates){
        final Map<Particle, R> rs = new HashMap<>();
        initialStates.forEach((particle, state) -> {
            final R r = new R();
            r.add(state.get(0).getX(),state.get(0).getY());
            r.add(state.get(0).getVx(), state.get(0).getVy());
            rs.put(particle,r);
        });

        final Map<Particle, R> initialRs = new HashMap<>();

        initialStates.forEach((p,s) -> {
            final R initialR = new R();
            initialR.add(s.get(0).getX(), s.get(0).getY()); //r0
            initialR.add(s.get(0).getVx(), s.get(0).getVy()); //r1
            final Pair r2 = calculateForce(p, initialR.get(0), rs);
            initialR.add(r2.getX(), r2.getY()); //r2
            initialR.add(0,0); //r3
            initialR.add(0,0); //r4
            initialR.add(0,0); //r5
            initialRs.put(p, initialR);
        });

        return initialRs;
    }

    private static Pair calculateForce(final Particle currentP, final Pair currentPR0, final Map<Particle, R> Rs){
        double forceX = 0;
        double forceY = 0;
        for (Map.Entry<Particle, R> entry : Rs.entrySet()) {
            Particle p = entry.getKey();
            R rs = entry.getValue();
            if (p != currentP) {
                double m = p.getM() * currentP.getM();
                double distanceR0X = rs.get(0).getX() - currentPR0.getX();
                double distanceR0Y = rs.get(0).getY() - currentPR0.getY();
                double distance = Math.sqrt(Math.pow(distanceR0X, 2) + Math.pow(distanceR0Y, 2));
                double eX = distanceR0X / distance;
                double eY = distanceR0Y / distance;
                forceX += G * m * eX / Math.pow(distance, 2);
                forceY += G * m * eY / Math.pow(distance, 2);
            }
        }
        return new Pair(forceX / currentP.getM(), forceY / currentP.getM());
    }

    private static void saveStates(Map<Particle, List<State>> states, Map<Particle, R> rMap, double t){
        rMap.forEach((particle, r) -> {
            double x = r.get(0).getX();
            double y = r.get(0).getY();
            double vx = r.get(1).getX();
            double vy = r.get(1).getY();
            states.get(particle).add(new State(t, x, y, vx, vy));
        });
    }

}
