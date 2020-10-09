package io.github.bbodin.yncgamelab.io;

import io.github.bbodin.yncgamelab.utils.Float2;

/**
 * SwipeAction correspond to a moving action on the screen.
 * executes takes the start and end point of touch.
 */
public interface SwipeAction extends Action {
    void execute(Float2 a, Float2 b);
}
