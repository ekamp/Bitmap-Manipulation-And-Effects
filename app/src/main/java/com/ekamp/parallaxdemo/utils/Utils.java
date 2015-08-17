package com.ekamp.parallaxdemo.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Android utilities class.
 *
 * @author Erik Kamp
 * @since v1.0
 */
public class Utils {

    /**
     * Given a Bitmap resizes the Bitmap to the specified width and height. Specifying a smaller
     * height and width will ensure the Bitmap image takes up less space in memory.
     *
     * @param originalBitmap Bitmap to be resized.
     * @param newWidth       width to convert original Bitmap to.
     * @param newHeight      height to convert original Bitmap to.
     * @return new Bitmap instance with specified height and width.
     */
    public static Bitmap getResizedBitmap(Bitmap originalBitmap, int newWidth, int newHeight) {
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        //A matrix object is used here to scale our original Bitmap correctly
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        //After scaled we then create a new Bitmap with the specified height and width.
        Bitmap resizedBitmap = Bitmap.createBitmap(
                originalBitmap, 0, 0, width, height, matrix, false);

        //Make sure to recycle the old Bitmap so it is no longer in memory
        originalBitmap.recycle();
        return resizedBitmap;
    }
}
