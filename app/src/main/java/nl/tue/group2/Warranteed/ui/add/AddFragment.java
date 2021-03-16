package nl.tue.group2.Warranteed.ui.add;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import nl.tue.group2.Warranteed.R;
import nl.tue.group2.Warranteed.ui.home.HomeFragment;

public class AddFragment extends Fragment {

    private Bitmap receiptImage;
    private Calendar purchaseDate;
    private Calendar expirationDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.receiptImage = null;

        // set the listeners for the 'take a picture' and 'upload from gallery' buttons
        this.getActivity().findViewById(R.id.takeImageButton).setOnClickListener(
                v -> PhotoHandler.requestImageCapture(this)
        );
        this.getActivity().findViewById(R.id.selectImageButton).setOnClickListener(
                v -> PhotoHandler.requestSelectPhoto(this)
        );

        // purchase and expiration date fields
        this.purchaseDate = Calendar.getInstance();
        this.getActivity().findViewById(R.id.purchaseDateField).setOnClickListener(
                v -> {
                    DatePickerDialog.OnDateSetListener listener = (v2, year, month, day) -> {
                        this.purchaseDate.set(Calendar.YEAR, year);
                        this.purchaseDate.set(Calendar.MONTH, month);
                        this.purchaseDate.set(Calendar.DAY_OF_MONTH, day);
                        ((EditText) v).setText(new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(this.purchaseDate.getTime()));
                    };
                    DatePickerDialog dialog = new DatePickerDialog(this.getContext(), listener, this.purchaseDate.get(Calendar.YEAR), this.purchaseDate.get(Calendar.MONTH), this.purchaseDate.get(Calendar.DAY_OF_MONTH));
                    dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    dialog.show();
                }
        );
        this.expirationDate = Calendar.getInstance();
        this.getActivity().findViewById(R.id.expirationDateField).setOnClickListener(
                v -> {
                    DatePickerDialog.OnDateSetListener listener = (v2, year, month, day) -> {
                        this.expirationDate.set(Calendar.YEAR, year);
                        this.expirationDate.set(Calendar.MONTH, month);
                        this.expirationDate.set(Calendar.DAY_OF_MONTH, day);
                        ((EditText) v).setText(new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(this.expirationDate.getTime()));
                    };
                    DatePickerDialog dialog = new DatePickerDialog(this.getContext(), listener, this.expirationDate.get(Calendar.YEAR), this.expirationDate.get(Calendar.MONTH), this.expirationDate.get(Calendar.DAY_OF_MONTH));
                    dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    dialog.show();
                }
        );

        // clear button
        this.getActivity().findViewById(R.id.clearButton).setOnClickListener(
                v -> ((EditText) this.getActivity().findViewById(R.id.itemField)).setText("")
        );

        // submit button
        this.getActivity().findViewById(R.id.submitReceiptButton).setOnClickListener(
                v -> {
                    if (this.validateFields())
                        this.completeReceipt();
                }
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Bitmap image = null;

        // capture taken photo
        if (PhotoHandler.isImageCaptureRequest(requestCode))
            image = PhotoHandler.getCapturedImageFromActivityResult(requestCode, resultCode, data);
            // capture selected image
        else if (PhotoHandler.isSelectPhotoRequest(requestCode))
            image = PhotoHandler.getSelectedPhotoFromActivityResult(this.getActivity(), requestCode, resultCode, data);

        if (image != null) {
            this.setReceiptImage(image);
            View view = this.getActivity().findViewById(R.id.imageView);
            if (view instanceof ImageView)
                ((ImageView) view).setImageBitmap(image);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setReceiptImage(Bitmap image) {
        this.receiptImage = image;
    }

    /**
     * Checks that all fields have valid data, otherwise it displays an error
     * @return true if all fields are valid
     */
    private boolean validateFields() {
        boolean valid = true;

        // name
        EditText nameField = this.getActivity().findViewById(R.id.nameField);
        if (nameField.getText().toString().trim().isEmpty()) {
            nameField.setError("Field must not be empty!");
            valid = false;
        }
        // purchase date
        EditText purchaseDataField = this.getActivity().findViewById(R.id.purchaseDateField);
        if (purchaseDataField.getText().toString().trim().isEmpty()) {
            purchaseDataField.setError("Field must be set!");
            valid = false;
        }
        // expiration date
        EditText expirationDateField = this.getActivity().findViewById(R.id.expirationDateField);
        if (expirationDateField.getText().toString().trim().isEmpty()) {
            expirationDateField.setError("Field must be set!");
            valid = false;
        } else if (this.expirationDate.getTime().getTime() < this.purchaseDate.getTime().getTime()) {
            expirationDateField.setError("Must be later than the purchase date!");
            valid = false;
        }
        // item
        EditText itemField = this.getActivity().findViewById(R.id.itemField);
        if (itemField.getText().toString().trim().isEmpty()) {
            itemField.setError("Field must not be empty!");
            valid = false;
        }
        // image
        if (this.receiptImage == null)
            valid = false;

        return valid;
    }

    /**
     * Uploads the receipt to the server and switches the activity to the receipt's page
     */
    private void completeReceipt() {
        String name = ((EditText) this.getActivity().findViewById(R.id.nameField)).getText().toString();
        Date purchaseDate = this.purchaseDate.getTime();
        Date expirationDate = this.expirationDate.getTime();
        String item = ((EditText) this.getActivity().findViewById(R.id.itemField)).getText().toString();
        Bitmap image = this.receiptImage;

        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yy", Locale.US);

        // put the data into a map
        Map<String, String> data = new HashMap<>();
        data.put("name", name);
        data.put("purchase_date", dateFormatter.format(purchaseDate));
        data.put("expiration_date", dateFormatter.format(expirationDate));
        data.put("product", item);

        // TODO send image data

        // create a new receipt in firebase
        FirebaseFirestore.getInstance().collection("Receipt").document().set(data).addOnCompleteListener(
                task -> this.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit()
        );
    }
}