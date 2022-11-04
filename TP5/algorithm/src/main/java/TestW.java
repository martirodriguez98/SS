import utils.Particle;
import utils.R;
import utils.SiloBeeman;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static utils.Utils.exportStaticData;
import static utils.Utils.generateParticles;

public class TestW {
    public static void main(String[] args) {
        int n = 200;
        int mass = 1;
        int L = 70;
        int W = 20;
        double minX = 0;
        double maxX = 20;
        double minY = 0;
        double maxY = 70;
        double finalTime = 500;
        double dt = 0.001;
        int d = 3;
        double A = 0.15;
        //todo despues se varia el omega
        List<Double> omegas = Arrays.asList(5.0,10.0,15.0,20.0,30.0,50.0);
        double gravity = 5;
        double kn =250;
        double kt =2*kn;


        Map<Particle, R> initialParticles = generateParticles(n, mass, L, W, minX, maxX,minY,maxY);
        for (Double omega : omegas){
            String staticFile = "static_w_" + omega +".txt";
            String dynamicFile = "dynamic_w_" + omega + ".txt";
            String flowFile = "flow_" + omega + ".txt";

            try(PrintWriter pw = new PrintWriter(staticFile)){
                exportStaticData(pw, initialParticles.keySet(), n);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            try(PrintWriter pw = new PrintWriter(dynamicFile)){
                try(PrintWriter pwFlow = new PrintWriter(flowFile)){
                    SiloBeeman.run(initialParticles, finalTime, dt, L, W, d, A, gravity, kn, kt, omega,pw,pwFlow );
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println("Process for omega " + omega + " done.");
        }
    }

}
