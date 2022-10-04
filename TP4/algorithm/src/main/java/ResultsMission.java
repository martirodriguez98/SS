import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ResultsMission {
    private final Map<Particle, List<State>> states;

    public ResultsMission(Map<Particle, List<State>> states) {
        this.states = states;
    }

    public void exportResults(String path) {

        Map<Double, List<State>> statesByTime = new TreeMap<>();
        for (Map.Entry<Particle, List<State>> entry : this.states.entrySet()) {
            for (State state : entry.getValue()) {
                statesByTime.putIfAbsent(state.getTime(), new LinkedList<>());
                statesByTime.get(state.getTime()).add(state);
            }
        }

        File file = new File(path);
        BufferedWriter bf = null;
        try {
            bf = new BufferedWriter(new FileWriter(file));
            for (Map.Entry<Double, List<State>> entry : statesByTime.entrySet()) {
                bf.write(String.valueOf(entry.getKey()));
                bf.newLine();
                for (State state : entry.getValue()) {
                    bf.write(state.getX() + "," + state.getY() + "," + 0 + "," + state.getVx() + "," + state.getVy());
                    bf.newLine();
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
}

