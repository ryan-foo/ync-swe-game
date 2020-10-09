package io.github.bbodin.yncgamelab.io;

import io.github.bbodin.yncgamelab.utils.Float2;

/**
 * A ClickAction corresponds to a single tap somewhere in the screen.
 */
public interface ClickAction extends Action {
    /**
     * Actions are built with a specific behavior
     * The execute activate this bahaviour click actions
     * need the position of the tap.
     * @param a Position of the tap in the screen.
     */
    void execute(Float2 a);
}
