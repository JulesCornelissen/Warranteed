package nl.tue.group2.Warranteed.ui.add;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;

import androidx.fragment.app.Fragment;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created 3/2/2021 by SuperMartijn642
 */
public class PhotoHandler {

    // Unique code for image capture and gallery
    private static final int REQUEST_IMAGE_CAPTURE = 444;
    private static final int REQUEST_SELECT_IMAGE = 445;

    /**
     * Allows the user to select the photo from their saved images.
     *
     * @param activity the currently active activity
     * @return true if the switch to a gallery app is successful
     */
    public static boolean requestSelectPhoto(Fragment activity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        try {
            activity.startActivityForResult(intent, REQUEST_SELECT_IMAGE);
            return true;
        } catch (ActivityNotFoundException ignore) {
            // either the device does not have a gallery app, or our app does not have permission
        }
        return false;
    }

    /**
     * Checks whether the given request code is a result of calling {@link #requestSelectPhoto(Fragment)}
     *
     * @param requestCode the request code
     * @return true if the request code corresponds to the select photo code
     */
    public static boolean isSelectPhotoRequest(int requestCode) {
        return requestCode == REQUEST_SELECT_IMAGE;
    }

    /**
     * Gets the selected image from the intent data given by {@link Fragment#onActivityResult(int, int, Intent)}
     *
     * @param activity    the currently active activity
     * @param requestCode the request code
     * @param resultCode  the result code
     * @param data        the intent data
     * @return the photo from the given {@code data} or {@code null} if no image was selected
     */
    public static Bitmap getSelectedPhotoFromActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (isSelectPhotoRequest(requestCode) && resultCode == Activity.RESULT_OK) {
            try {
                InputStream stream = activity.getContentResolver().openInputStream(data.getData());
                return BitmapFactory.decodeStream(stream);
            } catch (FileNotFoundException ignore) {
                // the user didn't select a file or selected a file not accessible by our app
            }
        }
        return null;
    }

    /**
     * Switches the current view to the device's camera app to let the user take a photo
     *
     * @param activity the currently active activity
     * @return true if the switch to the camera app is successful
     */
    public static boolean requestImageCapture(Fragment activity) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            return true;
        } catch (ActivityNotFoundException ignore) {
            // either the device does not have a camera app, or our app does not have permission
        }
        return false;
    }

    /**
     * Checks whether the given request code is a result of calling {@link #requestImageCapture(Fragment)}
     *
     * @param requestCode the request code
     * @return true if the request code corresponds to the image capture code
     */
    public static boolean isImageCaptureRequest(int requestCode) {
        return requestCode == REQUEST_IMAGE_CAPTURE;
    }

    /**
     * Gets the captured image from the intent data given by {@link Fragment#onActivityResult(int, int, Intent)}
     *
     * @param requestCode the request code
     * @param resultCode  the result code
     * @param data        the intent data
     * @return the image from the given {@code data} or {@code null} if no image was captured
     */
    public static Bitmap getCapturedImageFromActivityResult(int requestCode, int resultCode, Intent data) {
        if (isImageCaptureRequest(requestCode) && resultCode == Activity.RESULT_OK) {
            return (Bitmap) data.getExtras().get("data");
        }
        return null;
    }

}
