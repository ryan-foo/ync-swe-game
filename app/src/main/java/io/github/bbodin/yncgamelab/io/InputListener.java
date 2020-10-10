package io.github.bbodin.yncgamelab.io;

import android.view.MotionEvent;
import android.view.View;

import io.github.bbodin.yncgamelab.utils.Float2;

/**
 * The InputListener object can be add as a Listener for any View object.
 * It will wait for touch events and interpret them as
 *  - LeftSwipe, RightSwipe, UpSwipe, DownSwipe, Click
 * This simplifies the user actions.
 *
 * In order to get back these event, one has to add a callback function using the addCallback
 *
 */
public class InputListener implements View.OnTouchListener {

    private Float2   down_pos;
    private Float2   up_pos;
    private Callback callback;

    private final static int MIN_DISTANCE   = 30;
    private final static int CLICK_DISTANCE = 5;
    private final static String TAG = "InputController";


    /**
     * Set the callback object to call
     * when touch actions occur.
     * @param cb The callback object
     */
    public void setCallback(Callback cb) {
        this.callback = cb;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        Float2 dim     = new Float2(view.getWidth(), view.getHeight());
        Float2 current = new Float2(motionEvent.getX(), motionEvent.getY());
        Float2 curperc = current.div(dim);


        switch(motionEvent.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                down_pos = new Float2(motionEvent.getX(), motionEvent.getY());
                break;
            case MotionEvent.ACTION_UP:
                up_pos = new Float2(motionEvent.getX(), motionEvent.getY());
                Float2 delta = up_pos.sub(down_pos);
                Float2 upper = up_pos.div(dim);
                Float2 downper = down_pos.div(dim);

                if (Math.sqrt(delta.getX()*delta.getX() + delta.getY()*delta.getY()) < CLICK_DISTANCE) {
                    callback.onClick(upper);
                } else {
                    if (Math.abs(delta.getX()) > Math.abs(delta.getY())) {

                        if (delta.getX() < 0) {callback.onLeftSwipe(downper,upper); break;}
                        if (delta.getX() > 0) {callback.onRightSwipe(downper,upper);break;}

                    } else {

                        if (delta.getY() > 0) {callback.onDownSwipe(downper,upper); break;}
                        if (delta.getY() < 0) {callback.onUpSwipe(downper,upper);break;}
                    }
                }


                break;
        }
        return true;
    }

    /**
     * This is the interface a CallBack object has to follow
     * in order to be a valid CallBack for the InputListener.
     */
    public interface Callback {

        /**
         * triggered when a touch click from right to left occurs.
         * @param start Position when touch started.
         * @param end Position when touch released.
         */
        void onLeftSwipe (Float2 start, Float2 end) ;

        /**
         * triggered when a touch click from left to right occurs.
         * @param start Position when touch started.
         * @param end Position when touch released.
         */
        void onRightSwipe (Float2 start, Float2 end ) ;

        /**
         * triggered when a touch click from down to up occurs.
         * @param start Position when touch started.
         * @param end Position when touch released.
         */
        void onUpSwipe ( Float2 start, Float2 end ) ;

        /**
         * triggered when a touch click from up to down occurs.
         * @param start Position when touch started.
         * @param end Position when touch released.
         */
        void onDownSwipe (Float2 start, Float2 end) ;

        /**
         * triggered when a simple touch click occurs (no movements).
         * @param pos Position when touch click released.
         */
        void onClick ( Float2 pos ) ;
    }
}