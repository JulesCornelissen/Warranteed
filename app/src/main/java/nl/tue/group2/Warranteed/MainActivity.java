package nl.tue.group2.Warranteed;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import nl.tue.group2.Warranteed.firebase.FireBase;
import nl.tue.group2.Warranteed.notifications.NotificationHandler;
import nl.tue.group2.Warranteed.ui.add.AddFragment;
import nl.tue.group2.Warranteed.ui.chat.ChatFragment;
import nl.tue.group2.Warranteed.ui.home.HomeFragment;
import nl.tue.group2.Warranteed.ui.login.LoginActivity;
import nl.tue.group2.Warranteed.ui.store.StoreFragment;

public class MainActivity extends AppCompatActivity {

    private NotificationHandler notificationHandler;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FireBase instance = new FireBase(); // Creating Firebase instance
        instance.readData(); // Own class that calls data from database as test. to be changed.
        BottomNavigationView navView = findViewById(R.id.bottom_nav);
        navView.setOnNavigationItemSelectedListener(navListener);
        //rotate device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }

        final Button logoutbutton = findViewById(R.id.logoutButton);
        logoutbutton.setEnabled(true);
        logoutbutton.setOnClickListener(v -> {
            Intent intentLogout = new Intent(MainActivity.this, LoginActivity.class);
            mAuth.getInstance().signOut();
            startActivity(intentLogout);
        });



        // Initialize notification channel for general notifications
        this.notificationHandler = new NotificationHandler(
                getString(R.string.chan_general_app),
                getString(R.string.channel_name),
                getString(R.string.channel_description),
                this);


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
     * @param view The view that gets passed from the button, required for the button.
     */
    public void sendNotification(View view) {
        Random r = new Random();
        int notifID = r.nextInt();
        this.notificationHandler.sendNotification("Warranteed", Integer.toString(notifID));



    }
}