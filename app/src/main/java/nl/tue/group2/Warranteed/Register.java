package nl.tue.group2.Warranteed;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {
    CheckBox cb_cust, cb_store;
    EditText cb_Email, cb_Password, cb_Password2;
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        cb_cust = findViewById(R.id.checkBox_cust);
        cb_store = findViewById(R.id.checkBox_store);
        cb_Email = findViewById(R.id.username);
        cb_Password = findViewById(R.id.reg_password1);
        cb_Password2 = findViewById(R.id.reg_password2);
        mAuth = FirebaseAuth.getInstance();

        final Button signupButton = findViewById(R.id.signup);

        signupButton.setEnabled(true);
        signupButton.setOnClickListener(v -> {
            Intent intentCustomer = new Intent(Register.this, MainActivity.class);
            Intent intentStore = new Intent(Register.this, MainStoreActivity.class);
            String Email = cb_Email.getText().toString().trim();
            String password1 = cb_Password.getText().toString().trim();
            String password2 = cb_Password2.getText().toString().trim();

            if (!isDetailsValid(Email,password1,password2)) {
                return;
            }

            mAuth.createUserWithEmailAndPassword(Email,password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

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
                        FirebaseFirestore.getInstance().collection("Customers").document(user.getEmail()).set(data);

                        Map<String, Object> reg_entry = new HashMap<>();
                        reg_entry.put("email", Email);
                        reg_entry.put("is store owner", status);
                        db.collection("Customers").document(Email).set(reg_entry).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        });


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
