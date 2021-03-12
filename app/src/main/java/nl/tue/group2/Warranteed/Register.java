package nl.tue.group2.Warranteed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class Register extends AppCompatActivity {
    CheckBox cb_cust, cb_store;
    EditText cb_Email, cb_Password, cb_Password2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        cb_cust = findViewById(R.id.checkBox_cust);
        cb_store = findViewById(R.id.checkBox_store);
        cb_Email = findViewById(R.id.username);
        cb_Password = findViewById(R.id.reg_password1);
        cb_Password2 = findViewById(R.id.reg_password2);

        final Button signupButton = findViewById(R.id.signup);


        signupButton.setEnabled(true);
        signupButton.setOnClickListener(v -> {
            Intent intentCustomer = new Intent(Register.this, MainActivity.class);
            Intent intentStore = new Intent(Register.this, HomeStore.class);
            String Email = cb_Email.getText().toString().trim();
            String password1 = cb_Password.getText().toString().trim();
            String password2 = cb_Password2.getText().toString().trim();


            if (!isDetailsValid(Email,password1,password2)) {
                return;
            }

            if (cb_cust.isChecked()) {
                startActivity(intentCustomer);
            } else {
                startActivity(intentStore);
            }
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
        else if (!Email.contains("@")) {
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