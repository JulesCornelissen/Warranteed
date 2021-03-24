package nl.tue.group2.Warranteed;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import nl.tue.group2.Warranteed.firebase.FireBase;
import nl.tue.group2.Warranteed.notifications.NotificationHandler;
import nl.tue.group2.Warranteed.notifications.NotificationManager;
import nl.tue.group2.Warranteed.ui.add.AddFragment;
import nl.tue.group2.Warranteed.ui.chat.ChatFragment;
import nl.tue.group2.Warranteed.ui.home.HomeFragment;
import nl.tue.group2.Warranteed.ui.login.LoginActivity;
import nl.tue.group2.Warranteed.ui.store.StoreFragment;

public class MainActivity extends AppCompatActivity {
    //initialize the variables
    private FirebaseAuth mAuth;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intentLogout = new Intent(this, LoginActivity.class);
        FireBase instance = new FireBase(); // Creating Firebase instance
        instance.readData(); // Own class that calls data from database as test. to be changed.
        BottomNavigationView navView = findViewById(R.id.bottom_nav);
        navView.setOnNavigationItemSelectedListener(navListener);
        //rotate device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }


        final ImageButton popup_button = findViewById(R.id.logoutButton);
        // Setting onClick behavior to the button
        popup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            // Initializing the popup menu and giving the reference as current context
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, popup_button);
                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.settings_button, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                            case R.id.logout:
                                FirebaseAuth.getInstance().signOut();
                                Toast.makeText(MainActivity.this, "you've been logged out", Toast.LENGTH_SHORT).show();
                                startActivity(intentLogout);
                                return true;
                            case R.id.delete_account:
                                FirebaseUser currentuser = firebaseAuth.getInstance().getCurrentUser();
                                FirebaseAuth.getInstance().signOut();
                                currentuser.delete();
                                Toast.makeText(MainActivity.this, "your account has been deleted", Toast.LENGTH_SHORT).show();
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


        // Initialize notification channel for general notifications
        NotificationHandler notificationHandler = new NotificationHandler(
                getString(R.string.chan_general_app),
                getString(R.string.channel_name),
                getString(R.string.channel_description),
                this);
        NotificationManager.setNotificationHandler(notificationHandler);




        getSupportActionBar().hide();
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

    /**
     * A small sample notification sender/tester. Linked to a button on the mainview.
     *
     * @param view The view that gets passed from the button, required for the button.
     */
    public void sendNotification(View view) {
        Random r = new Random();
        int notifID = r.nextInt();
        NotificationManager.getNotificationHandler().sendNotification("Warranteed", Integer.toString(notifID));
    }
}

