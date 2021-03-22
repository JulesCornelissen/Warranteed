package nl.tue.group2.Warranteed.chat;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import nl.tue.group2.Warranteed.R;

public class ChatMessageView extends RecyclerView.ViewHolder {


    TextView messageTextView;
    TextView messageTimeTextView;
    ImageView messengerImageView; // Store logo or user photo


    public ChatMessageView(@NonNull View itemView) {
        super(itemView);
        this.messageTextView = (TextView) itemView.findViewById(R.id.messageTextReceiver);
        this.messageTimeTextView = (TextView) itemView.findViewById(R.id.messageTimeReceiver);
        this.messengerImageView = (ImageView) itemView.findViewById(R.id.messengerImageReceiver);
    }

}
