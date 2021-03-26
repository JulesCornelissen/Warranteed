package nl.tue.group2.Warranteed.ui.store;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import nl.tue.group2.Warranteed.R;
import nl.tue.group2.Warranteed.firebase.FirebaseImageHandler;

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
        this.updateStoreImages();
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
                    ((TextView) this.getActivity().findViewById(R.id.textView7)).setText(email);
                    // phone number
                    String phoneNumber = result.getString("phonenumber");
                    ((TextView) this.getActivity().findViewById(R.id.textView8)).setText("+" + phoneNumber);
                    // address
                    Map<String, Object> addressProperties = (Map<String, Object>) result.get("address");
                    String addressStreet = (String) addressProperties.get("streetName");
                    String addressHouseNumber = (String) addressProperties.get("houseNumber");
                    String addressZipCode = (String) addressProperties.get("zipCode");
                    String addressCity = (String) addressProperties.get("city");
                    String addressProvince = (String) addressProperties.get("province");
                    String address = addressStreet + " " + addressHouseNumber + ", " + addressZipCode + " " + addressCity + ", " + addressProvince;
                    ((TextView) this.getActivity().findViewById(R.id.textView9)).setText(address);
                }
        );
    }

    private void updateStoreImages() {
        View logoView = this.getActivity().findViewById(R.id.imageViewLogo),
                picture1View = this.getActivity().findViewById(R.id.imageView2),
                picture2View = this.getActivity().findViewById(R.id.imageView3),
                picture3View = this.getActivity().findViewById(R.id.imageView4);
        FirebaseFirestore.getInstance().collection("Store").document(StoreHomeFragment.COOLGREEN_STORE_ID).get().addOnSuccessListener(
                result -> {
                    // logo
                    String logoId = result.getString("logo");
                    if (logoId != null && !logoId.isEmpty()) {
                        FirebaseImageHandler.downloadImage("store", UUID.fromString(logoId)).addOnSuccessListener(
                                ((ImageView) logoView)::setImageBitmap
                        );
                    }
                    // picture 1
                    String picture1Id = result.getString("picture1");
                    if (picture1Id != null && !picture1Id.isEmpty()) {
                        FirebaseImageHandler.downloadImage("store", UUID.fromString(picture1Id)).addOnSuccessListener(
                                ((ImageView) picture1View)::setImageBitmap
                        );
                    }
                    // picture 2
                    String picture2Id = result.getString("picture2");
                    if (picture2Id != null && !picture2Id.isEmpty()) {
                        FirebaseImageHandler.downloadImage("store", UUID.fromString(picture2Id)).addOnSuccessListener(
                                ((ImageView) picture2View)::setImageBitmap
                        );
                    }
                    // picture 3
                    String picture3Id = result.getString("picture3");
                    if (picture3Id != null && !picture3Id.isEmpty()) {
                        FirebaseImageHandler.downloadImage("store", UUID.fromString(picture3Id)).addOnSuccessListener(
                                ((ImageView) picture3View)::setImageBitmap
                        );
                    }
                }
        );
    }
}

