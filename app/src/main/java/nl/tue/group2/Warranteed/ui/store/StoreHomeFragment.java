package nl.tue.group2.Warranteed.ui.store;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import nl.tue.group2.Warranteed.R;

public class StoreHomeFragment extends Fragment {

    /**
     * The document id in firebase for the coolgreen store, since we only have one store right now
     */
    public static final String COOLGREEN_STORE_ID = "G5Xas72YqtCHMQZoxxxS";

    // keep track of store info for editing
    private String name, description, email, phoneNumber, addressStreet, addressHouseNumber, addressZipCode, addressCity, addressProvince;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_store, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the initial store info
        this.updateStoreInfo();

        // Set the edit button handler
        this.getActivity().findViewById(R.id.button_edit).setOnClickListener(button -> this.openEditDialog());
    }

    /**
     * Retrieves the store info from firebase and updates the text fields accordingly
     */
    @SuppressWarnings("unchecked")
    private void updateStoreInfo() {
        FirebaseFirestore.getInstance().collection("Store").document(COOLGREEN_STORE_ID).get().addOnSuccessListener(
                result -> {
                    // store name
                    this.name = result.getString("name");
                    ((TextView) this.getActivity().findViewById(R.id.textViewStoreName)).setText(this.name);
                    // description
                    this.description = result.getString("description");
                    ((TextView) this.getActivity().findViewById(R.id.textViewDescription)).setText(this.description);
                    // email
                    this.email = result.getString("email");
                    ((TextView) this.getActivity().findViewById(R.id.textViewEmail)).setText(this.getResources().getString(R.string.shopEmail, this.email));
                    // phone number
                    this.phoneNumber = result.getString("phonenumber");
                    ((TextView) this.getActivity().findViewById(R.id.textViewPhoneNumber)).setText(this.getResources().getString(R.string.shopPhoneNumber, this.phoneNumber));
                    // address
                    Map<String, Object> addressProperties = (Map<String, Object>) result.get("address");
                    this.addressStreet = (String) addressProperties.get("streetName");
                    this.addressHouseNumber = (String) addressProperties.get("houseNumber");
                    this.addressZipCode = (String) addressProperties.get("zipCode");
                    this.addressCity = (String) addressProperties.get("city");
                    this.addressProvince = (String) addressProperties.get("province");
                    String address = this.addressStreet + " " + this.addressHouseNumber + ", " + this.addressZipCode + " " + this.addressCity + ", " + this.addressProvince;
                    ((TextView) this.getActivity().findViewById(R.id.textViewAddress)).setText(this.getResources().getString(R.string.shopAddress, address));
                }
        );
    }

    /**
     * Opens a dialog to edit the store information
     */
    private void openEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
