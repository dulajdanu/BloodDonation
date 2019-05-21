package com.example.blooddonation;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class completedActi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final TextView month = findViewById(R.id.MonthVal);
        final TextView total = findViewById(R.id.totVal);


        setContentView(R.layout.activity_completed);
        SharedPreferences sharedPref = getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
        final String email= sharedPref.getString("email", null);
        final FirebaseFirestore dbnew = FirebaseFirestore.getInstance();
        dbnew.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot ds : task.getResult()) {
                                String usrMail = ds.getString("email");
                                if (usrMail != null) {

//                                    Toast toast = Toast.makeText(context, "blood type found", Toast.LENGTH_SHORT);
//                                    toast.show();

                                    if (usrMail.equals(email)) {
                                        final String Cmonth = ds.getString("noMonth");
                                        final String Ctot = ds.getString("noTotal");
                                        //final String patientName = ds.getString("fullname");
                                        // Request a string response from the provided URL.
                                        month.setText(Cmonth);
                                        total.setText(Ctot);

                                    }

                                }
                            }
                        }

                    }


                });

    }

//    void changeVAl(int x)
//    {
//        TextView count1 ;
//        TextView count2;
//
//        count1 = findViewById(R.id.textView8);
//        count2 = findViewById(R.id.textView9);
//
//        count1.setText(x);
//        count2.setText(++x);
//
//    }
}
