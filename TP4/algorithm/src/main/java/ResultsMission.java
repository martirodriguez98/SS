import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ResultsMission {

    private static int SPACESHIP = 1;

    private final Map<Particle, List<State>> states;
    private double minDistance;
    private double minTime;

    public ResultsMission(Map<Particle, List<State>> states) {
        this.states = states;
    }

    public double getMinDistance() {
        return minDistance;
    }


    public void exportResults(String path, int planet) {
        double rv = 0;
        Map<Double, List<State>> statesByTime = new TreeMap<>();
        for (Map.Entry<Particle, List<State>> entry : this.states.entrySet()) {
            for (State state : entry.getValue()) {
                statesByTime.putIfAbsent(state.getTime(), new LinkedList<>());
                statesByTime.get(state.getTime()).add(state);
            }
            if(entry.getKey().getId() == planet){
                rv = entry.getKey().getRadio();
            }
        }
        this.minDistance = Double.MAX_VALUE;

        File file = new File(path);
        BufferedWriter bf = null;
        try {
            bf = new BufferedWriter(new FileWriter(file));
            double sx = 0,sy = 0,vx = 0,vy = 0;
            for (Map.Entry<Double, List<State>> entry : statesByTime.entrySet()) {
                bf.write(String.valueOf(entry.getKey()));
                bf.newLine();
                for (State state : entry.getValue()) {
                    bf.write(state.getId() + "," + state.getX() + "," + state.getY() + "," + state.getVx() + "," + state.getVy());
                    bf.newLine();
                    if (state.getId() == SPACESHIP){
                        sx = state.getX();
                        sy = state.getY();
                    }else if(state.getId() == planet){
                        vx = state.getX();
                        vy = state.getY();
                    }
                }
                double distance = Math.sqrt(Math.pow(sx - vx, 2) + Math.pow(sy - vy, 2)) - rv;
                if (distance < minDistance) {
                    this.minTime = entry.getKey() / 86400;
                    this.minDistance = distance;
                }
                bf.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert bf != null;
                bf.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public void exportResults2(int planet) {
        double rv = 0;
        Map<Double, List<State>> statesByTime = new TreeMap<>();
        for (Map.Entry<Particle, List<State>> entry : this.states.entrySet()) {
            for (State state : entry.getValue()) {
                statesByTime.putIfAbsent(state.getTime(), new LinkedList<>());
                statesByTime.get(state.getTime()).add(state);
            }
            if (entry.getKey().getId() == planet) {
                rv = entry.getKey().getRadio();
            }
        }
        this.minDistance = Double.MAX_VALUE;


        double sx = 0, sy = 0, vx = 0, vy = 0;
        for (Map.Entry<Double, List<State>> entry : statesByTime.entrySet()) {
            for (State state : entry.getValue()) {
                if (state.getId() == SPACESHIP) {
                    sx = state.getX();
                    sy = state.getY();
                } else if (state.getId() == planet) {
                    vx = state.getX();
                    vy = state.getY();
                }
            }
            double distance = Math.sqrt(Math.pow(sx - vx, 2) + Math.pow(sy - vy, 2)) - rv;
            if (distance < minDistance) {
                this.minTime = entry.getKey() / 86400;
                this.minDistance = distance;
            }
        }
    }



    public double getMinTime() {
        return minTime;
    }

    public void setMinTime(double minTime) {
        this.minTime = minTime;
    }
}

