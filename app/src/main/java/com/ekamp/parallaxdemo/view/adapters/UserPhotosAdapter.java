package com.ekamp.parallaxdemo.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ekamp.parallaxdemo.R;
import com.ekamp.parallaxdemo.model.BitmapCache;
import com.ekamp.parallaxdemo.model.UserPhoto;

import java.util.ArrayList;

/**
 * Adapter to be used with a RecyclerView in order to display a user's photos within various ImageViews.
 *
 * @author Erik Kamp
 * @since v1.0
 */
public class UserPhotosAdapter extends RecyclerView.Adapter<UserPhotosAdapter.ViewHolder> {

    private final ArrayList<UserPhoto> userPhotoArrayList;

    /**
     * Creates an instance of the UserPhotosAdapter.
     *
     * @param userPhotoArrayList list of UserPhoto objects pertaining to the current user's photos.
     */
    public UserPhotosAdapter(ArrayList<UserPhoto> userPhotoArrayList) {
        this.userPhotoArrayList = userPhotoArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View userPhotoImageView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_photo_view, parent, false);
        return new ViewHolder((ImageView) userPhotoImageView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {
            BitmapCache.loadBitmap(userPhotoArrayList.get(position).getPhoto(), holder.photoView);
        } catch (OutOfMemoryError outOfMemoryError) {
            Log.e(getClass().getName(), "Cannot allocate enough memory to populate imageView");
        }
    }

    @Override
    public int getItemCount() {
        if (userPhotoArrayList == null) return 0;
        return userPhotoArrayList.size();
    }

    /**
     * Inner ViewHolder class to be used with UserPhotosAdapter.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView photoView;

        public ViewHolder(ImageView photoView) {
            super(photoView);
            this.photoView = photoView;
        }
    }
}
