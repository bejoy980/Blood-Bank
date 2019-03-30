package com.example.bloodbank;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private TextView txt_signup,txt_forget;
    private EditText login_email,login_password;
    private Button btn_login;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        if(firebaseAuth.getCurrentUser()!=null && firebaseAuth.getCurrentUser().isEmailVerified()){
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }
        txt_signup=(TextView) findViewById(R.id.signup_txt);
        login_email=(EditText) findViewById(R.id.email_login);
        txt_forget=(TextView) findViewById(R.id.forget_text);
        login_password=(EditText) findViewById(R.id.password_login);
        btn_login=(Button) findViewById(R.id.login_btn);
        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));
            }
        });
        txt_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ResetPassword.class));
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail=login_email.getText().toString().trim();
                String mPassword=login_password.getText().toString().trim();
                if(TextUtils.isEmpty(mEmail)){
                    login_email.setError("Required Field...");
                    return;
                }
                if(TextUtils.isEmpty(mPassword)){
                    login_password.setError("Required Field...");
                    return;
                }
                progressDialog.setMessage("Processing...");
                progressDialog.show();
                firebaseAuth.signInWithEmailAndPassword(mEmail,mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            if(firebaseAuth.getCurrentUser().isEmailVerified()){
                                Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                                progressDialog.dismiss();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Please verify your email address",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
