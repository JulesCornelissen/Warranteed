package nl.tue.group2.Warranteed.firebase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created 3/21/2021 by SuperMartijn642
 */
public class FirebaseImageHandler {

    /**
     * Creates a unique id for an image in the given folder.
     * Should not be called on the main thread.
     * @param folder the folder to check for existing id
     * @return the found unique id, or null if an error occurred
     */
    public static UUID createImageId(String folder) {
        StorageReference storage = FirebaseStorage.getInstance().getReference();
        UUID imageId;
        while (true) {
            // create a random id
            imageId = UUID.randomUUID();
            // check if the id is already used
            final AtomicBoolean success = new AtomicBoolean(false);
            Task<?> task = storage.child(folder + "/" + imageId).getDownloadUrl().addOnFailureListener(o -> success.set(true));
            try {
                Tasks.await(task);
            } catch (ExecutionException e) {
                success.set(true);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            if (success.get())
                break;
        }
        return imageId;
    }

    /**
     * Uploads an image to firebase in the given folder.
     * Should not be called on the main thread.
     * @param folder folder to put the image into
     * @param image  image to be uploaded
     * @return the id of the uploaded image
     */
    public static UUID uploadImage(String folder, Bitmap image) {
        UUID imageId = createImageId(folder);
        if (imageId != null)
            return uploadImage(folder, imageId, image);
        return null;
    }

    /**
     * Uploads an image to firebase in the given folder with the given id.
     * Should not be called on the main thread.
     * @param folder  folder to put the image into
     * @param imageId id to use for the image
     * @param image   image to be uploaded
     * @return the id of the uploaded image
     */
    public static UUID uploadImage(String folder, UUID imageId, Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 0, stream);
        try {
            Tasks.await(FirebaseStorage.getInstance().getReference().child(folder + "/" + imageId).putBytes(stream.toByteArray()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return imageId;
    }

    /**
     * Downloads the image with the given id in the given folder from firebase.
     * Should not be called on the main thread.
     * @param folder  folder the image is in
     * @param imageId the id of the image
     * @return the downloaded image, or null if no image was found
     */
    public static Task<Bitmap> downloadImage(String folder, UUID imageId) {
        return FirebaseStorage.getInstance().getReference().child(folder + "/" + imageId).getBytes(Long.MAX_VALUE).continueWith(result -> BitmapFactory.decodeStream(new ByteArrayInputStream(result.getResult())));
    }

}
