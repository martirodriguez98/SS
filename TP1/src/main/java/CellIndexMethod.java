import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class CellIndexMethod {

    public static File cellIndexMethod(List<Particle> particles, int M, double r_c) {


        File output = new File("TP1/src/main/resources/ArchivosEjemplo/output.txt");


        return output;
    }

    public static void main(String[] args) {
        //todo check if parameters are ok.
        File static_file = new File(args[0]);
        int N = 0;
        double L = 0;
        List<Particle> particles = null;
        try {
            Scanner myReader = new Scanner(static_file);
            int index = 0;
            particles = new LinkedList<>();
            while (myReader.hasNextLine()) {
                if (index == 0) {
                    N = Integer.parseInt(myReader.nextLine());
                } else if (index == 1) {
                    L = Double.parseDouble(myReader.nextLine());
                } else {
                    String data = myReader.nextLine();
                    String[] tokens = data.split("\s+");
                    Particle p = new Particle(Double.parseDouble(tokens[0]), Double.parseDouble(tokens[1]), index - 1);
                    particles.add(p);
                }
                index++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        if(particles != null) {
            File dynamic_file = new File(args[1]);
            try {
                int index = 0;
                Scanner myReader = new Scanner(dynamic_file);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    String[] tokens = data.split("\s+");
                    if (tokens.length == 1) {
                        //time
                        index = 0;
                    } else {
                        particles.get(index-1).setPosition(new Position(Double.parseDouble(tokens[0]), Double.parseDouble(tokens[1])));
                    }
                    index++;

                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

        double rc = Double.parseDouble(args[2]);

        assert particles != null;
        double max_radio = particles.stream().max(Comparator.comparing(Particle::getRadio)).get().getRadio();

        int M = (int) Math.floor(L/(rc + 2 * max_radio));

        cellIndexMethod(particles,M, rc);

    }
}
