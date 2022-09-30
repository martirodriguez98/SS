import java.math.BigDecimal;

public class Test {
    public static void main(String[] args) {
        double gamma = 100.0;
        double finalTime = 5.0;
        double A = 1.0; //todo check
        double k = 10000.0;
        double m = 70;
        double v = (-A * gamma)/(2*m);
        Double[] deltas = {0.01, 0.001,0.0001,0.00001};
        for(Double dt : deltas){
            Particle particle = new Particle(1, m, v);
            Results resultsVerlet = VerletOriginal.run(particle, finalTime, dt, k, gamma);
            resultsVerlet.exportResults("verlet_" + dt +".txt");

            particle = new Particle(1, m, v);
            Results resultsBeeman = Beeman.run(particle,finalTime,dt,k,gamma);
            resultsBeeman.exportResults("beeman_" + dt +".txt");

            particle = new Particle(1, m, v);
            Results resultsGear = GearPredictorCorrector.run(particle, finalTime, dt, k, gamma);
            resultsGear.exportResults("gear_" + dt + ".txt");

//            particle = new Particle(1, m, v);
//            Results resultsAnalytic = AnalyticMethod.run(particle,finalTime,dt,k,gamma,A);
//            resultsAnalytic.exportResults("analytic.txt");

        }

    }


}

