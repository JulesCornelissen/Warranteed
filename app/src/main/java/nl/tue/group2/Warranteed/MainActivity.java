package nl.tue.group2.Warranteed;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import nl.tue.group2.Warranteed.notifications.Alarm;
import nl.tue.group2.Warranteed.notifications.NotificationHandler;
import nl.tue.group2.Warranteed.notifications.NotificationManager;
import nl.tue.group2.Warranteed.ui.add.AddFragment;
import nl.tue.group2.Warranteed.ui.chat.ChatFragment;
import nl.tue.group2.Warranteed.ui.home.HomeFragment;
import nl.tue.group2.Warranteed.ui.login.LoginActivity;
import nl.tue.group2.Warranteed.ui.store.StoreFragment;

public class MainActivity extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    //get current user
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    //get email of curent user
    String email = currentUser.getEmail().trim();
    //variable to store the notification days in
    int days;
    Long date_quickest = Long.MAX_VALUE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intentLogout = new Intent(this, LoginActivity.class);
        BottomNavigationView navView = findViewById(R.id.bottom_nav);
        navView.setOnNavigationItemSelectedListener(navListener);
        //rotate device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }

        // Initialize notification channel for general notifications
        NotificationHandler notificationHandler = new NotificationHandler(getString(R.string.chan_general_app),
                                                                          getString(R.string.channel_name),
                                                                          getString(R.string.channel_description),
                                                                          this);
        NotificationManager.setNotificationHandler(notificationHandler);

        //get notification days from server
        db.collection("Customers").document(currentUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String notif = documentSnapshot.getString("notif");
                days = notif == null ? 1 : Integer.parseInt(notif);
            }
        });

        //on start: delete alarm and set new alarm with correct days and receipt
        deleteAlarm();
        calculateAlarm();

        final ImageButton popup_button = findViewById(R.id.logoutButton);
        // Setting onClick behavior to the button
        popup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initialize alert dialog
                AlertDialog.Builder dialogBuilder;
                AlertDialog dialog;
                //create buttons
                Button bt_logout, bt_delete;
                dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                final View popup_view = getLayoutInflater().inflate(R.layout.popup_settings, null);
                //match buttons
                bt_logout = popup_view.findViewById(R.id.Button_popup_logout);
                bt_delete = popup_view.findViewById(R.id.Button_popup_delete);
                //show the popup_settings layout at the top of the screen
                dialogBuilder.setView(popup_view);
                dialog = dialogBuilder.create();
                dialog.show();
                dialog.getWindow().setGravity(Gravity.TOP);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                //make spinner
                Spinner spinner;
                spinner = popup_view.findViewById(R.id.spinner2);
                //set spinner adapter
                ArrayAdapter<CharSequence> spinner_adapter = ArrayAdapter.createFromResource(MainActivity.this,
                                                                                             R.array.notif_options,
                                                                                             android.R.layout.simple_spinner_item);
                spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinner_adapter);
                //set default value to notif stored in firestore
                if (days == 14) {
                    spinner.setSelection(1);
                } else if (days == 21) {
                    spinner.setSelection(2);
                } else if (days == 28) {
                    spinner.setSelection(3);
                }

                //when an item is selected:
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //get selected item
                        String new_notif = parent.getItemAtPosition(position).toString();
                        //update firestore
                        db.collection("Customers").document(currentUser.getUid()).update("notif", new_notif);
                        //locally update variable
                        days = Integer.parseInt(new_notif);

                        //update alarms
                        deleteAlarm();
                        calculateAlarm();
                    }

                    //not implemented
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                //when delete button is clicked
                bt_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //get the current user
                        FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
                        //sign the user out
                        FirebaseAuth.getInstance().signOut();
                        //delete the account of the user
                        currentuser.delete();
                        Toast.makeText(MainActivity.this, "your account has been deleted", Toast.LENGTH_SHORT).show();
                        //go back to the login page
                        startActivity(intentLogout);
                    }
                });

                //when logout button is clicked
                bt_logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(MainActivity.this, "you've been logged out", Toast.LENGTH_SHORT).show();
                        //go back to the login page
                        startActivity(intentLogout);
                    }
                });
            }
        });
        //hide the top bar from view
        getSupportActionBar().hide();
    }

    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;  //make empty fragment
        switch (item.getItemId()) {     //get the current fragment id
            case R.id.navigation_home:
                selectedFragment = new HomeFragment();
                deleteAlarm();
                calculateAlarm();
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
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        return true;
    };

    /*
     * calculates the date on which the first receipt is expiring and
     * sets an alarm to go of x days before
     */
    public void calculateAlarm() {
        db.collection("Receipt").whereEqualTo("email", email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot document : list) {
                    //get expiration date from each receipt
                    Long date_expiration = (Long) document.get("expiration_date_timestamp");
                    //if the expiration date is void
                    if (date_expiration < System.currentTimeMillis()) {
                        date_expiration = Long.MAX_VALUE;
                    }
                    //if the expected notification date has already passed
                    //Offset used to counter loading times between upload and alarm creation
                    else if (date_expiration - days * 86400000 < System.currentTimeMillis() - 100000000) {
                        date_expiration = Long.MAX_VALUE;
                    }
                    //the date is valid, check if there already is a shorter date
                    //if there is not, the date_expiration becomes the shortest date
                    else if (date_expiration < date_quickest) {
                        date_quickest = date_expiration;
                    }
                }
                //set alarm with stored date and 60000ms added (1 minute)
                setAlarm(date_quickest + 60000 - days * 86400000);
            }
        });
    }

    //set alarm with date given
    public void setAlarm(long date) {
        //make new alarmManager
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //create intents
        Intent intent = new Intent(this, Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        //set alarm for the specific date
        alarmManager.set(AlarmManager.RTC, date, pendingIntent);
    }

    //delete all alarms currently set
    public void deleteAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, Alarm.class);
        //pending intent is must be the same as for setAlarm
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.cancel(pendingIntent);
    }

    public static void updateReceiptStates() {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        FirebaseFirestore.getInstance().collection("Receipt").whereEqualTo("email", email).get().addOnSuccessListener(result -> {
            for (DocumentSnapshot snapshot : result.getDocuments()) {
                Long expirationDate = snapshot.getLong("expiration_date_timestamp");
                if (expirationDate == null) {
                    continue;
                }
                long current = System.currentTimeMillis();
                String state = expirationDate < current ? "Void" : expirationDate < current + 30L * 24 * 60 * 60 * 1000 ? "Expiring" : "Valid";
                FirebaseFirestore.getInstance().collection("Receipt").document(snapshot.getId()).update(Collections.singletonMap("state", state));
            }
        });
    }
}

