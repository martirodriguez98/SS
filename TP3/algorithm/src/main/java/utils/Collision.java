package utils;

import java.util.Optional;

public class Collision {
    private double time;
    private Particle p1; //the one in the list
    private Particle p2;
    private boolean isWall;
    private Walls wall;

    public Collision(double time, Particle p1, Particle p2) {
        this.time = time;
        this.p1 = p1;
        this.p2 = p2;
        this.isWall = false;
    }

    public Collision(double time, Particle p1, Walls wall) {
        this.p1 = p1;
        this.time = time;
        this.wall = wall;
        this.isWall = true;
    }

    public boolean isWall() {
        return isWall;
    }

    public void setWall(boolean wall) {
        isWall = wall;
    }

    public Walls getWall() {
        return wall;
    }

    public void setWall(Walls wall) {
        this.wall = wall;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public Particle getP1() {
        return p1;
    }

    public void setP1(Particle p1) {
        this.p1 = p1;
    }

    public Particle getP2() {
        return p2;
    }

    public void setP2(Particle p2) {
        this.p2 = p2;
    }
}
