package nl.tue.group2.Warranteed.ui.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import nl.tue.group2.Warranteed.R;
import nl.tue.group2.Warranteed.chat.ChatAdapter;
import nl.tue.group2.Warranteed.chat.ChatMessage;
import nl.tue.group2.Warranteed.chat.ChatMessageView;

public class ChatFragment extends Fragment {

    RecyclerView recyclerViewChat;
    ChatAdapter chatAdapter;
    private FirebaseDatabase mDatabase; // The messages database
    private FirebaseRecyclerAdapter<ChatMessage, ChatMessageView> mFirebaseAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        mDatabase = FirebaseDatabase.getInstance("https://warrenteed-60226-default-rtdb.europe-west1.firebasedatabase.app/");
//        mDatabase.setPersistenceEnabled(true);

        this.recyclerViewChat = (RecyclerView) view.findViewById(R.id.recyclerViewChat);

        String messagesPath = "messages/1234/";

        DatabaseReference messagesRef = this.mDatabase.getReference().child(messagesPath);
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


        FirebaseRecyclerOptions<ChatMessage> options = new FirebaseRecyclerOptions.Builder<ChatMessage>()
                .setQuery(messagesRef, ChatMessage.class)
                .build();
        this.chatAdapter = new ChatAdapter(options);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setStackFromEnd(true);
        recyclerViewChat.setLayoutManager(mLinearLayoutManager);
        recyclerViewChat.setAdapter(chatAdapter);

//        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://warrenteed-60226-default-rtdb.europe-west1.firebasedatabase.app/");
//        DatabaseReference messagesRef = mDatabase.getReference().child("messages");
//        messagesRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
//                    Log.v("firebase_failed", "Error getting data", task.getException());
//                }
//                else {
//                    Log.v("firebase_successful", String.valueOf(task.getResult().getValue()));
//                }
//            }
//        });

        return view;
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