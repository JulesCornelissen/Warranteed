package nl.tue.group2.Warranteed.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StreamDownloadTask;

import java.io.InputStream;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import nl.tue.group2.Warranteed.MainActivity;
import nl.tue.group2.Warranteed.R;

public class ReceiptInfo extends AppCompatActivity {
    //create variables for buttons and views
    Button bt_return;
    TextView txt_name, txt_product, txt_edate, txt_purchase_date, txt_duration;
    ImageView img_expanded;
    ImageButton bt_expand_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_info);
        //hide the top bar from view
        Objects.requireNonNull(getSupportActionBar()).hide();

        //buttons and textviews matching
        bt_return = findViewById(R.id.button_return);
        bt_expand_image = findViewById(R.id.imageButton_zoom);
        txt_product = findViewById(R.id.textView_product_filled);
        txt_name = findViewById(R.id.receiptInfo_name);
        txt_edate = findViewById(R.id.receiptInfo_edate_filled);
        txt_purchase_date = findViewById(R.id.receiptInfo_purchase_date_filled);
        txt_duration = findViewById(R.id.receiptInfo_duration_filled);
        img_expanded = findViewById(R.id.imageView_zoomed);

        //get information from Intent
        String name = getIntent().getStringExtra("name");
        String product = getIntent().getStringExtra("product");
        String expiration_date = getIntent().getStringExtra("expiration_date");
        String purchase_date = getIntent().getStringExtra("purchase_date");
        String duration = getIntent().getStringExtra("duration");
        String picture = getIntent().getStringExtra("image");

        //set textviews to appropriate information
        txt_name.setText(name);
        txt_edate.setText(expiration_date);
        txt_duration.setText(duration);
        txt_purchase_date.setText(purchase_date);
        txt_product.setText(product);

        if (picture != null) {
            //call getReceiptImage to get the image corresponding
            //to the location stored in picture
            getReceiptImage(picture);
        }
        //set all listeners needed
        setListeners();
    }

    /*
     * sets the image of the receipt to the image view
     * picture_path is the location of the picture in firebase storage
     */
    public void getReceiptImage(String picture_path){
        //to prioritze downloading the stream
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //get stream at the file location
        FirebaseStorage.getInstance().getReference().child("receipt/").child(picture_path).getStream()
                .addOnSuccessListener(new OnSuccessListener<StreamDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(StreamDownloadTask.TaskSnapshot taskSnapshot) {
                        //when successful an InputStream is returned
                        InputStream stream = taskSnapshot.getStream();
                        //make a bitmap of this sream
                        Bitmap bitmap = BitmapFactory.decodeStream(stream);
                        //set the bitmap as image to the button and image view
                        bt_expand_image.setImageBitmap(bitmap);
                        img_expanded.setImageBitmap(bitmap);
                    }
                });
    }

    /*
     * Sets OnClickListeners to bt_expand_image, img_expanded and bt_return
     */
    public void setListeners(){
        //click on the picture to enlarge it
        bt_expand_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set this button to invisible
                bt_expand_image.setVisibility(View.INVISIBLE);
                //set the large image to visible
                img_expanded.setVisibility(View.VISIBLE);
            }
        });

        //click on picture to make it small
        img_expanded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set large image to invisible
                img_expanded.setVisibility(View.INVISIBLE);
                //set the image button back to visible
                bt_expand_image.setVisibility(View.VISIBLE);
            }
        });

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