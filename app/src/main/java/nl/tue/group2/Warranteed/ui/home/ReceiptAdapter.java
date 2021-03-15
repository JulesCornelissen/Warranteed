package nl.tue.group2.Warranteed.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import nl.tue.group2.Warranteed.R;
import nl.tue.group2.Warranteed.Receipt;

public class ReceiptAdapter extends FirestoreRecyclerAdapter<Receipt, ReceiptAdapter.Viewholder> {

    public ReceiptAdapter(@NonNull FirestoreRecyclerOptions<Receipt> options) {
        super(options);
    }

    // Function to bind each view to appropriate card
    @Override
    protected void onBindViewHolder(@NonNull Viewholder holder, int position, @NonNull Receipt model) {
        //Add product from Receipt class to appropriate view in card
        holder.view_product.setText(model.getProduct());
        //Add date from Receipt class to appropriate view in card
        holder.view_date.setText(model.getDate());
        //Add state from Receipt class to appropriate view in card
        holder.view_state.setText(model.getState());
    }

    // Function to tell the class about the Card view (here
    // "person.xml")in
    // which the data will be shown
    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row,
                parent, false);
        return new Viewholder(view);
    }

    // Sub Class to create references of the views in Crad
    class Viewholder extends RecyclerView.ViewHolder {
        //create textviews
        TextView view_product, view_date, view_state;

        public Viewholder(View itemView) {
            super(itemView);
            //link textview to approptiate id
            view_product = itemView.findViewById(R.id.textView_product);
            view_state= itemView.findViewById(R.id.textView_expiring_filled);
            view_date = itemView.findViewById(R.id.textView_warranty_filled);
        }
    }
}