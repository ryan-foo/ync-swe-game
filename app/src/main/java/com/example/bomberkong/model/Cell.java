package com.example.bomberkong.model;

import com.example.bomberkong.util.Int2;

public interface Cell
{
    boolean isDestroyable();
    boolean isCollidable();
    Int2 getPosition();
}
