package com.example.tpmaps;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText numphone, adresse1, adresse2,adresse3;
    Button signup;
    DBHelper MyDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numphone = (EditText)findViewById(R.id.numphone);
        adresse1 = (EditText)findViewById(R.id.adresse1);
        adresse2 = (EditText)findViewById(R.id.adresse2);
        adresse3 = (EditText)findViewById(R.id.adresse3);

        signup = (Button)findViewById(R.id.btnsignin1);


        MyDB = new DBHelper(this);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numphoneTXT = numphone.getText().toString();
                String adresse1TXT = adresse1.getText().toString();
                String adresse2TXT = adresse2.getText().toString();
                String adresse3TXT = adresse3.getText().toString();

                Boolean checkinsertdata = MyDB.insertuserdata(numphoneTXT, adresse1TXT, adresse2TXT,adresse3TXT);
                if (checkinsertdata==true)
                {
                    Toast.makeText(MainActivity.this, "New Entry Inserted", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                    startActivity(intent);

                }

                else
                    Toast.makeText(MainActivity.this, "Inserted Failed", Toast.LENGTH_SHORT).show();

            }
        });

    }
}