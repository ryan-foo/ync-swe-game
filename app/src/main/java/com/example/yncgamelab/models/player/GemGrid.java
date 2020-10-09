package io.github.bbodin.yncgamelab.models.player;


import android.util.Log;

import io.github.bbodin.yncgamelab.models.CellStatus;
import io.github.bbodin.yncgamelab.models.Grid;
import io.github.bbodin.yncgamelab.utils.Int2;

/**
 * Grid model the grid of Gem.
 * This include basic operations on gems :
 *  - Create: Create a new gem in the grid and return true or false of the gem is correctly created.
 *  - Edit: Change the value of a cell
 *  - Move: Swap with the left, right, up or down
 */


public class GemGrid extends Grid {

    private static final String TAG = "GemGrid";

    public GemGrid (int w, int h) {
        super(w,h);
    }

    public boolean Create (Int2 pos, CellStatus status) {
        if (!this.isEditable()) return false;
        Log.d(TAG,"Move(" + pos.getX() + "," +pos.getY() + "," + status.toString() + ")");
        if (this.hasCell(pos)) {
            return false;
        }
        this.setCell(pos, status);
        startGemGridUpdate();
        return true;
    }

    public boolean Change (Int2 pos) {
        if (!this.isEditable()) return false;
        Log.d(TAG,"Change(" + pos.getX() + "," +pos.getY() + ")");
        if (this.hasCell(pos)) {
            CellStatus status = this.getCellStatus(pos);
            Log.d(TAG," Previous status is (" + pos.getX() + "," +pos.getY() + "," + status.toString() + ")");
            CellStatus new_status = CellStatus.values()[(status.ordinal() + 1) % CellStatus.values().length];
            if (new_status == CellStatus.UNKNOWN) {
                new_status = CellStatus.values()[(status.ordinal() + 2) % CellStatus.values().length];
            }
            this.setCell(pos, new_status);
            Log.d(TAG," New status is (" + pos.getX() + "," +pos.getY() + "," + new_status.toString() + ")");
            startGemGridUpdate();
            return true;
        } else {
            Log.d(TAG,"Position not found");
        }
        return false;
    }

    public boolean Move (Int2 pos, Int2 direction) {


        // Move swap me with pos+direction
        Int2 pos1 = pos;
        Int2 pos2 = pos.add(direction);

        CellStatus val1 = this.getCellStatus(pos1);
        CellStatus val2 = this.getCellStatus(pos2);

        if (val1 == CellStatus.WALL || val2 == CellStatus.WALL) { // We cannot swap against the wall
            return false;
        }

        this.setCell(pos1,val2);
        this.setCell(pos2,val1);
        this.startGemGridUpdate();
        return true;

    }

    public static GemGrid generateDemo () {
        GemGrid g = new GemGrid (13,8);
        g.Reset();
        return g;
    }

    public void Reset () {
        this.clear();
        for (int i = 0; i <= 8; i ++){
            this.setCell(new Int2(0, i), CellStatus.WALL);
            this.setCell(new Int2(this.getW() - 1, i), CellStatus.WALL);
        }
        for (int i = 0; i <= 13; i ++){
            this.setCell(new Int2(i, 0), CellStatus.WALL);
            this.setCell(new Int2(i, this.getH() - 1), CellStatus.WALL);
        }
        this.setCell(new Int2(1, 1), CellStatus.PLAYER);
        this.castChanges();

    }

    public void startGemGridUpdate() {
        castChanges();
    }

}