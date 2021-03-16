package nl.tue.group2.Warranteed.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import nl.tue.group2.Warranteed.MainActivity;
import nl.tue.group2.Warranteed.R;

public class ReceiptInfo extends AppCompatActivity {
    Button bt_return;
    TextView txt_product, txt_edate, txt_pdate, txt_duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_info);

        //buttons and textviews matching
        bt_return = findViewById(R.id.button_return);
        txt_product = findViewById(R.id.receiptInfo_product);
        txt_edate = findViewById(R.id.receiptInfo_edate_filled);
        txt_pdate = findViewById(R.id.receiptInfo_pdate_filled);
        txt_duration = findViewById(R.id.receiptInfo_duration_filled);

        //get information from Intent
        String product = getIntent().getStringExtra("product");
        String edate = getIntent().getStringExtra("edate");
        String pdate = getIntent().getStringExtra("pdate");
        String duration = getIntent().getStringExtra("duration");

        //set textviews to appropriate information
        txt_product.setText(product);
        txt_edate.setText(edate);
        txt_duration.setText(duration);
        txt_pdate.setText(pdate);

        //return button on click listener to return to the homescreen
        bt_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent to start mainActivity class
                Intent intent = new Intent(ReceiptInfo.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}