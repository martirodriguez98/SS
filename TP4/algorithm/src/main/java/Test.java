

public class Test {
    public static void main(String[] args) {
        double gamma = 100.0;
        double finalTime = 5;
        double A = 1; //todo check
        double k = Math.pow(10,4);
        double v = (-A * gamma)/2;
        double m = 70;
        double dt = 0.005; //todo check
        Particle particle = new Particle(1, m, v);
        Results resultsVerlet = VerletOriginal.run(particle, finalTime, dt, k, gamma);
        resultsVerlet.exportResults("src/main/resources/verlet.txt");

        particle = new Particle(1, m, v);
        Results resultsAnalytic = AnalyticMethod.run(particle,finalTime,dt,k,gamma,A);
        resultsAnalytic.exportResults("src/main/resources/analytic.txt");

    }


}

