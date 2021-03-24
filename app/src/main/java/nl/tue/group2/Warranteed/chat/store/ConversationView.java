package nl.tue.group2.Warranteed.chat.store;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import nl.tue.group2.Warranteed.R;
import nl.tue.group2.Warranteed.ui.chat.ChatFragment;

/**
 * Created 3/22/2021 by SuperMartijn642
 */
public class ConversationView extends RecyclerView.ViewHolder {

    private final TextView nameField;
    private String customerId;

    public ConversationView(@NonNull View itemView, FragmentManager fragmentManager) {
        super(itemView);
        this.nameField = itemView.findViewById(R.id.textViewCustomerName);
        this.itemView.setOnClickListener(view -> fragmentManager.beginTransaction().replace(R.id.store_fragment_container, new ChatFragment(this.customerId)).commit());
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
        FirebaseFirestore.getInstance().collection("Customers").document(customerId).get().addOnSuccessListener(result -> this.setDisplayName(result.getString("email"), customerId));
    }

    private void setDisplayName(String name, String customerId) {
        if (name == null || name.isEmpty()) {
            this.nameField.setText("Email not specified, " + customerId);
            this.nameField.setTypeface(this.nameField.getTypeface(), Typeface.ITALIC);
        } else
            this.nameField.setText(name);
    }
}
