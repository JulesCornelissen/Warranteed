package nl.tue.group2.Warranteed;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {
    //initialize the variables
    CheckBox cb_cust, cb_store;
    EditText cb_Email, cb_Password, cb_Password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //set up the register page
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //set up the variables in the register page
        cb_cust = findViewById(R.id.checkBox_cust);
        cb_store = findViewById(R.id.checkBox_store);
        cb_Email = findViewById(R.id.username);
        cb_Password = findViewById(R.id.reg_password1);
        cb_Password2 = findViewById(R.id.reg_password2);

        //when signup is pressed data is retrieved from the server
        final Button signupButton = findViewById(R.id.signup);
        signupButton.setEnabled(true);
        signupButton.setOnClickListener(v -> {
            Intent intentCustomer = new Intent(Register.this, MainActivity.class);
            Intent intentStore = new Intent(Register.this, MainStoreActivity.class);
            String Email = cb_Email.getText().toString().trim();
            String password1 = cb_Password.getText().toString().trim();
            String password2 = cb_Password2.getText().toString().trim();
            //checks if email and password are valid
            if (!isDetailsValid(Email,password1,password2)) {
                return;
            }
            // a new user is made with the email and the password
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(Email,password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        // checks the status of the user.
                        boolean status=true;
                        if (cb_cust.isChecked()) {
                            status=false;
                        }

                        // push user data to the database
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        Map<String, Object> data = new HashMap<>();
                        data.put("uid", user.getUid());
                        data.put("display_name", user.getDisplayName());
                        data.put("email", user.getEmail());
                        data.put("is store owner", status);
                        data.put("notif", "14");
                        FirebaseFirestore.getInstance().collection("Customers").document(user.getUid()).set(data);

                        // sends the user to the customer or store screen
                        if (cb_cust.isChecked()) {
                            startActivity(intentCustomer);
                        } else {
                            startActivity(intentStore);
                        }
                    }
                    else{
                        cb_Email.setError("email is not unique");
                    }
                }
            });
        });
        //checks if the user checks the customer or store button
        cb_cust.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                cb_store.setChecked(false);
            }
        });

        cb_store.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                cb_cust.setChecked(false);
            }
        });

    }
    //checks if the details comply to our requirements
    public boolean isDetailsValid(String Email,String password,String password2) {
        boolean main = true;

        if (TextUtils.isEmpty(Email)) {
            cb_Email.setError("email is required");
            main = false;
        }
        else if (!Email.contains("@") || !(Email.endsWith(".nl") || (Email.endsWith(".com")))) {
            cb_Email.setError("email is incorrect");
            main = false;
        }
        else if (Email.indexOf("@")==0  || (Email.indexOf("@")+1==Email.indexOf("."))) {
            cb_Email.setError("email is incorrect");
            main = false;
        }

        if (TextUtils.isEmpty(password)) {
            cb_Password.setError("password is required");
            main = false;
        }
        else if(TextUtils.getTrimmedLength(password) <6){
            cb_Password.setError("password requires 6 characters");
            main = false;
        }
        else if(TextUtils.getTrimmedLength(password) >15){
            cb_Password.setError("password requires less than 15 characters");
            main = false;
        }

        if (TextUtils.isEmpty(password2)) {
            cb_Password2.setError("password is required");
            main = false;
        }
        else if (!TextUtils.equals(password,password2)) {
            cb_Password2.setError("passwords don't match");
            main = false;
        }
        return main;
    }
}
