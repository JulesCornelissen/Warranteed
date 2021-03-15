package nl.tue.group2.Warranteed.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import nl.tue.group2.Warranteed.R;
import nl.tue.group2.Warranteed.Receipt;

public class HomeFragment extends Fragment {

    //Initialize adapter and recycleView
    RecyclerView recyclerView;
    ReceiptAdapter adapter;
    //get receipts from firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference receipts = db.collection("Receipt");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //create view object
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //Create and set adapter to appropriate card id
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        //specific query to get detailed receitps
        //for now this is all receipts
        Query query = receipts;
        FirestoreRecyclerOptions<Receipt> options = new FirestoreRecyclerOptions.Builder<Receipt>()
                .setQuery(query, Receipt.class)
                .build();
        //assign adapter to the recyclerView
        adapter = new ReceiptAdapter(options);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    //Method to tell the adapter to start monitoring for changes in database
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    //Method to tell the adapter to stop monitoring for changes in database
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}