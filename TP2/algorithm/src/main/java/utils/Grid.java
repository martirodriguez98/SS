package utils;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    private final List<List<Cell>> grid;
    private final boolean periodicGrid;
    private final int m;
    private final int l;

    public Grid(List<Particle> particles, boolean periodicGrid, int m, int l) {
        this.periodicGrid = periodicGrid;
        this.m = m;
        this.l = l;
        this.grid = new ArrayList<>();
        buildGrid(this.l/(double) m);
        fillGrid(particles, this.l/(double) m);
        setNeighbours();

    }

    public void buildGrid(double inc){
        double upperBorder = inc;
        double bottomBorder = 0;
        double leftBorder;
        double rightBorder;
        for(int row = 0 ; row < this.m ; row++){
            leftBorder = 0;
            rightBorder = leftBorder + inc;
            List<Cell> cells = new ArrayList<>();
            this.grid.add(row, cells);
            for(int col = 0; col < this.m; col++){
                this.grid.get(row).add(col, new Cell(row, col, upperBorder, bottomBorder, leftBorder, rightBorder));
                rightBorder += inc;
                leftBorder += inc;
            }
            bottomBorder += inc;
            upperBorder += inc;
        }
    }

    public void fillGrid(List<Particle> particles, double inc){
        for(Particle p : particles){
            Position pos = p.getPosition();
            final int row = (int) Math.floor(pos.getY() / inc);
            final int col = (int) Math.floor(pos.getX() / inc);
            this.grid.get(row).get(col).addParticle(p);
        }
    }

    public void setNeighbours(){
        for(int row = 0; row < m; row++){
            for(int col = 0; col < m ; col++ ){
                final Cell thisCell = this.grid.get(row).get(col);
                Cell UCell, RCell, URCell, BRCell;
                UCell = this.grid.get((row + 1) % m).get(col);
                URCell = this.grid.get((row + 1) % m).get((col + 1) % m);
                BRCell = this.grid.get((row - 1) < 0 ? m - 1: row - 1).get((col + 1) % m);
                RCell = this.grid.get(row).get((col + 1) % m);

                if(this.periodicGrid){
                    if (row == this.m - 1 && col == this.m -1){
                        URCell = this.grid.get(0).get(0);
                    }else if(row == 0 && col == this.m -1){
                        BRCell = this.grid.get(m - 1).get(0);
                    }
                }else {
                    if(row + 1 >= m){
                        UCell = null;
                        URCell = null;
                    }
                    if (row - 1 < 0){
                        BRCell = null;
                    }
                    if(col + 1 >= m){
                        URCell = null;
                        RCell = null;
                        BRCell = null;
                    }
                }

                thisCell.addNeighbours(UCell,URCell,RCell,BRCell);
            }
        }
    }

    public List<List<Cell>> getGrid() {
        return grid;
    }
}


