package nl.tue.group2.Warranteed.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.text.Format;
import java.text.SimpleDateFormat;

import nl.tue.group2.Warranteed.R;

public class ChatAdapter extends FirebaseRecyclerAdapter<ChatMessage, ChatMessageView> {

    public ChatAdapter(@NonNull FirebaseRecyclerOptions<ChatMessage> options) {
        super(options);
    }

    private final Format timeFormatter = new SimpleDateFormat("HH:mm");

    @Override
    protected void onBindViewHolder(@NonNull ChatMessageView holder, int position,
                                    @NonNull ChatMessage model) {
        holder.messageTextView.setText(model.getText());
        holder.messageTimeTextView.setText(timeFormatter.format(model.getTimestamp()));
    }

    @NonNull
    @Override
    public ChatMessageView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_chat_mrecycler_receiver, parent, false);
        return new ChatMessageView(view);
    }

}