//        builder.setTitle(this.getResources().getString(R.string.editShopTitle));

        // create a list view
        LinearLayout view = new LinearLayout(builder.getContext());
        view.setOrientation(LinearLayout.VERTICAL);

        // shortcut
        Function<Integer, TextView> createText = i -> {
            TextView textView = new TextView(view.getContext());
            textView.setText(this.getResources().getString(i));
            return textView;
        };

        // name
        view.addView(createText.apply(R.string.editShopName));
        final EditText nameField = new EditText(view.getContext());
        nameField.setText(this.name);
        view.addView(nameField);

        // description
        view.addView(createText.apply(R.string.editShopDescription));
        final EditText descriptionField = new EditText(view.getContext());
        descriptionField.setText(this.description);
        view.addView(descriptionField);

        // email
        view.addView(createText.apply(R.string.editShopEmail));
        final EditText emailField = new EditText(view.getContext());
        emailField.setText(this.email);
        emailField.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        view.addView(emailField);

        // phone number
        view.addView(createText.apply(R.string.editShopPhoneNumber));
        final EditText phoneNumberField = new EditText(view.getContext());
        phoneNumberField.setText(this.phoneNumber);
        phoneNumberField.setInputType(InputType.TYPE_CLASS_PHONE);
        view.addView(phoneNumberField);

        // address street
        view.addView(createText.apply(R.string.editShopStreet));
        final EditText addressStreetField = new EditText(view.getContext());
        addressStreetField.setText(this.addressStreet);
        view.addView(addressStreetField);

        // address house number
        view.addView(createText.apply(R.string.editShopHouseNumber));
        final EditText addressHouseNumberField = new EditText(view.getContext());
        addressHouseNumberField.setText(this.addressHouseNumber);
        addressHouseNumberField.setInputType(InputType.TYPE_CLASS_NUMBER);
        view.addView(addressHouseNumberField);

        // address zip code
        view.addView(createText.apply(R.string.editShopZipCode));
        final EditText addressZipCodeField = new EditText(view.getContext());
        addressZipCodeField.setText(this.addressZipCode);
        addressZipCodeField.setInputType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
        view.addView(addressZipCodeField);

        // city
        view.addView(createText.apply(R.string.editShopCity));
        final EditText addressCityField = new EditText(view.getContext());
        addressCityField.setText(this.addressCity);
        view.addView(addressCityField);

        // province
        view.addView(createText.apply(R.string.editShopProvince));
        final EditText addressProvinceField = new EditText(view.getContext());
        addressProvinceField.setText(this.addressProvince);
        view.addView(addressProvinceField);

        // save button
        builder.setPositiveButton(this.getResources().getString(R.string.editShopSave), (dialog, i) -> {
            boolean valid = true;
            // validate all inputs
            if (nameField.getText().toString().trim().isEmpty()) {
                nameField.setError("Must not be empty!");
                valid = false;
            }
            if (descriptionField.getText().toString().trim().isEmpty()) {
                descriptionField.setError("Must not be empty!");
                valid = false;
            }
            if (emailField.getText().toString().trim().isEmpty()) {
                emailField.setError("Must not be empty!");
                valid = false;
            }
            if (phoneNumberField.getText().toString().trim().isEmpty()) {
                phoneNumberField.setError("Must not be empty!");
                valid = false;
            }
            if (addressStreetField.getText().toString().trim().isEmpty()) {
                addressStreetField.setError("Must not be empty!");
                valid = false;
            }
            if (addressHouseNumberField.getText().toString().trim().isEmpty()) {
                addressHouseNumberField.setError("Must not be empty!");
                valid = false;
            }
            if (addressZipCodeField.getText().toString().trim().isEmpty()) {
                addressZipCodeField.setError("Must not be empty!");
                valid = false;
            }
            if (addressCityField.getText().toString().trim().isEmpty()) {
                addressCityField.setError("Must not be empty!");
                valid = false;
            }
            if (addressProvinceField.getText().toString().trim().isEmpty()) {
                addressProvinceField.setError("Must not be empty!");
                valid = false;
            }

            if (valid) {
                this.uploadStoreInfo(
                        nameField.getText().toString().trim(),
                        descriptionField.getText().toString().trim(),
                        emailField.getText().toString().trim(),
                        phoneNumberField.getText().toString().trim(),
                        addressStreetField.getText().toString().trim(),
                        addressHouseNumberField.getText().toString().trim(),
                        addressZipCodeField.getText().toString().trim(),
                        addressCityField.getText().toString().trim(),
                        addressProvinceField.getText().toString().trim());
                dialog.dismiss();
            }
        });

        // cancel button
        builder.setNegativeButton(this.getResources().getString(R.string.editShopCancel), (dialog, i) -> dialog.dismiss());

        // show the dialog
        builder.setView(view);
        builder.show();
    }

    /**
     * Uploads new store information to firebase
     */
    private void uploadStoreInfo(String name, String description, String email, String phoneNumber, String addressStreet, String addressHouseNumber, String addressZipCode, String addressCity, String addressProvince) {
        // put all data into a map
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("description", description);
        data.put("email", email);
        data.put("phonenumber", phoneNumber);
        Map<String, String> address = new HashMap<>();
        address.put("streetName", addressStreet);
        address.put("houseNumber", addressHouseNumber);
        address.put("zipCode", addressZipCode);
        address.put("city", addressCity);
        address.put("province", addressProvince);
        data.put("address", address);

        // push the data to firebase
        FirebaseFirestore.getInstance().collection("Store").document(COOLGREEN_STORE_ID).update(data).addOnSuccessListener(
                result -> this.updateStoreInfo() // refresh the store information
        );
    }
}



