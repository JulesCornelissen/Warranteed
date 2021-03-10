package nl.tue.group2.Warranteed.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import nl.tue.group2.Warranteed.R;

public class HomeFragment extends Fragment {

    ListView list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        String[] items = {"number1", "number2", "number3", "number5", "number5", "number6..",
                "number1", "number2", "number3", "number5", "number5", "number6...",
                "number1", "number2", "number3", "number5", "number5", "number6....",
                "number1", "number2", "number3", "number5", "number5", "number6....."};
        list = (ListView) view.findViewById(R.id.list_warranties);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                items
        );

        list.setAdapter(adapter);

        return view;
    }
}