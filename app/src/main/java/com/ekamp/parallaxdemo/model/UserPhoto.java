package com.ekamp.parallaxdemo.model;

/**
 * Represents photos to be displayed below the effects banner within CardViews.
 * These photos should have a reference to where they are and a title to be displayed
 * underneath them to be used as a short description of the effect that the onClick
 * action of the card will enact.
 *
 * @author Erik Kamp
 * @since v1.0
 */
public class UserPhoto {

    private String photo, photoTitle;

    /**
     * Creates a UserPhoto instance with the path to the photo on the current device and title for the photo.
     *
     * @param photo      the String photo path.
     * @param photoTitle title for the photo.
     */
    public UserPhoto(String photo, String photoTitle) {
        this.photo = photo;
        this.photoTitle = photoTitle;
    }

    /**
     * Creates a UserPhoto instance with the Bitmap image of the photo on the current device.
     *
     * @param photo the String path of the photo on the current device
     */
    public UserPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhoto() {
        return photo;
    }

    public String getPhotoTitle() {
        return photoTitle;
    }

    /**
     * Sets the location or path for this photo, pertaining to the current device.
     *
     * @param photo String path to image on the current device.
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * Sets the title for the current photo.
     *
     * @param photoTitle title for the current photo.
     */
    public void setPhotoTitle(String photoTitle) {
        this.photoTitle = photoTitle;
    }

    @Override
    public String toString() {
        return "UserPhoto{" +
                "photo=" + photo +
                ", photoTitle='" + photoTitle + '\'' +
                '}';
    }
}
