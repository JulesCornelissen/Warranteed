package nl.tue.group2.Warranteed;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import nl.tue.group2.Warranteed.ui.login.LoginActivity;
import nl.tue.group2.Warranteed.ui.store.StoreConversationFragment;
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
            if (item.getItemId() == R.id.navigation_home) {
                selectedFragment = new StoreHomeFragment();
            } else if (item.getItemId() == R.id.navigation_chat) {
                selectedFragment = new StoreConversationFragment();
            }

            //Display the selected fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.store_fragment_container, selectedFragment).commit();

            return true;
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.store_fragment_container, new StoreHomeFragment()).commit();
        }

        Intent intentLogout = new Intent(this, LoginActivity.class);
        final ImageButton popup_button = findViewById(R.id.logoutButton);
        // Setting onClick behavior to the button
        popup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initializing the popup menu and giving the reference as current context
                PopupMenu popupMenu = new PopupMenu(MainStoreActivity.this, popup_button);
                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.settings_button, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                            case R.id.logout:
                                FirebaseAuth.getInstance().signOut();
                                Toast.makeText(MainStoreActivity.this, "you've been logged out", Toast.LENGTH_SHORT).show();
                                startActivity(intentLogout);
                                return true;
                            case R.id.delete_account:
                                FirebaseAuth.getInstance().getCurrentUser().delete();
                                Toast.makeText(MainStoreActivity.this, "your account has been deleted", Toast.LENGTH_SHORT).show();
                                startActivity(intentLogout);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                // Showing the popup menu
                popupMenu.show();
            }
        });

    }
}
