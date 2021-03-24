package nl.tue.group2.Warranteed.chat.store;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import nl.tue.group2.Warranteed.R;
import nl.tue.group2.Warranteed.firebase.FireBase;

/**
 * Created 3/22/2021 by SuperMartijn642
 */
public class ConversationAdapter extends FirebaseRecyclerAdapter<ConversationAdapter.Conversation, ConversationView> {

    private final FragmentManager fragmentManager;

    public ConversationAdapter(FragmentManager fragmentManager) {
        super(new FirebaseRecyclerOptions.Builder<Conversation>().setQuery(FirebaseDatabase.getInstance(FireBase.FIREBASE_DATABASE_URL).getReference().child("conversations"), Conversation.class).build());
        this.fragmentManager = fragmentManager;
    }

    @Override
    protected void onBindViewHolder(@NonNull ConversationView holder, int position, @NonNull Conversation model) {
        holder.setCustomerId(model.customerId);
    }

    @NonNull
    @Override
    public ConversationView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_store_conversation_view, parent, false);
        return new ConversationView(view, this.fragmentManager);
    }

    public static class Conversation {

        private String customerId;

        public void setCustomerid(String customerId) {
            this.customerId = customerId;
        }

        public String getCustomerid() {
            return customerId;
        }
    }
}
