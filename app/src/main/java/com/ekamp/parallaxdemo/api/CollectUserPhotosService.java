package com.ekamp.parallaxdemo.api;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ekamp.parallaxdemo.utils.Constants;

import java.io.File;

/**
 * Service utilized to collect a user's photo via the ContentResolver which in turn accesses the
 * ContentProvider which holds the reference to user's photos data.
 *
 * @author Erik Kamp
 * @since v1.0
 */
public class CollectUserPhotosService extends IntentService {

    private static final String TAG = "CollectUserPhotosService", SQLITE_UPPER_LIMIT = " LIMIT 20";

    public CollectUserPhotosService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //First define the type of data we wish to receive.
        final String[] imageMediaProjection = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.TITLE};
        //Define a string in order to determine how we want our columns organized. In this case we just organize by file name.
        final String orderBy = new StringBuffer(MediaStore.Images.Media.TITLE).append(SQLITE_UPPER_LIMIT).toString();

        //Query via the ContentResolver for the current user's photos using our previously defined constraints.
        Cursor userPhotosCursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageMediaProjection, null,
                null, orderBy);

        //If the cursor is null we know that the user has no photos
        if (userPhotosCursor == null) {
            sendBroadCastIntentOnFailure();
            return;
        }

        int userPhotoCount = userPhotosCursor.getCount();

        //Create an array to store path to all the images
        String[] userPhotoPaths = new String[userPhotoCount];
        //Variable to temp hold a pointer to the location of the column that holds information on a photo.
        int dataColumnIndex;

        for (int photoNumber = 0; photoNumber < userPhotoCount; photoNumber++) {
            userPhotosCursor.moveToPosition(photoNumber);
            dataColumnIndex = userPhotosCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            //Store the path of the image
            userPhotoPaths[photoNumber] = userPhotosCursor.getString(dataColumnIndex);
        }
        //Close the cursor in order to free it from memory prevent OOM
        userPhotosCursor.close();
        if (userPhotoPaths.length == 0) {
            sendBroadCastIntentOnFailure();
            return;
        }

        sendBroadCastIntentOnCompletion(userPhotoPaths);
    }

    /**
     * Notifies the listening BroadcastReceiver via an Intent that the collection of user photos has failed.
     */
    private void sendBroadCastIntentOnFailure() {
        Intent userPhotosLocalBroadcast = new Intent(Constants.PHOTO_COLLECTION_BROADCAST_ACTION).putExtra(Constants.PHOTO_COLLECTION_STATUS, false);
        LocalBroadcastManager.getInstance(this).sendBroadcast(userPhotosLocalBroadcast);
    }

    /**
     * Notifies the listening BroadcastReceiver via an Intent that the collection of user photos has succeeded.
     */
    private void sendBroadCastIntentOnCompletion(String[] userPhotoPaths) {
        Intent userPhotosLocalBroadcast = new Intent(Constants.PHOTO_COLLECTION_BROADCAST_ACTION).putExtra(Constants.PHOTO_COLLECTION_STATUS, true).
                putExtra(Constants.PHOTO_COLLECTION_DATA, userPhotoPaths);
        LocalBroadcastManager.getInstance(this).sendBroadcast(userPhotosLocalBroadcast);
    }
}
