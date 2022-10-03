import java.util.Collection;

public class Particle {
    private double x;
    private double y;
    private double m;
    private double vx;
    private double vy;
    private double radio;
    private String name;

    public Particle(double x, double m, double vx) {
        this.x = x;
        this.m = m;
        this.vx = vx;
    }

    public Particle(String name, double m, double radio, double x, double y, double vx, double vy) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.m = m;
        this.vx = vx;
        this.vy = vy;
        this.radio = radio;
    }

    public Pair getForce(Collection<Particle> particles, Double G) {
        double forceX = 0;
        double forceY = 0;
        for (Particle p : particles) {
            if (p != this) {
                double m = p.getM() * this.getM();
                double distance = Math.sqrt(Math.pow(p.getX() - this.getX(), 2) + Math.pow(p.getY() - this.getY(), 2));
                double distanceX = (p.getX() - this.getX());
                double distanceY = (p.getY() - this.getY());
                double eX = distanceX / distance;
                double eY = distanceY / distance;
                forceX += G * m * eX / Math.pow(distance, 2);
                forceY += G * m * eY / Math.pow(distance, 2);
            }
        }
        return new Pair(forceX, forceY);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRadio() {
        return radio;
    }

    public void setRadio(double radio) {
        this.radio = radio;
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
