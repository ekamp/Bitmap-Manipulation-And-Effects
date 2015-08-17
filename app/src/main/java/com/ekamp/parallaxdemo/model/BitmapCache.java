package com.ekamp.parallaxdemo.model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import com.ekamp.parallaxdemo.R;
import com.ekamp.parallaxdemo.api.DecodeBitmapTask;

/**
 * An LRU singleton instance class used to hold ImageView Bitmaps in order to prevent such Bitmaps
 * from being decoded each time they are seen on screen.
 *
 * @author Erik Kamp
 * @since v1.0
 */
public class BitmapCache {

    private static LruCache<String, Bitmap> bitmapLruCache;
    private static final int LRU_SIZE = (int) Runtime.getRuntime().maxMemory() / 1024;

    /**
     * Retrieves the Singleton instance of the LRU Bitmap Cache.
     *
     * @return Singleton instance of the LRU Bitmap Cache
     */
    public static LruCache<String, Bitmap> getInstance() {
        if (bitmapLruCache == null) {
            bitmapLruCache = new LruCache<>(LRU_SIZE);
        }
        return bitmapLruCache;
    }

    /**
     * Retrieves Bitmap from the LRU memory Cache.
     *
     * @param key String path to the Image file in memory retrieved from the ContentResolver.
     * @return Bitmap from the LRU memory Cache, or null if such a Bitmap has been evicted or is
     * not present in the memory cache.
     */
    public static Bitmap getBitmapFromMemCache(String key) {
        return getInstance().get(key);
    }

    public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            getInstance().put(key, bitmap);
        }
    }

    /**
     * Loads the Bitmap on a backgrounded thread associated with the String path via the DecodeBitmapTask.
     *
     * @param imageKey  path to image on the current device.
     * @param imageView view to inflate/populate image.
     */
    public static void loadBitmap(String imageKey, ImageView imageView) {
        final Bitmap bitmap = getBitmapFromMemCache(imageKey);
        if (bitmap != null) {
            //Cache Hit
            imageView.setImageBitmap(bitmap);
        } else {
            //Cache Miss
            if (cancelPotentialWork(imageKey, imageView)) {

                final DecodeBitmapTask descodeBitmapTask = new DecodeBitmapTask(imageView);
                final AsyncDrawable asyncDrawable = new AsyncDrawable(Resources.getSystem(),
                        null,
                        descodeBitmapTask);
                imageView.setImageDrawable(asyncDrawable);
                descodeBitmapTask.execute(imageKey);
            }
        }
    }

    /**
     * If the Task to decode the currently seen Bitmap already exists we do not want to create a duplicate task.
     * This will check for such duplications.
     *
     * @param imagePath path to the image on the current device.
     * @param imageView view to hold the image.
     * @return true if there is a task associated with the ImageView false otherwise.
     */
    public static boolean cancelPotentialWork(String imagePath, ImageView imageView) {
        final DecodeBitmapTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            String bitmapData = bitmapWorkerTask.imagePath;
            // If bitmapData is not yet set or it differs from the new imagePath
            if (bitmapData == null || bitmapData.length() == 0 || bitmapData.equalsIgnoreCase(imagePath)) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    /**
     * Retrieves the Bitmap decode task associated with the current ImageView.
     *
     * @param imageView view to check if DecodeBitmapTask already exists.
     * @return DecodeBitmapTask or null if such a task does not exist for this ImageView.
     */
    public static DecodeBitmapTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getDecodeBitmapTask();
            }
        }
        return null;
    }
}
