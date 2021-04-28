package mobisocial.arcade;

import android.view.GestureDetector;
import android.view.MotionEvent;

class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        return true;
    }
}
