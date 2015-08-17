package com.ekamp.parallaxdemo.api;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.ekamp.parallaxdemo.utils.Constants;
import com.ekamp.parallaxdemo.view.callbacks.BroadcastRevieverActivityCallback;

/**
 * BroadcastReviever that is used to listen for photos downloaded Intent events coming from a local Broadcast.
 *
 * @author Erik Kamp
 * @since v1.0
 */
public class UsersPhotosBroadcastReceiver extends BroadcastReceiver {

    private BroadcastRevieverActivityCallback callback;

    /**
     * Callback used to notify the registered activity that the user's photos have been downloaded.
     *
     * @param callback activity callback instance.
     */
    public UsersPhotosBroadcastReceiver(BroadcastRevieverActivityCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.hasExtra(Constants.PHOTO_COLLECTION_STATUS) && intent.getBooleanExtra(Constants.PHOTO_COLLECTION_STATUS, false))
            callback.onPhotosCollectionSuccess(intent.getStringArrayExtra(Constants.PHOTO_COLLECTION_DATA));
        else
            callback.onPhotosCollectionFailure();
    }
}
