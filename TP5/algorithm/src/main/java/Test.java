import utils.Particle;
import utils.R;
import utils.SiloBeeman;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;

import static utils.Utils.exportStaticData;
import static utils.Utils.generateParticles;

public class Test {
    public static void main(String[] args) {
        ejA();
    }

    public static void ejA() {
        int n = 200;
        int mass = 1;
        int L = 70;
        int W = 20;
        double minX = 0;
        double maxX = 20;
        double minY = 0;
        double maxY = 70;
        double finalTime = 60;
        double dt = 0.001;
        int d = 3;
        double A = 0.15;
        //todo despues se varia el omega
        double omega = 5;
        double gravity = 5;
        double kn =250;
        double kt =2*kn;

        String staticFile = "static.txt";
        String dynamicFile = "dynamic.txt";
        Map<Particle, R> initialParticles = generateParticles(n, mass, L, W, minX, maxX,minY,maxY);

        try(PrintWriter pw = new PrintWriter(staticFile)){
            exportStaticData(pw, initialParticles.keySet(), n);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try(PrintWriter pw = new PrintWriter(dynamicFile)){
            SiloBeeman.run(initialParticles, finalTime, dt, L, W, d, A, gravity, kn, kt, omega,pw );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
