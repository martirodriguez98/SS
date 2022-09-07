package utils;

import java.util.Collection;
import java.util.Random;

public class Particle {
    private double radio;
    private double mass;
    private int id;
    private Position position; //if we need the time in the future, we save it as HashMap<time,position>
    private double v;
    private double theta;

    public Particle(double radio, double mass, int id) {
        this.radio = radio;
        this.mass = mass;
        this.id = id;
    }

    public Particle(double radio, double mass, int id, double v, double theta) {
        this.id = id;
        this.radio = radio;
        this.mass = mass;
        this.v = v;
        this.theta = theta;
    }

    public Particle(int id, double radio, double mass, Position position, double v, double theta) {
        this.radio = radio;
        this.mass = mass;
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

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
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

    public static Particle randomParticle(final Random randomGen, final int id, final double radio, final double speed, final int L, final double mass) {
        double x = randDouble(randomGen, radio, L-radio);
        double y = randDouble(randomGen, radio, L-radio);
        double v = randDouble(randomGen, 0, speed);
        double theta = randDouble(randomGen, -Math.PI, Math.PI);
        return new Particle(id, radio, mass, new Position(x,y), v, theta);
    }

    public static double randDouble(final Random randomGen, final double min, final double max){
        return min + randomGen.nextDouble() * (max-min);
    }

    public double distance(final Particle other){
        return Math.hypot(position.getX() -other.getPosition().getX(), position.getY() - other.getPosition().getY() - radio - other.radio);
    }

    public boolean collides(final Particle particle, int L){
        return distance(particle) <= 0;
    }

    public boolean collides(final Collection<Particle> particles, int L){
        return particles.stream().anyMatch(p -> collides(p, L));
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
