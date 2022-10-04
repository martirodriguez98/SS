public class State {
    private Double time;
    private double x;
    private double y;
    private double vx;
    private double vy;
    private String name;

    public State(Double time, double x, double vx) {
        this.time = time;
        this.x = x;
        this.vx = vx;
    }

    public State(String name, Double time, double x, double y, double vx, double vy) {
        this.name = name;
        this.time = time;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
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
