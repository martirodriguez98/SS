public class State {
    private double time;
    private double x;
    private double vx;

    public State(double time, double x, double vx) {
        this.time = time;
        this.x = x;
        this.vx = vx;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }
}
