package nl.tue.group2.Warranteed.chat;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import nl.tue.group2.Warranteed.R;

public class ChatMessageView extends RecyclerView.ViewHolder {

    public final ConstraintLayout itemView;

    TextView messageTextView;
    TextView messageTimeTextView;
    ImageView messengerImageView; // Store logo or user photo

    public ChatMessageView(@NonNull ConstraintLayout itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    public void setView(View view, ConstraintLayout.LayoutParams params) {
        this.itemView.addView(view, params);
        this.messageTextView = (TextView) view.findViewById(R.id.messageText);
        this.messageTimeTextView = (TextView) view.findViewById(R.id.messageTime);
        this.messengerImageView = (ImageView) view.findViewById(R.id.messengerImage);
    }

}
