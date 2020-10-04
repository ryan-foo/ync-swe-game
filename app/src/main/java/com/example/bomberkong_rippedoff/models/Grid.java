package com.example.bomberkong_rippedoff.models;

import android.util.Log;

import com.example.bomberkong_rippedoff.utils.Int2;

import java.util.HashMap;
import java.util.Map;


public class Grid {
    private static final String TAG = "Grid";
    public static final Int2 fixed = new Int2( 0, 0);
    public static final Int2 left  = new Int2(-1, 0);
    public static final Int2 right = new Int2(+1, 0);
    public static final Int2 down  = new Int2( 0,-1);
    public static final Int2 up    = new Int2( 0,+1);
    private int w,h;
    private Map<Int2,CellStatus> gridMap;

    public int getW() { return w; }
    public int getH() { return h; }


    /**
     * The constructor initializes the grid,
     * and fill it in with w*h Empty cells.
     * @param w Width
     * @param h Height
     */
    public Grid (int w, int h) {
        this.w = w;
        this.h = h;
        this.gridMap = new HashMap<Int2, CellStatus>();
        this.clear();
    }


    // Grid can be edit live for debug
    private boolean editable = false;

    /**
     * Return True only if the grid is editable.
     * @return editable value
     */
    public boolean isEditable() {return editable;}

    /**
     * Change the value of editable.
     * @param checked new value for editable
     */
    public void setEditable(boolean checked) {this.editable = checked;}

    /**
     * Return the value of the Cell at position pos.
     * @param pos Position to test.
     * @return value of the Cell.
     */
    public boolean hasCell(Int2 pos) {
        return (this.gridMap.containsKey(pos));
    }

    /**
     * Edit the value of a Cell.
     * This does not cast the change to listeners.
     * @param pos  Position to edit
     * @param status New value to set.
     */
    public void setCell(Int2 pos, CellStatus status) {
        this.gridMap.put(pos, status);
    }

    /**
     * Clean the grid, and fill it with empty cells.
     */
    protected void clear() {
        this.gridMap.clear();
        for (int x = 0 ; x < w ; x++) {
            for (int y = 0 ; y < h ; y++) {
                this.setCell(new Int2(x,y), CellStatus.EMPTY);
            }
        }
    }

    // Callback when the grid changes
    public interface Callback {void gridChanged ( Grid grid ) ; }
    public void setCallBack(Callback c) { callback = c; }
    public void addCallBack (Callback c )  { this.callback = c; }
    private Callback callback = null;

    /**
     * This function is used by the Grid to callback
     * its observer (GridRenderer so far)
     */
    protected void castChanges() {
        if (callback != null) {callback.gridChanged(this);}
        else {
            Log.w(TAG,"Callback is not available.");
        }
    }

    /**
     * getCellStatus return the current value of a cell in the grid
     * at the position pos.
     * If the value in not defined yet, it default to EMPTY.
     * Important to consider the case where it's out the grid,
     * Then it's UNKNOWN.
     *
     * @param pos Position evaluated
     * @return Cell status at the position pos, UNKNOWN if undefined.
     */
    public CellStatus getCellStatus(Int2 pos) {
        // FIXME : The default value could be better managed here
        if (this.gridMap.containsKey(pos)) {
            return this.gridMap.get(pos);
        } else {
            return CellStatus.UNKNOWN ;
        }
    }
}
