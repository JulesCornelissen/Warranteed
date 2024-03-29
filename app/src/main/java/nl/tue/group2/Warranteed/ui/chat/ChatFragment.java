package nl.tue.group2.Warranteed.ui.chat;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import nl.tue.group2.Warranteed.R;
import nl.tue.group2.Warranteed.chat.ChatAdapter;
import nl.tue.group2.Warranteed.chat.ChatMessage;
import nl.tue.group2.Warranteed.firebase.FireBase;

public class ChatFragment extends Fragment {

    private RecyclerView recyclerViewChat;
    private ChatAdapter chatAdapter;
    private FirebaseDatabase mDatabase; // The messages database
    private String messagesPath;
    private String UUIDReceiver;
    private String UUIDSender; // Only to be used when the current user is the store

    // Done get UUID from currently logged in Firebase user
    // Done create screen for store with list of all UUIDs in database that have sent a message
    // Done add second recycler view to differentiate between messages from user and store
    // Done message view who sent message lags one behind (i.e. if store sends a message only
    //      from the second message will it show as the store)
    // Done fix message view not aligned to right side
    // Done add comments
    // Scrapped implement image of user - partially done, need reference to where user image is stored
    // Done clean up UI
    // TODO scroll to bottom on new message
    // TODO chat notification - Likely not possible due to needing server side component
    // TODO chat background updating - Likely not possible due to needing server side component
    // Done Create slightly different ChatFragment for store, with two UUIDs. One for the
    //      receiver and one for the sender. Something like if store then sender UUID = "CoolGreen".
    // Done add code that gets the UUID of the customer that the store wants to talk to

    public ChatFragment() {

    }

    /**
     * Constructor for when the logged in user is the store, sets the UUID of the customer that the
     * store is chatting with.
     *
     * @param customerUUID The UUID of the customer.
     */
    public ChatFragment(String customerUUID) {
        this.UUIDReceiver = customerUUID;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Split adapter in to receiver and sender
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        //        this.getActivity().getWindow().setBackgroundDrawableResource(R.drawable.ic_chat_top);
        /**
         * Initialize the instance variables
         */
        // Connect to the database
        this.mDatabase = FirebaseDatabase.getInstance(FireBase.FIREBASE_DATABASE_URL);
        this.recyclerViewChat = (RecyclerView) view.findViewById(R.id.recyclerViewChat);

        if (this.UUIDReceiver != null) {
            // If UUIDReceiver is not null then the ChatFragment is being used by the store
            // The UUID of the receiver is given in the constructor when logged in as the store
            this.UUIDSender = "CoolGreen";
        } else {
            // A customer is using the ChatFragment
            this.UUIDReceiver = FirebaseAuth.getInstance().getUid();
            this.UUIDSender = this.UUIDReceiver;
        }

        this.messagesPath = "messages/" + this.UUIDReceiver;

        /**
         * Get the database reference and build the query
         */
        DatabaseReference messagesRef = this.mDatabase.getReference().child(messagesPath);
        FirebaseRecyclerOptions<ChatMessage> options = new FirebaseRecyclerOptions.Builder<ChatMessage>().setQuery(messagesRef, ChatMessage.class).build();
        this.chatAdapter = new ChatAdapter(options);
        this.chatAdapter.setSenderUUID(this.UUIDSender);

        // Get the width of the display and pass it to the view adapter
        // Unfortunately this has to be done here as there is no reliable way to get the activity
        // from a view only. For some reason the margin as configured converted to pixels is
        // slightly smaller than the actual margin, thus times 4 divided by 3 gives a very close
        // approximation.
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int activityMargin = Math.round(4 * getResources().getDimension(R.dimen.activity_horizontal_margin) / 3);
        this.chatAdapter.setWidth(screenWidth - activityMargin);

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
                ChatMessage message = new ChatMessage(text, UUIDReceiver);
                mDatabase.getReference().child(messagesPath).push().setValue(message);
                FirebaseFirestore.getInstance().collection("Customers").document(UUIDReceiver).get().addOnSuccessListener(result -> {
                    mDatabase.getReference().child("conversations").child(UUIDReceiver).updateChildren(Stream.of(
                            new Pair<>("customerid", UUIDReceiver),
                            new Pair<>("email", result.getString("email")),
                            new Pair<>("timestamp", System.currentTimeMillis()),
                            new Pair<>("negative_timestamp", -System.currentTimeMillis()),
                            new Pair<>("lastmessage", message.getText())).collect(Collectors.toMap(pair -> pair.first, pair -> pair.second)));
                });
                editText.getText().clear();
            }
        });
    }

    /**
     * Upon creating the ChatFragment tell the chatAdapter to start listening.
     */
    @Override
    public void onStart() {
        super.onStart();
        chatAdapter.startListening();
    }

    /**
     * Upon quitting the ChatFragment tell the chatAdapter to stop listening.
     */
    @Override
    public void onStop() {
        super.onStop();
        chatAdapter.stopListening();
    }

}