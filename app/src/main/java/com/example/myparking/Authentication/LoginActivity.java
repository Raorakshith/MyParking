package com.example.myparking.Authentication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.myparking.EditProfile;
import com.example.myparking.MainActivity;
import com.example.myparking.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//import com.example.ecomm.Booking.DisplayProducts;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Button loginBtn;
    private TextView signup, forgotpwd;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    DatabaseReference mRef;
    String name="N/A";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        editor=sharedPreferences.edit();
        mAuth= FirebaseAuth.getInstance();
        init();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInAccount();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        forgotpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });

    }

    private void init() {
        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        loginBtn = findViewById(R.id.loginBtn);
        signup = findViewById(R.id.signupTxt);
        forgotpwd = findViewById(R.id.forgotpwdTxt);

    }

    private void SignInAccount() {
        final String emails = "+91"+email.getText().toString()+"@gmail.com";
        String pass = password.getText().toString();

        if (TextUtils.isEmpty(emails)) {
            Toast.makeText(this, "Please write your email...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(emails, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        mRef= FirebaseDatabase.getInstance().getReference("userdata").child(mAuth.getCurrentUser().getUid()).child("Profile");
                        mRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.hasChild("username")) {
                                    name = snapshot.child("username").getValue().toString();
                                    editor.putString("useremail",email.getText().toString());
                                    editor.putString("username",name);
                                    editor.commit();
                                    Toast.makeText(LoginActivity.this, "Successful LogIn", Toast.LENGTH_SHORT).show();
                                    SendUserTodetails();
                                }else{
                                    name="N/A";
                                    editor.putString("useremail",email.getText().toString());
                                    editor.putString("username",name);
                                    editor.commit();
                                    Toast.makeText(LoginActivity.this, "Successful LogIn", Toast.LENGTH_SHORT).show();
                                    SendUserTodetails();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        //progressDialog.dismiss();

                    } else {
                        String message = task.getException().getMessage();
                        Toast.makeText(LoginActivity.this, "Error occured" + message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void SendUserTodetails() {
        startActivity(new Intent(LoginActivity.this, EditProfile.class));

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        mAuth.addAuthStateListener(authStateListener);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if(authStateListener!=null){
//            mAuth.removeAuthStateListener(authStateListener);
//        }
//    }
}