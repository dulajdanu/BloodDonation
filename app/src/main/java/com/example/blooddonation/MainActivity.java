package com.example.blooddonation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.blooddonation.Service.saveData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
{
    private Button btnLogin,btnRegister;
    private EditText etName,etPassword;
    private FirebaseAuth mAuth;
    private Boolean emailAddressChecker;
    private String email;
    private   String StoredMail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        etName = findViewById(R.id.etName);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AccountLogin();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MainActivity.this,RegActivity.class));
                finish();
            }
        });
    }



    private void AccountLogin()
    {
        email = etName.getText().toString();
        String password = etPassword.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(MainActivity.this,"Please Enter Your Email",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(MainActivity.this,"Please Enter Your Password",Toast.LENGTH_SHORT).show();
        }
        else
        {
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(MainActivity.this, "Authentication Success.", Toast.LENGTH_SHORT).show();
                       //startActivity(new Intent(MainActivity.this,Home.class));
                       //finish();

                        VerifyEmailAddress();


                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    protected void onStart()
    {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
        {
            startActivity(new Intent(MainActivity.this,Home.class));
            finish();
        }
    }
    private void VerifyEmailAddress()
    {
        FirebaseUser user = mAuth.getCurrentUser();
        emailAddressChecker = user.isEmailVerified();

        if(emailAddressChecker)
        {
//            SharedPreferences.Editor editor =  getSharedPreferences("MyPrefs",MODE_PRIVATE).edit();
//            editor.putString("email",email);+
            MyApplication myapp = (MyApplication)getApplication();
            myapp.setData(email);
            startActivity(new Intent(MainActivity.this,Home.class));
        }
        else
        {
            Toast.makeText(MainActivity.this, "Please Verify Your Account", Toast.LENGTH_SHORT).show();
//            SharedPreferences.Editor editor =  getSharedPreferences("MyPrefs",MODE_PRIVATE).edit();
//            editor.putString("email",email);
            //StoredMail = email;
            user.sendEmailVerification();
//            saveData.save(getApplicationContext(),"email",email);
            //mAuth.signOut();
        }
    }
}
