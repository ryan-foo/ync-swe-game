package io.github.bbodin.yncgamelab.models.player;

import android.util.Log;

import io.github.bbodin.yncgamelab.io.SwipeAction;
import io.github.bbodin.yncgamelab.utils.Assert;
import io.github.bbodin.yncgamelab.utils.Float2;
import io.github.bbodin.yncgamelab.utils.Int2;


public class MoveAction implements SwipeAction {

    private static final String TAG = "MoveAction";
    private GemGrid gemGrid;
    private Int2 direction;

    public MoveAction (GemGrid g,  Int2 d) {
        this.gemGrid = g;
        this.direction = d;
    }

    private Int2 surfaceViewPos2GridPos (Float2 svpos) {

        Assert.isTrue(svpos.getX() <= 1, "Only support position as percent.");
        Assert.isTrue(svpos.getY() <= 1, "Only support position as percent.");
        Assert.isTrue(svpos.getX() >= 0, "Only support position as percent.");
        Assert.isTrue(svpos.getY() >= 0, "Only support position as percent.");

        int vx =  (int) (svpos.getX() * this.gemGrid.getW());
        int vy =  (int) ((1 - svpos.getY()) * this.gemGrid.getH());

        Int2 gridPos = new Int2( vx, vy );

        Log.d(TAG, "surfaceViewPos2GridPos (" + svpos + ") = " + gridPos);

        return gridPos;
    }


    @Override
    public void execute(Float2 startPos, Float2 endPos) {
        Int2 gridPos = surfaceViewPos2GridPos (startPos);
        this.gemGrid.Move(gridPos, this.direction);
    }
}
