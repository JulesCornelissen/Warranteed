package nl.tue.group2.Warranteed.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import nl.tue.group2.Warranteed.MainActivity;
import nl.tue.group2.Warranteed.MainStoreActivity;
import nl.tue.group2.Warranteed.R;
import nl.tue.group2.Warranteed.Register;
import nl.tue.group2.Warranteed.ui.store.StoreHomeFragment;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private FirebaseAuth mAuth;
    private FirebaseAnalytics mFirebaseAnalytics;
    EditText usernameEditText,passwordEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);
        mAuth = FirebaseAuth.getInstance();
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final Button registerButton = findViewById(R.id.register);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        registerButton.setEnabled(true);
        registerButton.setOnClickListener(v -> {
            Intent intentRegister = new Intent(LoginActivity.this, Register.class);
            startActivity(intentRegister);
        });


        loginButton.setEnabled(true);
        loginButton.setOnClickListener(v -> {
            Intent intentCustomer = new Intent(LoginActivity.this, MainActivity.class);
            Intent intentStore = new Intent(LoginActivity.this, StoreHomeFragment.class);
            String Email = usernameEditText.getText().toString().trim();
            String password1 = passwordEditText.getText().toString().trim();

            if (!isDetailsValid(Email,password1)) {
                return;
            }

            mAuth.signInWithEmailAndPassword(Email, password1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            String email = user.getEmail().trim();
                            if (email.endsWith("coolgreen.nl")){
                                startActivity(intentStore);
                            }
                            else{
                                startActivity(intentCustomer);
                            }
                        } else {
                            usernameEditText.setError("email or/and password is incorrect");
                        }
                    }
                });
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getInstance().getCurrentUser();
        if (currentUser!= null){
            String email = currentUser.getEmail().trim();
            Intent intentCustomer = new Intent(LoginActivity.this, MainActivity.class);
            Intent intentStore = new Intent(LoginActivity.this, MainStoreActivity.class);
            if (email.endsWith("coolgreen.nl")){
                startActivity(intentStore);
            }
            else{
                startActivity(intentCustomer);
            }
        }
    }
    public boolean isDetailsValid(String Email,String password) {
        boolean main=true;
        if (TextUtils.isEmpty(Email)) {
            usernameEditText.setError("email is required");
            main = false;
        }
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("password is required");
            main = false;
        }
        return main;
    }
}

