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
        this.messageTextView = (TextView) itemView.findViewById(R.id.messageText);
        this.messageTimeTextView = (TextView) itemView.findViewById(R.id.messageTime);
        this.messengerImageView = (ImageView) itemView.findViewById(R.id.messengerImage);
    }

//    public void bindMessage(ChatMessage chatMessage) {
//        if (chatMessage.getText() != null) {
//            messageTextView.setText(chatMessage.getText());
//            messageTimeTextView.setText(chatMessage.getTimestamp());
//            messageTextView.setVisibility(TextView.VISIBLE);
////            messageImageView.setVisibility(ImageView.GONE);
//        }
//    }
}
