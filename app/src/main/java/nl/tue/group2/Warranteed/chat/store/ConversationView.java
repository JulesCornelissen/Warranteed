package nl.tue.group2.Warranteed.chat.store;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import nl.tue.group2.Warranteed.R;
import nl.tue.group2.Warranteed.ui.chat.ChatFragment;

/**
 * Created 3/22/2021 by SuperMartijn642
 */
public class ConversationView extends RecyclerView.ViewHolder {

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("kk:mm", Locale.US);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

    private final TextView nameField;
    private final TextView timeField;
    private final TextView previewField;
    private String customerId;

    public ConversationView(@NonNull View itemView, FragmentManager fragmentManager) {
        super(itemView);
        this.nameField = itemView.findViewById(R.id.textViewCustomerName);
        this.timeField = itemView.findViewById(R.id.textViewTime);
        this.previewField = itemView.findViewById(R.id.textViewMessagePreview);
        this.itemView.setOnClickListener(view -> fragmentManager.beginTransaction()
                                                                .replace(R.id.store_fragment_container, new ChatFragment(this.customerId))
                                                                .commit());
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
        FirebaseFirestore.getInstance().collection("Customers").document(customerId).get().addOnSuccessListener(result -> this.setDisplayName(result.getString(
                "email"), customerId));
    }

    public void setMessageTime(long time) {
        Date date = new Date(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR) && calendar.get(Calendar.YEAR) == Calendar.getInstance().get(
                Calendar.YEAR)) {
            this.timeField.setText(TIME_FORMAT.format(new Date(time)));
        } else if (calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance()
                                                                 .get(Calendar.DAY_OF_YEAR) - 1 && calendar.get(Calendar.YEAR) == Calendar.getInstance().get(
                Calendar.YEAR)) {
            this.timeField.setText("Yesterday");
        } else {
            this.timeField.setText(DATE_FORMAT.format(date));
        }
    }

    private void setDisplayName(String name, String customerId) {
        if (name == null || name.isEmpty()) {
            this.nameField.setText("Email not specified, " + customerId);
            this.nameField.setTypeface(this.nameField.getTypeface(), Typeface.ITALIC);
        } else {
            this.nameField.setText(name);
        }
    }

    public void setLastMessage(String message) {
        if (message == null) {
            this.previewField.setText("No messages");
        } else {
            this.previewField.setText(message);
        }
    }
}
