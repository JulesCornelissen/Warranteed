package nl.tue.group2.Warranteed.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
        //specific query to get detailed receipts
        //for now this is all receipts
        Query query = receipts;
        FirestoreRecyclerOptions<Receipt> options = new FirestoreRecyclerOptions.Builder<Receipt>()
                .setQuery(query, Receipt.class)
                .build();
        //assign adapter to the recyclerView
        adapter = new ReceiptAdapter(options);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //method to detect swiping
        //used for the deleting of receipts
        createTouchHelper();

        adapter.setOnItemClickListener(new ReceiptAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                //get receipt object from snapshot
                Receipt receipt = documentSnapshot.toObject(Receipt.class);
                //get variables using get methods from Receipt
                String product = receipt.getProduct();
                String edate = receipt.getDate();
                String pdate = receipt.getPdate();
                String duration = receipt.getDuration();

                //create intent
                Intent intent = new Intent(getActivity(), ReceiptInfo.class);
                //give intent extra information
                intent.putExtra("product", product);
                intent.putExtra("edate", edate);
                intent.putExtra("pdate", pdate);
                intent.putExtra("duration", duration);
                //start new activity (ReceiptInfo)
                startActivity(intent);
            }
        });
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

    //Method to create a TouchHelper
    //Detects swipes to the left in order to delete a specific receipt
    private void createTouchHelper(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            //detect move, not used
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull
                    RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            //detect swipes
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //call delete() from ReceiptAdapter to delete card at position
                adapter.delete(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
    }
}