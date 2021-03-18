package nl.tue.group2.Warranteed.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

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

import java.util.Objects;

import nl.tue.group2.Warranteed.R;
import nl.tue.group2.Warranteed.Receipt;

public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    //Initialize adapter and recycleView
    RecyclerView recyclerView;
    ReceiptAdapter adapter;
    //initialize searchbar and spinner
    EditText searchbar;
    Spinner spinner;
    //global variables for keeping track of which receipts to display
    String state = "All";
    String search = "";

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

        //create and monitor spinner
        setSpinner(view);
        //Create and monitor searchbar
        setSearchbar(view);
        //Create and monitor touch helper (used for deleting receipts)
        createTouchHelper();
        //start monitoring buttons in each cardview
        openReceiptInfo();

        return view;
    }

    /*
     * initializes spinner
     * and sets creates and sets adapter on spinner
     */
    private void setSpinner(View view){
        spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> spinner_adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.spinner_options, android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_adapter);
        spinner.setOnItemSelectedListener(this);
    }

    //Monitors changes in the spinner menu
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //update state to state chosen from the dropdown menu
        state = parent.getItemAtPosition(position).toString();
        //update the recyclerView
        updateRecyclerView(search, state);
    }
    //not implemented, must be included
    @Override
    public void onNothingSelected(AdapterView<?> parent) {}


    /*
     * initializes searchbar and
     * adds text listener to searchbar
     */
    public void setSearchbar(View view){
        searchbar = view.findViewById(R.id.search_bar);
        //add listener
        searchbar.addTextChangedListener(new TextWatcher() {
            //not implemented
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            //not implemented
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                search = s.toString().toLowerCase();
                //check if s is empty
                updateRecyclerView(search, state);
            }
        });
    }

    /*
     * Method to filter query with search word s
     * and notify adapter of change in dataset
     */
    private void updateRecyclerView(String search, String state){
        //when state = All the query should ignore the state.
        if (Objects.equals(state, "All")){
            Query newQuery = receipts.orderBy("product").startAt(search).endAt(search + '\uf8ff');
            //create new options
            FirestoreRecyclerOptions<Receipt> newOptions = new FirestoreRecyclerOptions.Builder<Receipt>()
                    .setQuery(newQuery, Receipt.class)
                    .build();
            //update adapter with the new options
            adapter.updateOptions(newOptions);
        } else {
            //new query that filters for products starting with search word s
            Query newQuery = receipts.orderBy("product")
                    .startAt(search)
                    .endAt(search + '\uf8ff')
                    .whereEqualTo("state", state); //state equals state given
            //create new options
            FirestoreRecyclerOptions<Receipt> newOptions = new FirestoreRecyclerOptions.Builder<Receipt>()
                    .setQuery(newQuery, Receipt.class)
                    .build();
            //update adapter with the new options
            adapter.updateOptions(newOptions);
        }
    }

    /*
     * Method for monitoring the buttons in each card
     * Passes data from specific card to ReceiptInfo
     * and starts this activity
     */
    private void openReceiptInfo(){
        adapter.setOnItemClickListener(new ReceiptAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                //get receipt object from snapshot
                Receipt receipt = documentSnapshot.toObject(Receipt.class);
                //get variables using get methods from Receipt
                assert receipt != null;
                String product = receipt.getProduct();
                String expiration_date = receipt.getExpiration_date();
                String purchase_date = receipt.getPurchase_date();
                String duration = receipt.getDuration();

                //create intent
                Intent intent = new Intent(getActivity(), ReceiptInfo.class);
                //give intent extra information
                intent.putExtra("product", product);
                intent.putExtra("expiration_date", expiration_date);
                intent.putExtra("purchase_date", purchase_date);
                intent.putExtra("duration", duration);
                //start new activity (ReceiptInfo)
                startActivity(intent);
            }
        });
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