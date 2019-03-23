package com.example.blooddonation;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.api.AnnotationsProto.http;

public class RequestActivity extends AppCompatActivity {

    private   DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);


        mDatabase = FirebaseDatabase.getInstance().getReference();

        Button Apositive = findViewById(R.id.btnApositive);
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
                findUsers("ssss");
            }
        });













    }

    public void findUsers(final String BloodType)
    {

        String url = "http://192.168.1.101/BloodDonation/push_notication.php";

        Call<ResponseBody> call = RetrofitClient
                .getmInstance()
                .getApi()
                .sendPushMsg("ssss");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s = response.body().toString();
                Toast.makeText(RequestActivity.this,s,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RequestActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();

            }
        });


        final FirebaseFirestore dbnew = FirebaseFirestore.getInstance();
        dbnew.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for(DocumentSnapshot ds : task.getResult())
                            {
                                String bloodType = ds.getString(" bloodgroup");
                                if(bloodType.equals(BloodType))
                                {
                                    String deviceTkn = ds.getString(" device_token");
                                    HttpURLConnection connection = null;
                                    try {
                                        URL url = new URL("http://localhost/BloodDonation/push_notication.php");
                                        connection = (HttpURLConnection) url.openConnection();
                                        connection.setRequestMethod("POST");





                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }

                            }
                        }
                    }
                });


    }


}
