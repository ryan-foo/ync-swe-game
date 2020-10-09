package io.github.bbodin.yncgamelab.models;

/**
 * CellStatus defines the value of a cell in a Grid,
 * This type is not very flexible for now, but let's keep it that way.
 *
 *
 */
public enum CellStatus {
    UNKNOWN,
    WALL,
    BOX,
    PLAYER,
    EMPTY;
}
