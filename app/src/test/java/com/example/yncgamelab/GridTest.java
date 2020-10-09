package io.github.bbodin.yncgamelab;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.github.bbodin.yncgamelab.models.CellStatus;
import io.github.bbodin.yncgamelab.models.Grid;
import io.github.bbodin.yncgamelab.utils.Int2;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class GridTest {

    static int w = 10;
    static int h = 5;
    private Grid grid;

    @Before
    public void before() {
        this.grid = new Grid(w,h);
    }
    @Test
    public void gridSize_test() {
        assertEquals(this.grid.getW(),w );
        assertEquals(this.grid.getH(),h );
    }

    @Test
    public void setCell_test() {
        Int2       p1 = new Int2(0,0);
        Int2       p2 = new Int2(1,0);
        Int2       p3 = new Int2(w,0);
        CellStatus s1  = CellStatus.RED;
        CellStatus s2  = CellStatus.EMPTY;
        CellStatus s3  = CellStatus.UNKNOWN;
        this.grid.setCell(p1, s1);
        assertEquals(this.grid.getCellStatus(p1), s1);
        assertEquals(this.grid.getCellStatus(p2), s2);
        assertEquals(this.grid.getCellStatus(p3), s3);
    }
    @After
    public void after() {
        this.grid = new Grid(10,10);
    }
}