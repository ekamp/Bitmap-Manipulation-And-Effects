package com.ekamp.parallaxdemo.view.callbacks;

/**
 * Callback utilized to notify the main activity when a backgrounded service has completed via a
 * custom BroadcastReceiver.
 *
 * @author Erik Kamp
 * @since v1.0
 */
public interface BroadcastRevieverActivityCallback {

    /**
     * Notifies the class implementing the callback that user photos from the gallery have been collected.
     *
     * @param userPhotos an array of String paths that correspond to a user's photos.
     */
    void onPhotosCollectionSuccess(String[] userPhotos);

    /**
     * Notifies the class implementing the callback that user photos from the gallery could not be collected,
     * or that the user does not have any photos.
     */
    void onPhotosCollectionFailure();
}