import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ResultsMission {
    private final Map<Particle, List<State>> states;

    public ResultsMission(Map<Particle, List<State>> states){
        this.states = states;
    }

    public void exportResults(String path){
        for(Map.Entry<Particle, List<State>> entry : this.states.entrySet()){

            File file = new File(path + entry.getKey().getName() + ".txt");
            BufferedWriter bf = null;
            try{
                bf = new BufferedWriter(new FileWriter(file));
                for(State state : entry.getValue()){
                    bf.write(state.getTime() + "," + state.getX() + "," + state.getVx() + "\n");
                }
                bf.flush();
            }catch (IOException e){
                e.printStackTrace();
            }finally {
                try{
                    assert bf != null;
                    bf.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
