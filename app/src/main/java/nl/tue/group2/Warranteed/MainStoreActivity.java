package nl.tue.group2.Warranteed;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import nl.tue.group2.Warranteed.ui.login.LoginActivity;
import nl.tue.group2.Warranteed.ui.store.StoreChatFragment;
import nl.tue.group2.Warranteed.ui.store.StoreHomeFragment;

/**
 * Created 3/19/2021 by SuperMartijn642
 */
public class MainStoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_store);

        // hide the action bar
        getSupportActionBar().hide();

        // switch fragments when an icon is pressed
        BottomNavigationView navView = findViewById(R.id.store_bottom_nav);
        navView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            if (item.getItemId() == R.id.navigation_home)
                selectedFragment = new StoreHomeFragment();
            else if (item.getItemId() == R.id.navigation_chat)
                selectedFragment = new StoreChatFragment();

            //Display the selected fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.store_fragment_container,
                    selectedFragment).commit();

            return true;
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.store_fragment_container,
                    new StoreHomeFragment()).commit();
        }

        final Button logoutbutton = findViewById(R.id.logoutButton);
        logoutbutton.setEnabled(true);
        logoutbutton.setOnClickListener(v -> {
            Intent intentLogout = new Intent(this, LoginActivity.class);
            FirebaseAuth.getInstance().signOut();
            startActivity(intentLogout);
        });
    }
}
