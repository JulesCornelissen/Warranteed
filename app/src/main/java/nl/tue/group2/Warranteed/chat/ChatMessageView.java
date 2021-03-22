package nl.tue.group2.Warranteed.chat;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import nl.tue.group2.Warranteed.R;

public class ChatMessageView extends RecyclerView.ViewHolder {

    public final LinearLayout itemView;

    TextView messageTextView;
    TextView messageTimeTextView;
    ImageView messengerImageView; // Store logo or user photo

    public ChatMessageView(@NonNull LinearLayout itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    public void setView(View view){
        this.itemView.addView(view);
        this.messageTextView = (TextView) view.findViewById(R.id.messageText);
        this.messageTimeTextView = (TextView) view.findViewById(R.id.messageTime);
        this.messengerImageView = (ImageView) view.findViewById(R.id.messengerImage);
    }

}
