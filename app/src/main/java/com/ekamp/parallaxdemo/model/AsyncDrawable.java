package com.ekamp.parallaxdemo.model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.ekamp.parallaxdemo.api.DecodeBitmapTask;

import java.lang.ref.WeakReference;

/**
 * Asynchronous Drawable used to hold the Task that is assigned to load/decode it's respective Bitmap.
 * This class prevents more than one Task running at a time to decode a Bitmap.
 *
 * @author Erik Kamp
 * @since v1.0
 */
public class AsyncDrawable extends BitmapDrawable {
    private final WeakReference<DecodeBitmapTask> bitmapWorkerTaskReference;

    public AsyncDrawable(Resources res, Bitmap bitmap, DecodeBitmapTask bitmapWorkerTaskReference) {
        super(res, bitmap);
        this.bitmapWorkerTaskReference = new WeakReference<>(bitmapWorkerTaskReference);
    }

    public DecodeBitmapTask getDecodeBitmapTask() {
        return bitmapWorkerTaskReference.get();
    }

}
