package com.ekamp.parallaxdemo.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ekamp.parallaxdemo.R;
import com.ekamp.parallaxdemo.api.CollectUserPhotosService;
import com.ekamp.parallaxdemo.api.DecodeBitmapTask;
import com.ekamp.parallaxdemo.api.UsersPhotosBroadcastReceiver;
import com.ekamp.parallaxdemo.model.UserPhoto;
import com.ekamp.parallaxdemo.utils.Constants;
import com.ekamp.parallaxdemo.view.adapters.UserPhotosAdapter;
import com.ekamp.parallaxdemo.view.callbacks.BroadcastRevieverActivityCallback;
import com.ekamp.parallaxdemo.view.custom_views.ListenableScrollView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Main Activity used to manage all views present in the activity_view_controller layout.
 * This activity will first setup its views and then spawn a backgrounded service to collect
 * 20 of the current user's photos from their Gallery. If the user does not have 20 photos
 * [0, 20) will be shown.
 *
 * @author Erik Kamp
 * @since v1.0
 */
public class ViewControllerActivity extends Activity implements BroadcastRevieverActivityCallback {

    private ListenableScrollView listenableScrollView;
    private FrameLayout bannerImageParallaxContainer;
    private ImageView bannerImageView;
    private RecyclerView userPhotosRecyclerView;

    private static final float PARALLAX_SCROLL_EFFECT = 0.7f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_controller);

        bindViews();
        instanciateScrollViewListener();
        startCollectionOfUserPhotos();
    }

    /**
     * Binds the local instance of the activities views to the views present in our XML.
     */
    private void bindViews() {
        listenableScrollView = (ListenableScrollView) findViewById(R.id.scrollable_content_view);
        bannerImageParallaxContainer = (FrameLayout) findViewById(R.id.banner_parallax_image_container);
        bannerImageView = (ImageView) findViewById(R.id.banner_parallax_imageview);
        userPhotosRecyclerView = (RecyclerView) findViewById(R.id.photo_collection_recyclerview);
    }

    /**
     * Creates and instantiates the activities ScrollViewListener. This listener enables us to create
     * a parallax effect on the banner image present in the activity's layout.
     */
    private void instanciateScrollViewListener() {
        if (listenableScrollView == null || bannerImageParallaxContainer == null) return;

        listenableScrollView.setOnScrollChangedListener(new ListenableScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int changeInX, int changeInY) {
                int scrollViewYPosition = listenableScrollView.getScrollY();
                //Setting the translation allows us to scroll the image .7 (70%) slower than we are scrolling the parent ViewGroup
                bannerImageParallaxContainer.setTranslationY(scrollViewYPosition * PARALLAX_SCROLL_EFFECT);
            }
        });
    }

    /**
     * Launches a backgrounded service via an IntentFilter in order to collect 20 of the current
     * user's photos. On completion of the service an instance of UsersPhotosBroadcastReceiver will
     * call back to this activity with the pertinent data.
     */
    private void startCollectionOfUserPhotos() {
        IntentFilter userPhotosIntentFilter = new IntentFilter(
                Constants.PHOTO_COLLECTION_BROADCAST_ACTION);

        UsersPhotosBroadcastReceiver usersPhotosBroadcastReceiver = new UsersPhotosBroadcastReceiver(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(usersPhotosBroadcastReceiver, userPhotosIntentFilter);

        Intent collectUsersPhotosIntent = new Intent(this, CollectUserPhotosService.class);
        this.startService(collectUsersPhotosIntent);
    }

    /**
     * Simply populates the banner parallax view with the first image present in the users Gallery.
     *
     * @param firstUserPhoto first photo in the user's gallery.
     */
    private void populateBannerParallaxView(String firstUserPhoto) {
        DecodeBitmapTask decodeBitmapTask = new DecodeBitmapTask(bannerImageView);
        decodeBitmapTask.execute(firstUserPhoto);
    }

    /**
     * On Completion of the service, this will set the proper UserPhotosAdapter instance to our local
     * RecyclerView. This effectively creates and populates the user photo grid seen below the description
     * effect block.
     *
     * @param userPhotos Bitmap array of user's photos.
     */
    private void instanciateRecyclerView(String[] userPhotos) {

        ArrayList<UserPhoto> userPhotoArrayList = new ArrayList<>(userPhotos.length);

        for (String photo : userPhotos)
            userPhotoArrayList.add(new UserPhoto(photo));

        userPhotosRecyclerView.setHasFixedSize(true);
//        //The second argument for the layout manage is the span count, or number of items per row for our grid.
        userPhotosRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        userPhotosRecyclerView.setAdapter(new UserPhotosAdapter(userPhotoArrayList));
    }

    /**
     * Adjusts the height of our RecyclerView in order to allow for all photo elements to be displayed at once.
     * If this view is not adjusted and left static say 400dp, this view container will intercept scroll events from
     * the parent ScrollView; unfortunately this makes for a jagged weird experience, so intead we adjust the height of this view
     * based on the number of elements / rows we want to display. This makes it so the entire view smooth scrolls and does not
     * look like a segmented experience to the user.
     * Decided against the calculation as scrolling was not as smooth with larger view sizes.
     *
     * @param numberOfUserPhotos number of photos to be displayed within the RecyclerView.
     */
    private void adjustRecyclerViewHeight(double numberOfUserPhotos) {
        //Each user photo is of height @dimen/user_photo_vertical, and there are 3 photos per grid row.
        //Therefore we can adjust the height to fit the exact height of the photos in the grid with the following :
        //n = # photos , r = height of row
        //height = celi(n/3)*r
//        int newRecyclerViewHeight = (int) (Math.ceil(numberOfUserPhotos / 3.0) * getResources().getDimension(R.dimen.user_photo_vertical));
//        RelativeLayout.LayoutParams recyclerViewLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, newRecyclerViewHeight);
//        recyclerViewLayoutParams.setMargins(0, (int) getResources().getDimension(R.dimen.user_photos_recycler_view_vertical_offset), 0, 0);
//        recyclerViewLayoutParams.addRule(RelativeLayout.BELOW, R.id.banner_parallax_image_container);
//        userPhotosRecyclerView.setLayoutParams(recyclerViewLayoutParams);
//        userPhotosRecyclerView.setSc
    }

    @Override
    public void onPhotosCollectionSuccess(String[] userPhotos) {
        //On main thread populate the RecyclerView ImageViews
        populateBannerParallaxView(userPhotos[0]);
        instanciateRecyclerView(Arrays.copyOfRange(userPhotos, 1, userPhotos.length));
    }

    @Override
    public void onPhotosCollectionFailure() {
        //On Failure we simply do no want to show the RecyclerView
        userPhotosRecyclerView.setVisibility(View.GONE);
    }
}
