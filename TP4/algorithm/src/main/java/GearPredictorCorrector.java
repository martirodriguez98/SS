import java.math.BigInteger;
import java.util.*;

public class GearPredictorCorrector {
    private static final double G = 6.693 * Math.pow(10, -11);
    private static final double[] alphas = {3.0 / 16, 251.0 / 360, 1.0, 11.0 / 18, 1.0 / 6, 1.0 / 60};

    public static Results run(final Particle particle, final double finalTime, final double dt, double k, double gamma) {
        List<State> states = new LinkedList<>();
        double t = 0;
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

    public static ResultsMission runMissionGear(Map<String, Particle> particlesMap, final double finalTime, final double dt) {
        final Map<Particle, List<State>> initialStates = new HashMap<>();
        //initial states
        for (Particle p : particlesMap.values()) {
            initialStates.putIfAbsent(p, new LinkedList<>());
            initialStates.get(p).add(new State(0, p.getX(), p.getY(), p.getVx(), p.getVy()));
        }

        int iterations = 0;

        final Map<Particle, R> initialRs = calculateInitialRs(initialStates);
        final Map<Particle, List<State>> states = new HashMap<>();
        saveStates(states, initialRs, 0);

        Map<Particle, R> currentRs = initialRs;
        for (double t = dt; t <= finalTime; t += dt, iterations += 1) {
            final Map<Particle, R> predictions = predict(currentRs, dt);
            final Map<Particle, Pair> deltasR2 = getDeltasR2(predictions, dt);
            currentRs = correct(predictions, deltasR2, dt);
            saveStates(states, currentRs, t);
        }
        return new ResultsMission(states);

    }

    private static Map<Particle, R> predict(Map<Particle, R> currentRs, final double dt) {
        final Map<Particle, R> predictions = new HashMap<>();
        currentRs.forEach((particle, r) -> {
            final R prediction = new R();
            for (int i = 0; i < 6; i++) { //r0 .. r5
                double rpx = 0;
                double rpy = 0;
                for (int j = i; j < 6; j++) {
                    final Pair rj = r.get(j);
                    rpx += rj.getX() * Math.pow(dt, j - i) / factorial(j-i);
                    rpy += rj.getY() * Math.pow(dt, j - i) / factorial(j-i);
                }
                prediction.add(rpx,rpy);
            }
            predictions.put(particle,prediction);
        });
        return predictions;
    }

    private static Map<Particle, Pair> getDeltasR2(Map<Particle, R> predictionsRs, double dt){
        Map<Particle, Pair> deltaR2 = new HashMap<>();
        predictionsRs.forEach((particle, r) -> {
            final Pair r2 = calculateForce(particle, r.get(0), predictionsRs);
            final double r2x = r.get(2).getX();
            final double r2y = r.get(2).getY();
            final double deltar2x = r2.getX() - r2x;
            final double deltar2y = r2.getY() - r2y;
            final double deltaR2x = deltar2x * Math.pow(dt,2) /factorial(2);
            final double deltaR2y = deltar2y * Math.pow(dt,2) / factorial(2);
            deltaR2.put(particle, new Pair(deltaR2x, deltaR2y));
        });
        return deltaR2;
    }

    private static Map<Particle, R> correct(Map<Particle, R> predictions, final Map<Particle, Pair> deltasR2, final double dt){
        final Map<Particle, R> corrections = new HashMap<>();
        predictions.forEach((particle, r) -> {
            final R correction = new R();
            for (int i = 0; i < 6 ; i++){
                final Pair rp = predictions.get(particle).get(i);
                final double rx = rp.getX() + alphas[i] * deltasR2.get(particle).getX() * factorial(i) / Math.pow(dt,i);
                final double ry = rp.getY() + alphas[i] * deltasR2.get(particle).getY() * factorial(i) / Math.pow(dt,i);
                correction.add(rx,ry);
            }
            corrections.put(particle, correction);
        });
        return corrections;
    }


    private static int factorial(int n) {
        if (n == 0)
            return 1;
        else
            return n * factorial(n - 1);
    }

    private static Map<Particle, R> calculateInitialRs(Map<Particle, List<State>> initialStates) {
        final Map<Particle, R> rs = new HashMap<>();
        initialStates.forEach((particle, state) -> {
            final R r = new R();
            r.add(state.get(0).getX(), state.get(0).getY());
            r.add(state.get(0).getVx(), state.get(0).getVy());
            rs.put(particle, r);
        });

        final Map<Particle, R> initialRs = new HashMap<>();

        initialStates.forEach((p, s) -> {
            final R initialR = new R();
            initialR.add(s.get(0).getX(), s.get(0).getY()); //r0
            initialR.add(s.get(0).getVx(), s.get(0).getVy()); //r1
            final Pair r2 = calculateForce(p, initialR.get(0), rs);
            initialR.add(r2.getX(), r2.getY()); //r2
            initialR.add(0, 0); //r3
            initialR.add(0, 0); //r4
            initialR.add(0, 0); //r5
            initialRs.put(p, initialR);
        });

        return initialRs;
    }

    private static Pair calculateForce(final Particle currentP, final Pair currentPR0, final Map<Particle, R> Rs) {
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

    private static void saveStates(Map<Particle, List<State>> states, Map<Particle, R> rMap, double t) {
        rMap.forEach((particle, r) -> {
            double x = r.get(0).getX();
            double y = r.get(0).getY();
            double vx = r.get(1).getX();
            double vy = r.get(1).getY();
            states.putIfAbsent(particle, new LinkedList<>());
            states.get(particle).add(new State(t, x, y, vx, vy));
        });
    }
}
