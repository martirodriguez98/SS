package grid_utils;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    private List<Particle> particles;

    private Cell UCell; //upper cell
    private Cell URCell; //upper right cell
    private Cell RCell; //right cell
    private Cell BRCell; //bottom right cell

    private int row;
    private int col;

    private double upperBorder;
    double bottomBorder;
    double leftBorder;
    double rightBorder;

    public Cell(int row, int col, double upperBorder, double bottomBorder, double leftBorder, double rightBorder) {
        this.particles = new ArrayList<>();
        this.row = row;
        this.col = col;
        this.upperBorder = upperBorder;
        this.bottomBorder = bottomBorder;
        this.leftBorder = leftBorder;
        this.rightBorder = rightBorder;
    }

    public void addNeighbours(Cell UCell, Cell URCell, Cell RCell, Cell BRCell) {
        this.UCell = UCell;
        this.URCell = URCell;
        this.RCell = RCell;
        this.BRCell = BRCell;
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public void setParticles(List<Particle> particles) {
        this.particles = particles;
    }

    public void addParticle(Particle p) {
        this.particles.add(p);
    }

    public Cell getUCell() {
        return UCell;
    }

    public void setUCell(Cell UCell) {
        this.UCell = UCell;
    }

    public Cell getURCell() {
        return URCell;
    }

    public void setURCell(Cell URCell) {
        this.URCell = URCell;
    }

    public Cell getRCell() {
        return RCell;
    }

    public void setRCell(Cell RCell) {
        this.RCell = RCell;
    }

    public Cell getBRCell() {
        return BRCell;
    }

    public void setBRCell(Cell BRCell) {
        this.BRCell = BRCell;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public double getUpperBorder() {
        return upperBorder;
    }

    public void setUpperBorder(double upperBorder) {
        this.upperBorder = upperBorder;
    }

    public double getBottomBorder() {
        return bottomBorder;
    }

    public void setBottomBorder(double bottomBorder) {
        this.bottomBorder = bottomBorder;
    }

    public double getLeftBorder() {
        return leftBorder;
    }

    public void setLeftBorder(double leftBorder) {
        this.leftBorder = leftBorder;
    }

    public double getRightBorder() {
        return rightBorder;
    }

    public void setRightBorder(double rightBorder) {
        this.rightBorder = rightBorder;
    }

    @Override
    public String toString() {
        return "[" + row +
                ", " + col +
                "] - nb: " +
                "U = [" + (UCell == null ? "*" : UCell.getRow()) + ", " + (UCell == null ? "*" : UCell.getCol()) + "] -" +
                "UR = [" + (URCell == null ? "*" : URCell.getRow()) + ", " + (URCell == null ? "*" : URCell.getCol()) + "] -" +
                "BR = [" + (BRCell == null ? "*" : BRCell.getRow()) + ", " + (BRCell == null ? "*" : BRCell.getCol()) + "] -" +
                "R = [" + (RCell == null ? "*" : RCell.getRow()) + ", " + (RCell == null ? "*" : RCell.getCol()) + "]\n" + particles ;
    }
}
