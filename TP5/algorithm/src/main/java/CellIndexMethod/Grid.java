package CellIndexMethod;

import utils.Pair;
import utils.Particle;
import utils.R;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;

public class Grid {
    private final List<List<Cell>> grid;
    private final int m;
    private final int n;
    private final int w;
    private final int l;
    private final double incX;
    private final double incY;

    public Grid(Map<Particle, R> particles, int m, int n, int l, int w) {
        this.m = m;
        this.l = l;
        this.w = w;
        this.n = n;
        this.incX = this.w / (double) n;
        this.incY = this.l / (double) m;
        this.grid = new ArrayList<>();
        buildGrid(incX, incY);
        fillGrid(particles, incX, incY);
        setNeighbours();

    }

    public void buildGrid(double incX, double incY) {
        double upperBorder = incY;
        double bottomBorder = 0;
        double leftBorder;
        double rightBorder;
        for (int row = 0; row < this.m; row++) {
            leftBorder = 0;
            rightBorder = leftBorder + incX;
            List<Cell> cells = new ArrayList<>();
            this.grid.add(row, cells);
            for (int col = 0; col < this.m; col++) {
                this.grid.get(row).add(col, new Cell(row, col, upperBorder, bottomBorder, leftBorder, rightBorder));
                rightBorder += incX;
                leftBorder += incX;
            }
            bottomBorder += incY;
            upperBorder += incY;
        }
    }

    public void fillGrid(Map<Particle, R> particles, double incX, double incY) {
        for (Map.Entry<Particle, R> entry : particles.entrySet()) {
            Pair pos = entry.getValue().get(0);

            if (pos.getY() >= 0 && pos.getY() < l) {
                final int row = (int) Math.floor(pos.getY() / incY);
                final int col = (int) Math.floor(pos.getX() / incX);
                this.grid.get(row).get(col).addParticle(entry.getKey());
            }

        }
    }

    public void setNeighbours() {
        for (int row = 0; row < m; row++) {
            for (int col = 0; col < n; col++) {
                final Cell thisCell = this.grid.get(row).get(col);
                Cell UCell, RCell, URCell, BRCell;
                UCell = this.grid.get((row + 1) % m).get(col);
                URCell = this.grid.get((row + 1) % m).get((col + 1) % n);
                BRCell = this.grid.get((row - 1) < 0 ? m - 1 : row - 1).get((col + 1) % n);
                RCell = this.grid.get(row).get((col + 1) % n);

                if (row + 1 >= m) {
                    UCell = null;
                    URCell = null;
                }
                if (row - 1 < 0) {
                    BRCell = null;
                }
                if (col + 1 >= n) {
                    URCell = null;
                    RCell = null;
                    BRCell = null;
                }

                thisCell.addNeighbours(UCell, URCell, RCell, BRCell);
            }
        }
    }

    public List<List<Cell>> getGrid() {
        return grid;
    }

    public static int getBestGrid(int distance, double maxR){
        final double grid = distance / (2 * maxR);
        int best = (int) Math.floor(grid);
        if (grid == (int) grid){
            best = (int) grid - 1;
        }
        return best;
    }
}
