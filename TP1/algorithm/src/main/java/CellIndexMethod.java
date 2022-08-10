import java.io.*;
import java.util.*;

public class CellIndexMethod {
    final static String outputFilePath = "TP1/Algorithm/src/main/resources/Results/output.txt";

    public static void cellIndexMethod(List<Particle> particles, int M, double r_c) {
        HashMap<Particle, List<Particle>> neighbours = new HashMap<>();
        for(int i = 0 ; i < particles.size() ; i++){
            Particle pi = particles.get(i);
            neighbours.putIfAbsent(pi, new LinkedList<>());
            for(int j = i+1 ; j < particles.size() ; j++){
                Particle pj = particles.get(j);
                neighbours.putIfAbsent(pj, new LinkedList<>());
                if(isNeighbour(pi, pj, r_c) ){
                    neighbours.get(pi).add(pj);
                    neighbours.get(pj).add(pi);
                }
            }
        }

        // new file object
        File file = new File(outputFilePath);

        BufferedWriter bf = null;

        try {

            // create new BufferedWriter for the output file
            bf = new BufferedWriter(new FileWriter(file));

            // iterate map entries
            for (Map.Entry<Particle, List<Particle>> entry : neighbours.entrySet()) {
                // put key and value separated by a colon
                bf.write(entry.getKey().getId() + "\t"
                        + entry.getValue());
                // new line
                bf.newLine();
            }

            bf.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                // always close the writer
                assert bf != null;
                bf.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isNeighbour(Particle p1, Particle p2, double rc){
        Position pp1 = p1.getPosition();
        Position pp2 = p2.getPosition();
        double distance = Math.sqrt(Math.pow(pp1.getX()-pp2.getX(),2) + Math.pow(pp1.getY()-pp2.getY(),2));
        return distance - p1.getRadio() - p2.getRadio() <= rc;
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
