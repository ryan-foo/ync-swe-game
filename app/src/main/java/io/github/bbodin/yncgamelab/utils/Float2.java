package io.github.bbodin.yncgamelab.utils;

public class Float2 {

    private final float x;
    private final float y;

    public Float2(float x , float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }


    public Float2 add(Float2 other) {
        return new Float2(this.x + other.x , this.y + other.y);
    }

    @Override
    public String toString() {
        return x +","+ y ;
    }

    public Float2 div(Float2 other) {
        return new Float2(this.x / other.x , this.y / other.y);
    }

    public Float2 sub(Float2 other) {
        return new Float2(this.x - other.x , this.y - other.y);
    }
}