import utils.AlgorithmTime;

import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class CellIndexMethod {
    final static String outputFilePath = "TP1/Algorithm/src/main/resources/Results/output.txt";

    public static LocalTime cellIndexMethod(List<Particle> particles,int L, int M, double rc, boolean periodicGrid) {
        Grid grid = new Grid(particles, periodicGrid, M, L);

        final AlgorithmTime algorithmTime = new AlgorithmTime();

        algorithmTime.setStart(LocalDateTime.now());

        List<List<Cell>> cells = grid.getGrid();

        HashMap<Integer, Set<Particle>> neighbours = new HashMap<>();
        for(Particle p : particles){
            neighbours.put(p.getId(), new HashSet<>());
        }

        for(int row = 0 ; row < M ; row++){
            for(int col = 0 ; col < M ; col++){
                final Cell thisCell = cells.get(row).get(col);
                if(!thisCell.getParticles().isEmpty()){
                    for(Particle p : thisCell.getParticles()){
                        findNeighbours(thisCell, p, neighbours, particles, L, M, rc, periodicGrid);
                    }
                }
            }
        }

        algorithmTime.setEnd(LocalDateTime.now());
        LocalTime totalTime = algorithmTime.getTotalTime();
        System.out.println("Execution time: " + totalTime);
        exportResults(neighbours);
        return totalTime;

    }

    public static void exportResults(HashMap<Integer, Set<Particle>> neighbours){
        // new file object
        File file = new File(outputFilePath);

        BufferedWriter bf = null;

        try {

            // create new BufferedWriter for the output file
            bf = new BufferedWriter(new FileWriter(file));

            // iterate map entries
            for (Map.Entry<Integer, Set<Particle>> entry : neighbours.entrySet()) {
                // put key and value separated by a colon
                bf.write(entry.getKey() + "\t"
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

    public static void addNeighbourIfClose(Particle p1, Particle p2,Position posP1, Position posP2, double rc, HashMap<Integer, Set<Particle>> neighbours){
        double distance = Math.sqrt(Math.pow(posP1.getX()-posP2.getX(),2) + Math.pow(posP1.getY()-posP2.getY(),2));
        if(distance - p1.getRadio() - p2.getRadio() <= rc){
            neighbours.get(p1.getId()).add(p2);
            neighbours.get(p2.getId()).add(p1);
        }
    }

    public static void findNeighbours(Cell cell, Particle p, HashMap<Integer, Set<Particle>> neighbours, List<Particle> particles, int L, int M, double rc, boolean periodicGrid){
        final Position pos = p.getPosition();
        final Cell UCell = cell.getUCell();
        final Cell URCell = cell.getURCell();
        final Cell BRCell = cell.getBRCell();
        final Cell RCell = cell.getRCell();

        //check inside cell
        for(Particle otherP : cell.getParticles()) {
            if(!p.equals(otherP)) {
                addNeighbourIfClose(p, otherP, pos, otherP.getPosition(), rc, neighbours);
            }
        }

        if(UCell != null && !UCell.getParticles().isEmpty()){
            for(Particle otherP : UCell.getParticles()){
                Position otherPos = otherP.getPosition();
                if(periodicGrid && M != 1){ //if M==1 grid is only one cell
                    if (otherPos.getY() < pos.getY()){
                        otherPos = new Position(otherPos.getX(), otherPos.getY() + L);
                    }
                }
                addNeighbourIfClose(p, otherP, pos, otherPos, rc,neighbours);
            }
        }

        if(URCell != null && !URCell.getParticles().isEmpty()){
            for(Particle otherP : URCell.getParticles()){
                Position otherPos = otherP.getPosition();
                if (periodicGrid && M != 1){
                    if(otherPos.getY() < pos.getY()){
                        otherPos = new Position(otherPos.getX() + (otherPos.getX() < pos.getX() ? L : 0), otherPos.getY() + L);
                    }else if(otherPos.getX() < pos.getX()){
                        otherPos = new Position(otherPos.getX() + L , otherPos.getY());
                    }
                }
                addNeighbourIfClose(p, otherP, pos, otherPos, rc,neighbours);
            }
        }

        if(BRCell != null && !BRCell.getParticles().isEmpty()){
            for (Particle otherP : BRCell.getParticles()){
                Position otherPos = otherP.getPosition();

                if(periodicGrid && M != 1){
                    if(otherPos.getY() > pos.getY()){
                        otherPos = new Position(otherPos.getX() + (otherPos.getX() < pos.getX() ? L : 0), otherPos.getY() - L);
                    }else if(otherPos.getX() < pos.getX()){
                        otherPos = new Position(otherPos.getX() + L, otherPos.getY());
                    }
                    addNeighbourIfClose(p, otherP, pos, otherPos, rc,neighbours);
                }
            }
        }

        if(RCell != null && !RCell.getParticles().isEmpty()){
            for (Particle otherP : RCell.getParticles()){
                Position otherPos = otherP.getPosition();
                if(periodicGrid && M != 1){
                    if(otherPos.getX() < pos.getX()){
                        otherPos = new Position(otherPos.getX() + L, otherPos.getY());
                    }
                }
                addNeighbourIfClose(p, otherP, pos, otherPos, rc,neighbours);
            }
        }

    }

    public static LocalTime bruteForceMethod(List<Particle> particles, int L, int M, double rc, boolean periodicGrid){
        AlgorithmTime algorithmTime = new AlgorithmTime();
        algorithmTime.setStart(LocalDateTime.now());
        HashMap<Particle, List<Particle>> neighbours = new HashMap<>();
        for(int i = 0 ; i < particles.size() ; i++){
            Particle pi = particles.get(i);
            neighbours.putIfAbsent(pi, new LinkedList<>());
            for(int j = i+1 ; j < particles.size() ; j++){
                Particle pj = particles.get(j);
                neighbours.putIfAbsent(pj, new LinkedList<>());
                if(isNeighbour(pi, pj, rc) ){
                    neighbours.get(pi).add(pj);
                    neighbours.get(pj).add(pi);
                }
            }
        }
        algorithmTime.setEnd(LocalDateTime.now());
        LocalTime totalTime = algorithmTime.getTotalTime();
        System.out.println("Execution time: " + totalTime);
        return totalTime;
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
        int L = 0;
        List<Particle> particles = null;
        try {
            Scanner myReader = new Scanner(static_file);
            int index = 0;
            particles = new LinkedList<>();
            while (myReader.hasNextLine()) {
                if (index == 0) {
                    N = Integer.parseInt(myReader.nextLine());
                } else if (index == 1) {
                    L = Integer.parseInt(myReader.nextLine());
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
        boolean periodicGrid = Boolean.parseBoolean(args[4]);

        assert particles != null;
        double max_radio = particles.stream().max(Comparator.comparing(Particle::getRadio)).get().getRadio();

        int M = (int) Math.floor(L/(rc + 2 * max_radio));

        cellIndexMethod(particles,L,M, rc, periodicGrid);
//        bruteForceMethod(particles, L, M, rc, periodicGrid);
    }
}
