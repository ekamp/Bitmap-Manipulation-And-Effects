package com.ekamp.parallaxdemo.view.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Custom ScrollView utilized to update subscribers via a callback about scroll events within the view.
 * In order to accomplish this we simply override the onScrollChanged method to add our additional callback
 * functionality.
 *
 * @author Erik Kamp
 * @since v1.0
 */
public class ListenableScrollView extends ScrollView {

    /**
     * Custom interface used to notify the listening class of scroll change events.
     */
    public interface OnScrollChangedListener {
        /**
         * Notifies the listening class of changes in the horizontal(x) and vertical(y) scroll position.
         *
         * @param changeInX horizontal scroll change.
         * @param changeInY vertical scroll change.
         */
        void onScrollChanged(int changeInX, int changeInY);
    }

    private OnScrollChangedListener onScrollChangedListener;

    public ListenableScrollView(Context context) {
        super(context);
    }

    public ListenableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListenableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Sets the OnScrollChangedListener instance for the current ListenableScrollView instance.
     *
     * @param onScrollChangedListener OnScrollChangedListener instance.
     */
    public void setOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
        this.onScrollChangedListener = onScrollChangedListener;
    }

    /**
     * Retrieves the current OnScrollChangedListener instance.
     *
     * @return current OnScrollChangedListener instance.
     */
    public OnScrollChangedListener getOnScrollChangedListener() {
        return onScrollChangedListener;
    }

    @Override
    protected void onScrollChanged(int horizontalScrollOrigin, int verticalScrollOrigin, int previousVerticalScrollOrigin, int previousHorizontalScrollOrigin) {
        super.onScrollChanged(horizontalScrollOrigin, verticalScrollOrigin, previousVerticalScrollOrigin, previousHorizontalScrollOrigin);
        if (onScrollChangedListener != null) {
            onScrollChangedListener.onScrollChanged(horizontalScrollOrigin - previousVerticalScrollOrigin, verticalScrollOrigin - previousHorizontalScrollOrigin);
        }
    }
}
