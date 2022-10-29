package CellIndexMethod;

import utils.Pair;
import utils.Particle;
import utils.R;

import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class CellIndexMethod {
    final static String outputFilePath = "TP1/Algorithm/src/main/resources/Results/output.txt";
    final static String times_path = "TP1/Algorithm/src/main/resources/Results/times.txt";
    final static String timesBF = "TP1/Algorithm/src/main/resources/Results/timesBF.txt";


    public static HashMap<Integer, Set<Particle>> cellIndexMethod(Map<Particle, R> particles, int L, int M, int N, int W) {
        Grid grid = new Grid(particles, M, N, L, W);

//        final AlgorithmTime algorithmTime = new AlgorithmTime();

//        algorithmTime.setStart(LocalDateTime.now());

        List<List<Cell>> cells = grid.getGrid();

        HashMap<Integer, Set<Particle>> neighbours = new HashMap<>();
        for (Particle p : particles.keySet()) {
            neighbours.put(p.getId(), new HashSet<>());
        }

        for (int row = 0; row < M; row++) {
            for (int col = 0; col < M; col++) {
                final Cell thisCell = cells.get(row).get(col);
                if (!thisCell.getParticles().isEmpty()) {
                    for (Particle p : thisCell.getParticles()) {
                        findNeighbours(thisCell, p, neighbours, particles, L, M);
                    }
                }
            }
        }

//        algorithmTime.setEnd(LocalDateTime.now());
//        LocalTime totalTime = algorithmTime.getTotalTime();
//        exportResults(neighbours);
//        return totalTime.getNano() / 1000000;
        return neighbours;

    }


    public static void addNeighbourIfClose(Particle p1, Particle p2, Pair posP1, Pair posP2, HashMap<Integer, Set<Particle>> neighbours) {
        double distance = Math.sqrt(Math.pow(posP1.getX() - posP2.getX(), 2) + Math.pow(posP1.getY() - posP2.getY(), 2));
        if (distance - p1.getRadio() - p2.getRadio() <= 0) {
            neighbours.get(p1.getId()).add(p2);
            neighbours.get(p2.getId()).add(p1);
        }
    }

    public static void findNeighbours(Cell cell, Particle p, HashMap<Integer, Set<Particle>> neighbours, Map<Particle, R> particles, int L, int M) {
        final Pair position = particles.get(p).get(0);
        final Cell UCell = cell.getUCell();
        final Cell URCell = cell.getURCell();
        final Cell BRCell = cell.getBRCell();
        final Cell RCell = cell.getRCell();

        //check inside cell
        for (Particle otherP : cell.getParticles()) {
            Pair otherPos = particles.get(otherP).get(0);
            if (!p.equals(otherP)) {
                addNeighbourIfClose(p, otherP, position, otherPos, neighbours);
            }
        }

        if (UCell != null && !UCell.getParticles().isEmpty()) {
            for (Particle otherP : UCell.getParticles()) {
                Pair otherPos = particles.get(otherP).get(0);
                if ( M != 1) { //if M==1 grid is only one cell
                    if (otherPos.getY() < position.getY()) {
                        otherPos = new Pair(otherPos.getX(), otherPos.getY() + L);
                    }
                }
                addNeighbourIfClose(p, otherP, position, otherPos, neighbours);
            }
        }

        if (URCell != null && !URCell.getParticles().isEmpty()) {
            for (Particle otherP : URCell.getParticles()) {
                Pair otherPos = particles.get(otherP).get(0);
                if (M != 1) {
                    if (otherPos.getY() < position.getY()) {
                        otherPos = new Pair(otherPos.getX() + (otherPos.getX() < position.getX() ? L : 0), otherPos.getY() + L);
                    } else if (otherPos.getX() < position.getX()) {
                        otherPos = new Pair(otherPos.getX() + L, otherPos.getY());
                    }
                }
                addNeighbourIfClose(p, otherP, position, otherPos, neighbours);
            }
        }

        if (BRCell != null && !BRCell.getParticles().isEmpty()) {
            for (Particle otherP : BRCell.getParticles()) {
                Pair otherPos = particles.get(otherP).get(0);
                if (M != 1) {
                    if (otherPos.getY() > position.getY()) {
                        otherPos = new Pair(otherPos.getX() + (otherPos.getX() < position.getX() ? L : 0), otherPos.getY() - L);
                    } else if (otherPos.getX() < position.getX()) {
                        otherPos = new Pair(otherPos.getX() + L, otherPos.getY());
                    }
                }
                addNeighbourIfClose(p, otherP, position, otherPos, neighbours);
            }
        }

        if (RCell != null && !RCell.getParticles().isEmpty()) {
            for (Particle otherP : RCell.getParticles()) {
                Pair otherPos = particles.get(otherP).get(0);
                if (M != 1) {
                    if (otherPos.getX() < position.getX()) {
                        otherPos = new Pair(otherPos.getX() + L, otherPos.getY());
                    }
                }
                addNeighbourIfClose(p, otherP, position, otherPos, neighbours);
            }
        }

    }

}
