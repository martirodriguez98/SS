package utils;

import java.util.List;

public class InitialData {
    private List<Particle> particles;
    private int N,L;

    public InitialData(List<Particle> particles, int n, int l) {
        this.particles = particles;
        N = n;
        L = l;
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public void setParticles(List<Particle> particles) {
        this.particles = particles;
    }

    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }

    public int getL() {
        return L;
    }

    public void setL(int l) {
        L = l;
    }
}
