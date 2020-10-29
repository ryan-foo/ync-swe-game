package com.example.bomberkong;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.bomberkong.util.Int2;

import java.util.Map;
import java.util.HashMap;

public class Grid
{
    private Map<Int2, CellStatus> gridMap;
    private int w;
    private int h;
    private Callback callback = null;


    public Grid(int w, int h){
        this.w = w;
        this.h = h;
        this.gridMap = new HashMap<Int2, CellStatus>();
        reset();
    }

    public Map getMap(){
        return gridMap;
    }

    public int getW(){
        return w;
    }

    public int getH(){
        return h;
    }

    public void setCell(Int2 pos, CellStatus status) {
        gridMap.put(pos, status);
    }

    public CellStatus getCellStatus(Int2 pos){
        return gridMap.get(pos);
    }

    public String getCellString(Int2 pos){
        CellStatus temp = gridMap.get(pos);
        if (temp == CellStatus.WALL){
            return "WALL";
        } else if (temp == CellStatus.PLAYER){
            return "PLAYER";
        } else if (temp == CellStatus.EMPTY){
            return "EMPTY";
        } else {
            return "NULL";
        }
    }

    // Callback when grid changes
    public interface Callback {void gridChanged ( Grid grid ) ; }
    public void setCallBack(Callback c) { callback = c; }
    public void addCallBack (Callback c )  { this.callback = c; }

    void draw(Canvas canvas, Paint paint) {
        if (canvas == null) return;

        canvas.drawARGB(255, 255, 255, 255);

        Paint gridPaint = new Paint();
        gridPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        gridPaint.setStrokeWidth(10);
        gridPaint.setARGB(135, 0, 0, 0);


        int xcount = getW();
        int ycount = getH();

        for (int n = 0; n < xcount; n++) {
            float xpos = n * canvas.getWidth() / xcount;
            canvas.drawLine(canvas.getWidth() - xpos, 0, canvas.getWidth() - xpos, canvas.getHeight(), gridPaint);
        }
        // Can you draw inside the grid? Draw BitMap
        // If CellStatus BitMap
        for (int n = 0; n < ycount; n++) {
            float ypos = n * canvas.getHeight() / ycount;
            canvas.drawLine(canvas.getWidth() - 0, ypos, canvas.getWidth() - canvas.getWidth(), ypos, gridPaint);
        }
    }

    public void reset(){
        for (int x = 0; x < getW(); x ++){
            for (int y = 0; y < getH(); y ++){
                this.setCell(new Int2 (x, y), CellStatus.EMPTY);
            }
        }
        for(int x = 0; x < getW(); x ++){
            this.setCell(new Int2 (x, 0), CellStatus.WALL);
            this.setCell(new Int2 (x, getH() - 1), CellStatus.WALL);
        }
        for (int y = 0; y < getH(); y ++) {
            this.setCell(new Int2 (0, y), CellStatus.WALL);
            this.setCell(new Int2 (getW() - 1, y), CellStatus.WALL);
        }
    }
}
