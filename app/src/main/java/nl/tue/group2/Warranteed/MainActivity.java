package nl.tue.group2.Warranteed;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import nl.tue.group2.Warranteed.ui.add.AddFragment;
import nl.tue.group2.Warranteed.ui.chat.ChatFragment;
import nl.tue.group2.Warranteed.ui.home.HomeFragment;
import nl.tue.group2.Warranteed.ui.store.StoreFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.bottom_nav);
        navView.setOnNavigationItemSelectedListener(navListener);
        //rotate device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }
    }

    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;  //make empty fragment
                switch (item.getItemId()) {     //get the current fragment id
                    case R.id.navigation_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.navigation_add:
                        selectedFragment = new AddFragment();
                        break;
                    case R.id.navigation_store:
                        selectedFragment = new StoreFragment();
                        break;
                    case R.id.navigation_chat:
                        selectedFragment = new ChatFragment();
                        break;
                }
                //Display the selected fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();
                return true;
            };
}