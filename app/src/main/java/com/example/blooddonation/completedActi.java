package com.example.blooddonation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class completedActi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed);
    }

    void changeVAl(int x)
    {
        TextView count1 ;
        TextView count2;

        count1 = findViewById(R.id.textView8);
        count2 = findViewById(R.id.textView9);

        count1.setText(x);
        count2.setText(++x);

    }
}
