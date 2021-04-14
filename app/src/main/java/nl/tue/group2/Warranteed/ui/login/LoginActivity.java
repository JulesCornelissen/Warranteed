package nl.tue.group2.Warranteed.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import nl.tue.group2.Warranteed.MainActivity;
import nl.tue.group2.Warranteed.MainStoreActivity;
import nl.tue.group2.Warranteed.R;
import nl.tue.group2.Warranteed.Register;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    EditText usernameEditText, passwordEditText;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        //set up the login page
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //sets up variables
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory()).get(LoginViewModel.class);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final Button registerButton = findViewById(R.id.register);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        //if register button is clicked, the page changes to register
        registerButton.setEnabled(true);
        registerButton.setOnClickListener(v -> {
            Intent intentRegister = new Intent(LoginActivity.this, Register.class);
            startActivity(intentRegister);
        });

        //if login is clicked the data that is given will be checked
        loginButton.setEnabled(true);
        loginButton.setOnClickListener(v -> {
            String Email = usernameEditText.getText().toString().trim();
            String password1 = passwordEditText.getText().toString().trim();

            if (!isDetailsValid(Email, password1)) {
                return;
            }
            //if data is correct user can log in
            FirebaseAuth.getInstance().signInWithEmailAndPassword(Email, password1).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        Intent intentCustomer = new Intent(LoginActivity.this, MainActivity.class);
                        Intent intentStore = new Intent(LoginActivity.this, MainStoreActivity.class);

                        // it gets checked if the user was a customer or store owner
                        db.collection("Customers").document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    boolean usertype = document.getBoolean("is store " + "owner");
                                    System.out.println(usertype);
                                    //user gets send to the right screen.
                                    if (usertype) {
                                        Toast.makeText(LoginActivity.this, getString(R.string.msgStoreLogin), Toast.LENGTH_SHORT).show();
                                        startActivity(intentStore);
                                    } else {
                                        Toast.makeText(LoginActivity.this, getString(R.string.msgCustomerLogin), Toast.LENGTH_SHORT).show();
                                        startActivity(intentCustomer);
                                    }
                                }
                            }
                        });
                    } else {
                        usernameEditText.setError(getString(R.string.msgWrongCredentials));
                    }
                }
            });
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is already signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            Intent intentCustomer = new Intent(LoginActivity.this, MainActivity.class);
            Intent intentStore = new Intent(LoginActivity.this, MainStoreActivity.class);
            // it gets checked if the user was a customer or store owner
            db.collection("Customers").document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        boolean usertype = document.getBoolean("is store owner");
                        System.out.println(usertype);
                        //user gets send to the right screen.
                        if (usertype) {
                            Toast.makeText(LoginActivity.this, getString(R.string.msgStoreLogin), Toast.LENGTH_SHORT).show();
                            startActivity(intentStore);
                        } else {
                            Toast.makeText(LoginActivity.this, getString(R.string.msgCustomerLogin), Toast.LENGTH_SHORT).show();
                            startActivity(intentCustomer);
                        }
                    }
                }
            });
        }
    }

    public boolean isDetailsValid(String Email, String password) {
        boolean main = true;
        if (TextUtils.isEmpty(Email)) {
            usernameEditText.setError(getString(R.string.msgNoEmail));
            main = false;
        }
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError(getString(R.string.msgNoPassword));
            main = false;
        }
        return main;
    }
}

