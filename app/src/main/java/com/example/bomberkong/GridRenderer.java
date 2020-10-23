package com.example.bomberkong;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;

import com.example.bomberkong.model.CellStatus;
import com.example.bomberkong.model.Grid;
import com.example.bomberkong.util.Int2;

public class GridRenderer implements Grid.Callback, SurfaceHolder.Callback{
    private Grid grid;
    private SurfaceHolder holder;
    private Bitmap bitmapfood;
    private Bitmap bitmapplayer;
    private Bitmap bitmapwall;
    private Context context;

    public GridRenderer (Grid grid, Context context){
        this.grid = grid;
        bitmapfood = BitmapFactory.decodeResource(context.getResources(), R.drawable.banana);
        bitmapplayer = BitmapFactory.decodeResource(context.getResources(), R.drawable.monkey);
        bitmapwall = BitmapFactory.decodeResource(context.getResources(), R.drawable.wall);
        //to do: resize bitmapfood
    }

    private void drawSurfaceView() {
        if (grid != null && holder != null) {
            Canvas canvas = holder.lockCanvas();
            this.drawGrid(grid, canvas);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    private void drawGrid(Grid grid, Canvas canvas){
        if (grid == null) return;
        if (canvas == null) return;

        canvas.drawARGB(255, 255, 255, 255);
        Paint gridPaint = new Paint();
        gridPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        gridPaint.setStrokeWidth(10);
        gridPaint.setARGB(135, 0, 0, 0);

        int width = canvas.getWidth() / grid.getW();
        int height = canvas.getHeight() / grid.getH();
        bitmapfood = Bitmap.createScaledBitmap(bitmapfood, width, height, false);
        bitmapplayer = Bitmap.createScaledBitmap(bitmapplayer, width, height, false);
        bitmapwall = Bitmap.createScaledBitmap(bitmapwall, width, height, false);

        int xcount = grid.getW();
        int ycount = grid.getH();

        for (int n = 0; n < xcount; n++) {
            float xpos = n * canvas.getWidth() / xcount;
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
                float xpos2 = (nx + 1) * canvas.getWidth() / xcount ;
                float ypos2 = (ny + 1) * canvas.getHeight() / ycount ;
                Int2 pos = new Int2(nx,ny);
                CellStatus status = grid.getCellStatus(pos);

                switch (status) {
                    case FRUIT:
                        canvas.drawBitmap(bitmapfood, xpos1, ypos1, null);
                        break;

                    case PLAYER:
                        canvas.drawBitmap(bitmapplayer, xpos1, ypos1, null);
                        break;

                    case WALL:
                        canvas.drawBitmap(bitmapwall, xpos1, ypos1, null);
                        break;
                }
            }
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        this.holder = holder;
        drawSurfaceView();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        drawSurfaceView();
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        this.holder = null;
    }

    @Override
    public void gridChanged(Grid grid) {
        this.drawSurfaceView();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
