package com.example.blooddonation;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.blooddonation.Service.saveData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.*;
/*
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
*/

public class RequestActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    ArrayList<String> Bldlist=new ArrayList<String>();
//    SharedPreferences prefs = getSharedPreferences("MyPrefs",MODE_PRIVATE);
    String storedUsrMail ;
    String storedNum;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

//        storedUsrMail = String.valueOf(saveData.read(getApplicationContext(),"email",""));
//        MyApplication myapp = (MyApplication)getApplication();
////        myapp.setData(80);
//        String val = myapp.getData();
//        System.out.println("This is the val" + val);


        mDatabase = FirebaseDatabase.getInstance().getReference();

        final Button Apositive = findViewById(R.id.btnApositive);
        Button Anegative = findViewById(R.id.btnAmin);
        Button Bpositive = findViewById(R.id.btnBpositive);
        Button Bnegative = findViewById(R.id.btnBmin);
        Button Opositive = findViewById(R.id.btnOpositive);
        Button Onegative = findViewById(R.id.btnOmin);
        Button ABpositive = findViewById(R.id.btnABpositive);
        Button ABnegative = findViewById(R.id.btnABneg);

        Apositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bldlist.add("O-");
                Bldlist.add("O+");
                Bldlist.add("A-");
                Bldlist.add("A+");
                findUsers(Bldlist, getApplicationContext());
                Bldlist.clear();
            }
        });

        Anegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bldlist.add("O-");
                Bldlist.add("A-");

                findUsers(Bldlist, getApplicationContext());
                Bldlist.clear();

            }
        });
        Bpositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bldlist.add("O-");
                Bldlist.add("O+");
                Bldlist.add("B+");

                Bldlist.add("B-");
                findUsers(Bldlist, getApplicationContext());
                Bldlist.clear();
            }
        });
        Bnegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bldlist.add("O-");
                Bldlist.add("B-");
                findUsers(Bldlist, getApplicationContext());
                Bldlist.clear();
            }
        });
        Opositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bldlist.add("O-");
                Bldlist.add("O+");
                findUsers(Bldlist, getApplicationContext());
                Bldlist.clear();
            }
        });
        Onegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bldlist.add("O-");

                findUsers(Bldlist, getApplicationContext());
                Bldlist.clear();
            }
        });
        ABpositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bldlist.add("O-");
                Bldlist.add("O+");
                Bldlist.add("B+");
                Bldlist.add("B-");
                Bldlist.add("A-");
                Bldlist.add("A+");
                Bldlist.add("AB+");
                Bldlist.add("AB-");
                findUsers(Bldlist, getApplicationContext());
                Bldlist.clear();
            }
        });
        ABnegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bldlist.add("O-");
                Bldlist.add("B-");
                Bldlist.add("A-");

                Bldlist.add("AB-");
                findUsers(Bldlist, getApplicationContext());
                Bldlist.clear();
            }
        });

    }

    public void findUsers(final List<String>bldList, final Context context) {

        /*String url = "http://192.168.1.101/BloodDonation/push_notication.php";

        Call<ResponseBody> call = RetrofitClient
                .getmInstance()
                .getApi()
                .sendPushMsg("ssss");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s = response.body().toString();
                Toast.makeText(RequestActivity.this, s, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RequestActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
*/

        final FirebaseFirestore dbnew = FirebaseFirestore.getInstance();
        dbnew.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot ds : task.getResult()) {
                                String bloodType = ds.getString("bloodgroup");
                                if (bloodType != null) {


                                    for (String bld : bldList) {
                                        if (bloodType.equals(bld)) {
                                            Toast toast = Toast.makeText(context, "blood type found", Toast.LENGTH_SHORT);
                                            toast.show();
                                            final String deviceTkn = ds.getString("device_token");
                                            final String fullName = ds.getString("fullname");
                                            final String contactNum = ds.getString("contact1");
                                            //final String patientName = ds.getString("fullname");
                                            RequestQueue queue = Volley.newRequestQueue(context);
                                            String url = "http://192.168.1.102/BloodDonation/push_notification.php";
                                            // Request a string response from the provided URL.
                                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            // Display the first 500 characters of the response string.
                                                            //textView.setText("Response is: "+ response.substring(0,500));
                                                            Log.i("RequestActivity", response);
                                                        }
                                                    }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    //Log.e("RequestActivity",error.getMessage());
                                                }
                                            }) {
                                                @Override
                                                protected Map<String, String> getParams() {
                                                    Map<String, String> params = new HashMap<String, String>();
                                                    params.put("token", deviceTkn);
                                                    String body = "The patient " + fullName + " needs your blood . This is the contact number" + contactNum;
                                                    params.put("body", body);
                                                    return params;
                                                }
                                            };

                                            // Add the request to the RequestQueue.
                                            queue.add(stringRequest);
                                        }
                                    }
                                }
                            }
                        }

                    }


                });
    }


}
