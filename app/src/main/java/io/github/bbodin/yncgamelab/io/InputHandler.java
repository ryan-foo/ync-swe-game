package io.github.bbodin.yncgamelab.io;

import io.github.bbodin.yncgamelab.utils.Float2;

public class InputHandler implements InputListener.Callback  {

    private SwipeAction onLeftSwipeAction;
    private SwipeAction onRightSwipeAction;
    private SwipeAction onUpSwipeAction;
    private SwipeAction onDownSwipeAction;
    private ClickAction onClickAction;

    public void setOnLeftSwipeAction(SwipeAction onLeftSwipeAction) {
        this.onLeftSwipeAction = onLeftSwipeAction;
    }

    public void setOnRightSwipeAction(SwipeAction onRightSwipeAction) {
        this.onRightSwipeAction = onRightSwipeAction;
    }

    public void setOnUpSwipeAction(SwipeAction onUpSwipeAction) {
        this.onUpSwipeAction = onUpSwipeAction;
    }

    public void setOnDownSwipeAction(SwipeAction onDownSwipeAction) {
        this.onDownSwipeAction = onDownSwipeAction;
    }

    public void setOnClickAction(ClickAction onClickAction) {
        this.onClickAction = onClickAction;
    }


    @Override
    public void onLeftSwipe(Float2 startPos, Float2 endPos) {
        if (onLeftSwipeAction != null) onLeftSwipeAction.execute(startPos, endPos);
    }

    @Override
    public void onRightSwipe(Float2 startPos, Float2 endPos) {
        if (onRightSwipeAction != null) onRightSwipeAction.execute(startPos, endPos);
    }

    @Override
    public void onUpSwipe(Float2 startPos, Float2 endPos) {
        if (onUpSwipeAction != null) onUpSwipeAction.execute(startPos, endPos);

    }

    @Override
    public void onDownSwipe(Float2 startPos, Float2 endPos) {
        if (onDownSwipeAction != null) onDownSwipeAction.execute(startPos, endPos);

    }
    @Override
    public void onClick(Float2 pos) {
        if (onClickAction != null) onClickAction.execute(pos);
    }




}
