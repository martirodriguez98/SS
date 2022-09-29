public class Particle {
    private double x;
    private double m;
    private double vx;

    public Particle(double x, double m, double vx) {
        this.x = x;
        this.m = m;
        this.vx = vx;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getM() {
        return m;
    }

    public void setM(double m) {
        this.m = m;
    }

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }
}
