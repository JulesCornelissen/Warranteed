package nl.tue.group2.Warranteed.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class FireBase extends AppCompatActivity {

    // Need to define URL for the Firebase database since it is not stored in the US
    public static String FIREBASE_DATABASE_URL = "https://warrenteed-60226-default-rtdb.europe-west1.firebasedatabase.app/";

    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db;

    private static String TAG = "firebase";

    public FireBase() {
        this.db = FirebaseFirestore.getInstance();
    }


    public void readData() {
        //        Query getCustomers = db.collection("Customers");
        db.collection("Customers").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.v(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.v(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }
}
