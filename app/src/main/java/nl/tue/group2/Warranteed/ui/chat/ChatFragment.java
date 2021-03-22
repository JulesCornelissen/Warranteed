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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private FirebaseDatabase mDatabase; // The messages database
    private String messagesPath;
    private String UUIDreceiver;
    private String UUIDsender; // Only to be used when the current user is the store

    // Done get UUID from currently logged in Firebase user
    // TODO create screen for store with list of all UUIDs in database that have sent a message
    // Done add second recycler view to differentiate between messages from user and store
    // TODO message view who sent message lags one behind (i.e. if store sends a message only from the second message will it show as the store)
    // TODO add comments
    // TODO implement image of user - partially done, need reference to where user image is stored
    // TODO clean up UI
    // TODO scroll to bottom on new message
    // TODO chat notification - Likely not possible due to needing server side component
    // TODO chat background updating - Likely not possible due to needing server side component
    // Done Create slightly different ChatFragment for store, with two UUIDs. One for the receiver and one for the sender.
    //      Something like if store then sender UUID = "CoolGreen".

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Split adapter in to receiver and sender
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        /**
         * Initialize the instance variables
         */
        // Connect to the database
        this.mDatabase = FirebaseDatabase.getInstance(FireBase.FIREBASE_DATABASE_URL);
        this.recyclerViewChat = (RecyclerView) view.findViewById(R.id.recyclerViewChat);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser!= null) {
            String email = currentUser.getEmail().trim();
            if (email.endsWith("coolgreen.nl")){
                // TODO add code that gets the UUID of the customer that the store wants to talk to
                // update UUIDreceiver to the UUID of the customer
                this.UUIDsender = "CoolGreen";
            } else {
                this.UUIDreceiver = currentUser.getUid();
                this.UUIDsender = currentUser.getUid();
            }
        }
        this.messagesPath = "messages/" + this.UUIDreceiver;

        /**
         * Get the database reference and build the query
         */
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
        this.chatAdapter.setSenderUUID(this.UUIDsender);

        /**
         * Create the UI
         */
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
                ChatMessage message = new ChatMessage(text, UUIDreceiver);
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