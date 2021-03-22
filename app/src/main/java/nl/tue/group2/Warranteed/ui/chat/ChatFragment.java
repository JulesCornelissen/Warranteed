package nl.tue.group2.Warranteed.ui.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import nl.tue.group2.Warranteed.R;
import nl.tue.group2.Warranteed.chat.ChatAdapter;
import nl.tue.group2.Warranteed.chat.ChatMessage;
import nl.tue.group2.Warranteed.firebase.FireBase;

public class ChatFragment extends Fragment {

    private RecyclerView recyclerViewChat;
    private ChatAdapter chatAdapter;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance(FireBase.FIREBASE_DATABASE_URL); // The messages database
    private String messagesPath;
    private String UUID = "fj2893jf103j1";

    // TODO get UUID from currently logged in Firebase user
    // TODO create screen for store with list of all UUIDs in database that have sent a message
    // TODO add second recycler view to differentiate between messages from user and store
    // TODO add comments
    // TODO implement image of user
    // TODO clean up UI
    // TODO scroll to bottom on new message
    // TODO chat notification - Likely not possible due to needing server side component
    // TODO chat background updating - Likely not possible due to needing server side component

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

//        mDatabase = FirebaseDatabase.getInstance(FireBase.FIREBASE_DATABASE_URL);
        this.recyclerViewChat = (RecyclerView) view.findViewById(R.id.recyclerViewChat);

        this.messagesPath = "messages/" + this.UUID;
        DatabaseReference messagesRef = this.mDatabase.getReference().child(messagesPath);

        /**
         * DEBUG
         */
//        mDatabase.setPersistenceEnabled(true);
        //        messagesRef.keepSynced(true);

//        ChatMessage debugMessage = new ChatMessage();
//        debugMessage.setText("Hello world123");
//        messagesRef.setValue(debugMessage);

        messagesRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.v("firebase_failed", "Error getting data", task.getException());
                }
                else {
                    Log.v("firebase_successful", String.valueOf(task.getResult().getValue()));
                }
            }
        });

        /**
         * END OF DEBUG
         */


        FirebaseRecyclerOptions<ChatMessage> options = new FirebaseRecyclerOptions.Builder<ChatMessage>()
                .setQuery(messagesRef, ChatMessage.class)
                .build();
        this.chatAdapter = new ChatAdapter(options);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());

        // Let messages appear in reverse chronological order
        mLinearLayoutManager.setStackFromEnd(true);
        recyclerViewChat.setLayoutManager(mLinearLayoutManager);
        recyclerViewChat.setAdapter(chatAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.getActivity().findViewById(R.id.buttonSendChatMessage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = ((EditText) getActivity().findViewById(R.id.textInputChatMessage));
                String text = editText.getText().toString();
                ChatMessage message = new ChatMessage(text, UUID);
                mDatabase.getReference().child(messagesPath).push().setValue(message);
                editText.getText().clear();
            }
        });
    }

    //Method to tell the adapter to start monitoring for changes in database
    @Override
    public void onStart() {
        super.onStart();
        chatAdapter.startListening();
    }

    //Method to tell the adapter to stop monitoring for changes in database
    @Override
    public void onStop() {
        super.onStop();
        chatAdapter.stopListening();
    }

}