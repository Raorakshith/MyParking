package com.example.myparking;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myparking.Authentication.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SplahScreen extends AppCompatActivity {
FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splah_screen);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
             new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(firebaseUser!=null) {

                    Intent intent = new Intent(SplahScreen.this, ShowParking.class);
                    SplahScreen.this.startActivity(intent);
                    SplahScreen.this.finish();
                }else{
                    Intent intent = new Intent(SplahScreen.this, LoginActivity.class);
                    SplahScreen.this.startActivity(intent);
                    SplahScreen.this.finish();
                }
            }
        },2000);
    }

}
