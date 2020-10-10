package io.github.bbodin.yncgamelab.utils;

public class Int2 {

    private final int x;
    private final int y;

    public Int2(int x , int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public Int2 add(Int2 other) {
        return new Int2(this.x + other.x , this.y + other.y);
    }

    @Override
    public String toString() {
        return x +","+ y ;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        if (o == this)
            return true;

        if (o.getClass() != getClass())
            return false;

        Int2 e = (Int2) o;

        return this.getX() == e.getX() && this.getY() == e.getY();
    }
    @Override
    public int hashCode()
    {
        return x + y * 100;
    }



}