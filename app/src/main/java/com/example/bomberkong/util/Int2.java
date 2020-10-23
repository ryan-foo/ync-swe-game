package com.example.bomberkong.util;

public class Int2
{
    public int x;
    public int y;

    public Int2(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public void add(Int2 other){
        this.x += other.getX();
        this.y += other.getY();
    }

    public Int2 addReturn(Int2 other){
        Int2 out = new Int2(this.x + other.getX(), this.y + other.getY());
        return out;
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
