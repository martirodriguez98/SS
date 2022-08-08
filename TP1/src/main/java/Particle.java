public class Particle {
    private double radio;
    private double property;
    private long id;
    private Position position; //if we need the time in the future, we save it as HashMap<time,position>

    public Particle(double radio, double property, long id) {
        this.id = id;
        this.radio = radio;
        this.property = property;
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

    @Override
    public String toString() {
        return "Particle{" +
                "radio=" + radio +
                ", property=" + property +
                ", id=" + id +
                ", position=" + position +
                '}';
    }
}
