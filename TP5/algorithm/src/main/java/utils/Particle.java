package utils;

import java.util.Objects;

public class Particle {
    private int id;
    private double radio;
    private double mass;

    public Particle(int id, double radio, double mass) {
        this.id = id;
        this.radio = radio;
        this.mass = mass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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


//
//    public double distance(final Particle other) {
//        return Math.hypot(position.getX() - other.getPosition().getX() - radio - other.radio, position.getY() - other.getPosition().getY() - radio - other.radio);
//    }
//
//    public boolean collidesWithWall(final int L, final int W) {
//        return position.getX() + radio <= 0 || position.getX() + radio >= W || position.getY() + radio <= 0 || position.getY() + radio >= L;
//    }
//
//    public boolean collides(final Particle particle) {
//        return distance(particle) <= 0;
//    }
//
//    public boolean collides(final Collection<Particle> particles, int L, int W) {
//        return collidesWithWall(L, W) || particles.stream().anyMatch(this::collides);
//    }

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
        return Objects.hash(id);
    }
}
