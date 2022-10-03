import java.util.ArrayList;

public class R {
    private final ArrayList<Pair> ri;

    public R(){
        this.ri = new ArrayList<>();
    }

    public void add(final double x, final double y){
        ri.add(new Pair(x,y));
    }

    public void set(final int index, final double x, final double y){
        ri.set(index, new Pair(x,y));
    }

    public Pair get(final int index){
        return ri.get(index);
    }
}
