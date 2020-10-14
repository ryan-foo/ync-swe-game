package com.example.bomberkong;

import com.example.bomberkong.util.Int2;

public interface Cell
{
    boolean isDestroyable();
    boolean isCollidable();
    Int2 getPosition();
}
