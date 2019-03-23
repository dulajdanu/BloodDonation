package com.example.blooddonation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegActivity extends AppCompatActivity
{
    private EditText etUserEmail,etUserPassword;
    private Button btnConfirm,btnLog;

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        mAuth = FirebaseAuth.getInstance();

        etUserEmail = (EditText)findViewById(R.id.etUserEmail);
        etUserPassword = (EditText)findViewById(R.id.etUserPassword);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnLog = (Button)findViewById(R.id.btnLog);

        progressDialog = new ProgressDialog(this);

        btnConfirm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CreateNewAccount();
            }
        });

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegActivity.this,MainActivity.class));
            }
        });


    }

    private void CreateNewAccount()
    {
        String email = etUserEmail.getText().toString().trim();
        String password = etUserPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(RegActivity.this,"Please Enter New Username",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(RegActivity.this,"Please Enter New Password",Toast.LENGTH_SHORT).show();
        }
//        else
//        {
//            mAuth.createUserWithEmailAndPassword(email, password)
//                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
//                    {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task)
//                        {
//                            if (task.isSuccessful())
//                            {
//                                Toast.makeText(RegActivity.this, "Account Creation Success.", Toast.LENGTH_SHORT).show();
//                                SendEmailVerification();
//                            }
//                            else
//                            {
//                                Toast.makeText(RegActivity.this, "Account Creation failed.", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//        }
        progressDialog.setMessage("Registering new User..");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(
                this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(RegActivity.this,"User Registration Successful",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(RegActivity.this,"User Registration Failed",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();

                    }
                }
        );

    }
    private void SendEmailVerification()
    {
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null)
        {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>()
            {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(RegActivity.this,"SignUp Success.Please Verify Your Account..",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegActivity.this,MainActivity.class));
                        mAuth.signOut();
                    }
                    else
                    {
                        Toast.makeText(RegActivity.this, "Verification Mail Send Failed", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                    }
                }
            });
        }
    }
}
