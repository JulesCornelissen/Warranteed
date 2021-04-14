package nl.tue.group2.Warranteed.chat.store;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import nl.tue.group2.Warranteed.R;
import nl.tue.group2.Warranteed.firebase.FireBase;

/**
 * Created 3/22/2021 by SuperMartijn642
 */
public class ConversationAdapter extends FirebaseRecyclerAdapter<ConversationAdapter.Conversation, ConversationView> {

    private final FragmentManager fragmentManager;

    public ConversationAdapter(FragmentManager fragmentManager) {
        super(createOptions(null));
        this.fragmentManager = fragmentManager;
    }

    @Override
    protected void onBindViewHolder(@NonNull ConversationView holder, int position, @NonNull Conversation model) {
        holder.setCustomerId(model.customerId);
        holder.setMessageTime(model.timestamp);
        holder.setLastMessage(model.getLastmessage());
    }

    @NonNull
    @Override
    public ConversationView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_store_conversation_view, parent, false);
        return new ConversationView(view, this.fragmentManager);
    }

    public void setSearchText(String searchText) {
        this.updateOptions(createOptions(searchText));
    }

    public static FirebaseRecyclerOptions<Conversation> createOptions(String searchText) {
        Query query = FirebaseDatabase.getInstance(FireBase.FIREBASE_DATABASE_URL).getReference().child("conversations");
        if (searchText != null && !searchText.trim().isEmpty()) {
            query = query.orderByChild("email").startAt(searchText.trim()).endAt(searchText.trim() + '\uf8ff');
        }
        query = query.orderByChild("negative_timestamp");
        return new FirebaseRecyclerOptions.Builder<Conversation>().setQuery(query, Conversation.class).build();
    }

    public static class Conversation {

        private String customerId, email, lastmessage;
        private long timestamp, negativeTimestamp;

        public void setCustomerid(String customerId) {
            this.customerId = customerId;
        }

        public String getCustomerid() {
            return this.customerId;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getEmail() {
            return this.email;
        }

        public void setLastmessage(String lastmessage) {
            this.lastmessage = lastmessage;
        }

        public String getLastmessage() {
            return this.lastmessage;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public long getTimestamp() {
            return this.timestamp;
        }

        public void setNegative_timestamp(long negativeTimestamp) {
            this.negativeTimestamp = negativeTimestamp;
        }

        public long getNegative_timestamp() {
            return this.negativeTimestamp;
        }
    }
}
