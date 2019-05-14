package com.example.blooddonation;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

public class  Profile extends AppCompatActivity
{
    private Button btnSave;
    private EditText etFullName,etDOB,etBloodGroup,etAddress,etContact1,etContact2;
    private DatabaseReference settingsUserRef;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private int numbOfPart;

    final FirebaseFirestore db = FirebaseFirestore.getInstance();




    private String tokenDev;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        MyApplication myapp = (MyApplication)getApplication();
//        myapp.setData(80);
        String val = myapp.getData();


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            //`Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        tokenDev = task.getResult().getToken();

                        // Log and toast
                        //String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d(TAG, msg);
                        //Toast.makeText(Profile.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        SharedPreferences sharedPref = getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
        String email= sharedPref.getString("email", null);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        settingsUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

        etFullName = (EditText)findViewById(R.id.etFullName);
        etDOB = (EditText) findViewById(R.id.etDOB);
        etBloodGroup = (EditText)findViewById(R.id.etBloodGroup);
        etAddress = (EditText)findViewById(R.id.etAddress);
        etContact1= (EditText)findViewById(R.id.etContact1);
        etContact2 = (EditText)findViewById(R.id.etContact2);
        btnSave = (Button)findViewById(R.id.btnSave);

        etBloodGroup.setText(val);
         btnSave.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 ValidateInfo();
             }
         });

         settingsUserRef.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 if(dataSnapshot.exists())
                 {
                     String username = dataSnapshot.child("fullname").getValue().toString();
                     String bld = dataSnapshot.child("bloodgroup").getValue().toString();
                     etFullName.setText(username);
                     etBloodGroup.setText(bld);
                 }



             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });

    }

    private void ValidateInfo()
    {
        String fullname = etFullName.getText().toString();
        String dob = etDOB.getText().toString();
        String bloodgroup = etBloodGroup.getText().toString();
        String address = etAddress.getText().toString();
        String contact1 = etContact1.getText().toString();
        String contact2 = etContact2.getText().toString();

        if(TextUtils.isEmpty(fullname))
        {
            etFullName.setError("enter your full name");
        }
        else
        {
            UpdateInfo(fullname,dob,bloodgroup,address,contact1,contact2);
            etAddress.setText("paka");

        }
    }

    private void UpdateInfo(String fullname, String dob, String bloodgroup, String address, String contact1, String contact2)
    {
        HashMap userMap = new HashMap();
        userMap.put("fullname",fullname);
        userMap.put("Dob",dob);
        userMap.put("bloodgroup",bloodgroup);
        userMap.put("address",address);
        userMap.put("contact1",contact1);
        userMap.put("contact2",contact2);
        userMap.put("device_token",tokenDev);

//        Map<String, Object> docData = new HashMap<>();
//        docData.put("name", "Los Angeles");
//        docData.put("state", "CA");
//        docData.put("country", "USA");
//        docData.put("regions", Arrays.asList("west_coast", "socal"));
//// Add a new document (asynchronously) in collection "cities" with id "LA"
//        ApiFuture<WriteResult> future = db.collection("Users").document("LA").set(docData);
//// ...
//// future.get() blocks on response
////        System.out.println("Update time : " + future.get().getUpdateTime());
//
//



        db.collection("Users").document().set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Profile.this,"details updated",Toast.LENGTH_LONG).show();
            }
        });
    }


}
