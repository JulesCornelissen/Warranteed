package nl.tue.group2.Warranteed.ui.store;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import nl.tue.group2.Warranteed.R;
import nl.tue.group2.Warranteed.chat.store.ConversationAdapter;

/**
 * Created 3/19/2021 by SuperMartijn642
 */
public class StoreConversationFragment extends Fragment {

    private ConversationAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store_conversations, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.adapter = new ConversationAdapter(this.getActivity().getSupportFragmentManager());
        ((RecyclerView) this.getActivity().findViewById(R.id.recyclerViewConversations)).setAdapter(this.adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        this.adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        this.adapter.stopListening();
    }
}
