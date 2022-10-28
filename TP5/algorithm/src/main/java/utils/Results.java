package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Results {
    private List<State> states;

    public Results(List<State> states) {
        this.states = states;
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

    public void exportResults(String path){
        File file = new File(path);
        BufferedWriter bf = null;
        try{
            bf = new BufferedWriter(new FileWriter(file));
            for(State state : states){
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
