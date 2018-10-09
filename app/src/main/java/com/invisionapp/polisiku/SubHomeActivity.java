package com.invisionapp.polisiku;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

public class SubHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_home);

        LinearLayout internet = findViewById(R.id.linearInternet);

        LinearLayout videoCall = findViewById(R.id.linearVideo);

        LinearLayout telephone = findViewById(R.id.linearTelephone);

        internet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubHomeActivity.this, DialPhoneActivity.class);
                startActivity(intent);
            }
        });

        videoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubHomeActivity.this, VideoCallActivity.class);
                startActivity(intent);
            }
        });

        telephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TelephoneClick(v);
            }
        });



    }

    @SuppressLint("MissingPermission")
    public void TelephoneClick(View view){
        try {
            Intent callIntent =new Intent(Intent.ACTION_CALL);
            Intent intent = new Intent(callIntent);
            intent.setPackage("com.android.phone");
            callIntent.setData(Uri.parse("tel: 08989725807"));
            startActivity(callIntent);
        }catch (ActivityNotFoundException activityException){
            Log.e("Calling a Phone Number", "Call failed", activityException);
        }

    }
}
