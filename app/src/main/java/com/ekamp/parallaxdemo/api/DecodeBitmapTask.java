package com.ekamp.parallaxdemo.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.ekamp.parallaxdemo.R;
import com.ekamp.parallaxdemo.model.BitmapCache;
import com.ekamp.parallaxdemo.utils.Utils;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Task used to decode the Bitmap into the LRU memory cache. This task will also resize all images
 * to 720p(720x1280) format in order to cut down / preserve memory and prevent lag in the UI.
 *
 * @author Erik Kamp
 * @since v1.0
 */
public class DecodeBitmapTask extends AsyncTask<String, Void, Bitmap> {

    private final WeakReference<ImageView> imageViewReference;
    public String imagePath = "";

    /**
     * Constructor to create a new DecodeBitmapTask.
     *
     * @param imageView view in which the decoded Bitmap is to be inflated/populted.
     */
    public DecodeBitmapTask(ImageView imageView) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReference = new WeakReference<>(imageView);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (imageViewReference != null)
            imageViewReference.get().setImageResource(R.drawable.banner);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        imagePath = params[0];
        File userPhotoFile = new File(imagePath);
        if (!userPhotoFile.exists()) return null;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        //Cuts the amount of space in half sacrificing just a little bit of quality.
        //This will not be noticeable with the size of the ImageViews present in this application.
        bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        try {
            //We then decode or image file into a Bitmap, then reduce the Bitmap to 720p to save space
            Bitmap largeOriginalBitmap = BitmapFactory.decodeFile(userPhotoFile.getAbsolutePath(), bitmapOptions),
                    smallerCompressedBitmap = Utils.getResizedBitmap(largeOriginalBitmap, 720, 1280);

            BitmapCache.addBitmapToMemoryCache(imagePath, smallerCompressedBitmap);
            return smallerCompressedBitmap;
        } catch (OutOfMemoryError outOfMemoryError) {
            //This will occur if we cannot load a Bitmap into memory, this will happen with very large images.
            Log.e(getClass().getName(), "Could not allocate enough memory to display this image");
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled())
            bitmap = null;

        //After decoding and compression we then want to load our Bitmap into the ImageView.
        //Due to the fact that the UI is using a RecyclerView we want to make sure this task is
        //not already taking place.
        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            final DecodeBitmapTask bitmapWorkerTask = BitmapCache.getBitmapWorkerTask(imageView);
            if (this == bitmapWorkerTask && imageView != null) {
                imageView.setImageBitmap(bitmap);
                return;
            }
        }
        if (imageViewReference != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
