package nl.tue.group2.Warranteed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import nl.tue.group2.Warranteed.ui.login.LoginActivity;

public class Register extends AppCompatActivity {
    CheckBox cb_cust, cb_store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        cb_cust = findViewById(R.id.checkBox_cust);
        cb_store = findViewById(R.id.checkBox_store);
        final Button signupButton = findViewById(R.id.signup);

        signupButton.setEnabled(true);
        signupButton.setOnClickListener(v -> {
            Intent intent = new Intent(Register.this, MainActivity.class);
            startActivity(intent);
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
}