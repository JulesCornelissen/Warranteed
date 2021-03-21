package nl.tue.group2.Warranteed.ui.store;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import nl.tue.group2.Warranteed.R;

public class StoreFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the store info from firebase
        this.updateStoreInfo();
    }

    /**
     * Retrieves the store info from firebase and updates the text fields accordingly
     */
    @SuppressWarnings("unchecked")
    private void updateStoreInfo() {
        FirebaseFirestore.getInstance().collection("Store").document(StoreHomeFragment.COOLGREEN_STORE_ID).get().addOnSuccessListener(
                result -> {
                    // store name
                    String name = result.getString("name");
                    ((TextView) this.getActivity().findViewById(R.id.textView_store)).setText(name);
                    // description
                    String description = result.getString("description");
                    ((TextView) this.getActivity().findViewById(R.id.textView4)).setText(description);
                    // email
                    String email = result.getString("email");
                    ((TextView) this.getActivity().findViewById(R.id.textView5)).setText(this.getResources().getString(R.string.shopEmail, email));
                    // phone number
                    String phoneNumber = result.getString("phonenumber");
                    ((TextView) this.getActivity().findViewById(R.id.textView6)).setText(this.getResources().getString(R.string.shopPhoneNumber, phoneNumber));
                    // address
                    Map<String, Object> addressProperties = (Map<String, Object>) result.get("address");
                    String addressStreet = (String) addressProperties.get("streetName");
                    String addressHouseNumber = (String) addressProperties.get("houseNumber");
                    String addressZipCode = (String) addressProperties.get("zipCode");
                    String addressCity = (String) addressProperties.get("city");
                    String addressProvince = (String) addressProperties.get("province");
                    String address = addressStreet + " " + addressHouseNumber + ", " + addressZipCode + " " + addressCity + ", " + addressProvince;
                    ((TextView) this.getActivity().findViewById(R.id.textView)).setText(this.getResources().getString(R.string.shopAddress, address));
                }
        );
    }
}

