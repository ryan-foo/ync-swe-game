package io.github.bbodin.yncgamelab.rendering;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.SurfaceHolder;

import androidx.core.content.res.ResourcesCompat;

import io.github.bbodin.yncgamelab.R;
import io.github.bbodin.yncgamelab.models.CellStatus;
import io.github.bbodin.yncgamelab.models.Grid;
import io.github.bbodin.yncgamelab.utils.Int2;

public class GridRenderer implements Grid.Callback, SurfaceHolder.Callback  {


    @Override
    public void gridChanged(Grid universe) {
        this.drawSurfaceView();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private final static String TAG = "RendererObject";
    private Grid grid;
    private SurfaceHolder holder;

    public GridRenderer ( Grid g ) {
        this.grid = g;
        this.grid.setCallBack(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d(TAG, "start surfaceCreated");
        this.holder = surfaceHolder;
        drawSurfaceView();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.d(TAG, "start surfaceChanged");
        drawSurfaceView();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.d(TAG, "start surfaceDestroyed");
        this.holder = null;
    }

    private void drawSurfaceView() {
        Log.d(TAG, "start drawSurfaceView");
        if (grid != null && holder != null) {
            Canvas canvas = holder.lockCanvas();
            this.drawGrid(grid, canvas);
            holder.unlockCanvasAndPost(canvas);
        } else {
            Log.e(TAG, "error in drawSurfaceView");
        }
    }

    private void drawGrid(Grid grid, Canvas canvas) {
        Log.d(TAG, "start drawGrid");

        if (grid == null) return;
        if (canvas == null) return;

        canvas.drawARGB(255, 255, 255, 255);

        Paint gridPaint = new Paint();
        gridPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        gridPaint.setStrokeWidth(10);
        gridPaint.setARGB(135, 0, 0, 0);

        int xcount = grid.getW();
        int ycount = grid.getH();

        for (int n = 0; n < xcount; n++) {
            float xpos = n * canvas.getWidth() / xcount ;
            canvas.drawLine(canvas.getWidth() - xpos, 0,canvas.getWidth() - xpos,  canvas.getHeight(),gridPaint);
        }

        for (int n = 0; n < ycount; n++) {
            float ypos = n * canvas.getHeight() / ycount ;
            canvas.drawLine(canvas.getWidth() - 0, ypos,canvas.getWidth() - canvas.getWidth(), ypos,gridPaint);
        }


        for (int nx = 0; nx < xcount; nx++) {
            for (int ny = 0; ny < ycount; ny++) {

                float xpos1 = nx * canvas.getWidth() / xcount ;
                float ypos1 = ny * canvas.getHeight() / ycount ;
                float xpos2 = (nx +1 ) * canvas.getWidth() / xcount ;
                float ypos2 = (ny+1 ) * canvas.getHeight() / ycount ;

                Int2 pos = new Int2(nx,ny);
                CellStatus status = grid.getCellStatus(pos);

                Paint  cellPaint = new Paint();
                cellPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                cellPaint.setStrokeWidth(5);

                switch (status) {
                    case WALL:
                        cellPaint.setARGB(255, 0, 0, 0);
                        break;

                    case PLAYER:
                        cellPaint.setARGB(255,251, 128, 114);
                        break;

                    case BOX:
                        cellPaint.setARGB(255, 150, 75, 0);
                        break;

                    case UNKNOWN:
                    case EMPTY:
                    default:
                        cellPaint.setARGB(0, 200, 0, 0);
                        break;
                }
                canvas.drawRect(xpos1, canvas.getHeight() - ypos1, xpos2, canvas.getHeight() - ypos2, cellPaint);
            }
        }

    }

}