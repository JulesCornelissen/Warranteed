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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import nl.tue.group2.Warranteed.R;

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
}