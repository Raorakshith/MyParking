package com.example.myparking.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myparking.MainActivity;
import com.example.myparking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

public class RegisterActivity extends AppCompatActivity {

    private EditText username, email, phone, password;
    private Button submit;
    private CountryCodePicker ccp;
    private TextView login;
    FirebaseAuth mAuth;
    DatabaseReference mRef;
    String phones;
    int code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth= FirebaseAuth.getInstance();
        init();
        //code=ccp.getSelectedCountryCodeAsInt();
        //Toast.makeText(this, ""+code, Toast.LENGTH_SHORT).show();
        ccp.registerCarrierNumberEditText(phone);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RegisterActivity.this, ""+ccp.getFullNumberWithPlus(), Toast.LENGTH_SHORT).show();
                CreateNewAccount();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

    }

    private void init() {
        username = findViewById(R.id.userName);
        phone = findViewById(R.id.phone);
        submit = findViewById(R.id.submitBtn);
        ccp = findViewById(R.id.ccp);
        login = findViewById(R.id.loginTxt);
        password = findViewById(R.id.password);
    }
    private void CreateNewAccount() {
        String passwords=password.getText().toString();
        final String name=username.getText().toString();
        phones=ccp.getFullNumberWithPlus();
        final String emphones=phones+"@gmail.com";
         if(TextUtils.isEmpty(passwords)){
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(phones)){
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        }else{
            mAuth.createUserWithEmailAndPassword(emphones,passwords).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        mRef= FirebaseDatabase.getInstance().getReference("userdata").child(mAuth.getCurrentUser().getUid()).child("Profile");
                        mRef.child("useremail").setValue(emphones);
                        mRef.child("username").setValue(name);
                        mRef.child("userphone").setValue(phones);
                        Toast.makeText(RegisterActivity.this, "you are authenticated", Toast.LENGTH_SHORT).show();
                        SendUserToLogin();

                    }
                    else{
                        String message=task.getException().getMessage();
                        Toast.makeText(RegisterActivity.this, "Error occured"+message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void SendUserToLogin() {
        Intent intent=new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
//        if(types.equals("medical")) {
//
//            Intent intent = new Intent(Register.this, Doctordetails.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            finish();
//        }else {
////            Intent intent = new Intent(Register.this, PatientDetails.class);
////            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////            startActivity(intent);
////            finish();
//        }
    }
}