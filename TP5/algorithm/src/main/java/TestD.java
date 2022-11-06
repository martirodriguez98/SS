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

public class TestD {
    public static void main(String[] args) {
        int n = 200;
        int mass = 1;
        int L = 70;
        int W = 20;
        double minX = 0;
        double maxX = 20;
        double minY = 0;
        double maxY = 70;
        double finalTime = 1000;
        double dt = 0.001;
        double A = 0.15;
        double omega = 15; //todo usar el optimo
        List<Integer> ds = Arrays.asList(4,5,6);
        double gravity = 5;
        double kn =250;
        double kt =2*kn;


        Map<Particle, R> initialParticles = generateParticles(n, mass, L, W, minX, maxX,minY,maxY);
        for (Integer d : ds){
            String staticFile = "static_d_" + d +".txt";
            String dynamicFile = "dynamic_d_" + d + ".txt";
            String flowFile = "flow_w20_" + d+ ".txt";

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
            System.out.println("Process for gap " + d + " done.");
        }
    }
}
