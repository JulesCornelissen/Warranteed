package nl.tue.group2.Warranteed.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

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
    private String senderUUID;
    private ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(100, 0);
    ;

    @Override
    protected void onBindViewHolder(@NonNull ChatMessageView holder, int position, @NonNull ChatMessage model) {

        boolean sender = this.senderUUID.equals(model.getSender());

        View view;
        if (sender) {
            view = LayoutInflater.from(holder.itemView.getContext()).inflate(R.layout.fragment_chat_mrecycler_sender, holder.itemView, false);
        } else {
            view = LayoutInflater.from(holder.itemView.getContext()).inflate(R.layout.fragment_chat_mrecycler_receiver, holder.itemView, false);
        }
        holder.setView(view, this.params);

        holder.messageTextView.setText(model.getText());
        holder.messageTimeTextView.setText(timeFormatter.format(model.getTimestamp()));
    }

    @NonNull
    @Override
    public ChatMessageView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatMessageView(new ConstraintLayout(parent.getContext()));
    }
    // first oncreate, then onbind gets executed

    /**
     * Sets the sender UUID which determines where the message should be displayed.
     *
     * @param senderUUID The UUID of the sender.
     */
    public void setSenderUUID(String senderUUID) {
        this.senderUUID = senderUUID;
    }

    /**
     * Set the width of the chat messages in the view, should be set to width of the display.
     *
     * @param width The desired width.
     */
    public void setWidth(int width) {
        this.params.width = width;
    }

}
