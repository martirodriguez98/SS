package CellIndexMethod;

import utils.Pair;
import utils.Particle;
import utils.R;

import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static java.lang.Math.hypot;

public class CellIndexMethod {
    final static String outputFilePath = "TP1/Algorithm/src/main/resources/Results/output.txt";
    final static String times_path = "TP1/Algorithm/src/main/resources/Results/times.txt";
    final static String timesBF = "TP1/Algorithm/src/main/resources/Results/timesBF.txt";


    public static void addNeighbourIfClose(Particle p, Map<Particle, R> particles, Cell currCell, Map<Particle, Set<Particle>> neighbours) {

        Optional<Cell> UCell = Optional.ofNullable(currCell.getUCell());
        Optional<Cell> URCell = Optional.ofNullable(currCell.getURCell());
        Optional<Cell> RCell = Optional.ofNullable(currCell.getRCell());
        Optional<Cell> BRCell = Optional.ofNullable(currCell.getBRCell());

        Pair pos = particles.get(p).get(0);
        List<Optional<Cell>> neighbourCells = List.of(UCell, URCell, RCell, BRCell);

        //check in own cell
        for(Particle other : currCell.getParticles()){
            Pair otherPos = particles.get(other).get(0);
            if(!p.equals(other))
                addIfTouching(p, other, pos, otherPos, neighbours);
        }
        //check in neighbouring cells
        neighbourCells.stream().filter(cell -> cell.isPresent() && !cell.get().getParticles().isEmpty()).forEach(cell ->{
            for(Particle other : cell.get().getParticles()){
                Pair otherPos = particles.get(other).get(0);
                addIfTouching(p, other, pos, otherPos, neighbours);
            }
        });
    }

    private static void addIfTouching(Particle p, Particle other, Pair pos, Pair otherPos, Map<Particle, Set<Particle>> neighbours){
        double distX = pos.getX() - otherPos.getX();
        double distY = pos.getY() - otherPos.getY();
        double particleDistance = hypot(distX, distY) - p.getRadio() - other.getRadio();
        //check if particles are touching each other
        if(particleDistance <= 0.0){
            neighbours.get(p).add(other);
            neighbours.get(other).add(p);
        }
    }

    public static Map<Particle,Set<Particle>> findNeighbours(Grid grid, Map<Particle, R> particles){
        //set up grid
        List<List<Cell>> cells = grid.getGrid();
        grid.emptyGrid();

        Map<Particle, Set<Particle>> neighbours = new HashMap<>();
        for(Map.Entry<Particle, R> e : particles.entrySet()){
            neighbours.put(e.getKey(), new HashSet<>());
        }
        for (int y=0; y<grid.getM(); y++) {
            for (int x=0; x<grid.getN(); x++) {
                final Cell currCell = cells.get(y).get(x);
                if (!currCell.getParticles().isEmpty()) {
                    for (Particle p : currCell.getParticles()) {
                        addNeighbourIfClose(p, particles, currCell, neighbours);
                    }
                }
            }
        }
        return neighbours;
    }

}
