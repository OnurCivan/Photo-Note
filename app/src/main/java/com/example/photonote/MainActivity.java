package com.example.photonote;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button button;
    TextView textView3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button_basla);
        textView3 = findViewById(R.id.textView3);



    }

    public void basla(View view){

        Intent intent = new Intent(getApplicationContext(),Menu.class);

        startActivity(intent);
    }






}
