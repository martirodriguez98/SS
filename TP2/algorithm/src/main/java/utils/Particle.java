package utils;

public class Particle {
    private double radio;
    private double property;
    private int id;
    private Position position; //if we need the time in the future, we save it as HashMap<time,position>
    private double v;
    private double theta;

    public Particle(double radio, double property, int id) {
        this.radio = radio;
        this.property = property;
        this.id = id;
    }

    public Particle(double radio, double property, int id, double v, double theta) {
        this.id = id;
        this.radio = radio;
        this.property = property;
        this.v = v;
        this.theta = theta;
    }

    public Particle(double radio, double property, int id, Position position, double v, double theta) {
        this.radio = radio;
        this.property = property;
        this.id = id;
        this.position = position;
        this.v = v;
        this.theta = theta;
    }

    public double getRadio() {
        return radio;
    }

    public void setRadio(double radio) {
        this.radio = radio;
    }

    public double getProperty() {
        return property;
    }

    public void setProperty(double property) {
        this.property = property;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getV() {
        return v;
    }

    public void setV(double v) {
        this.v = v;
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }
    public String getInfo(){
        return "" + id + " - " + this.position.toString();
    }

    @Override
    public String toString() {
        return "" + id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Particle particle)) return false;

        return id == particle.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
