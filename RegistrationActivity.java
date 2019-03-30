package com.example.bloodbank;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {
    private TextView txt_login;
    private EditText signup_email,signup_password,signup_name;
    private Button btn_signup;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private Spinner signup_city,signup_group;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Userinfo");
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        String uId=firebaseUser
        progressDialog=new ProgressDialog(this);
        txt_login=(TextView) findViewById(R.id.login_txt);
        signup_email=(EditText) findViewById(R.id.email_signup);
        String city[]={"Amritsar","Barnala","Bathinda","Faridkot","Fatehgarh Sahib","Firozpur","Fazilka","Gurdaspur","Hoshiarpur","Jalandhar","Kapurthala","Ludhiana","Mansa","Moga","Sri Muktsar Sahib","Pathankot","Patiala","Rupnagar","Sahibzada Ajit Singh Nagar","Sangrur","Shahid Bhagat Singh Nagar","Taran Taran"};
        String group[]={"O-","O+","A-","A+","B+","B-","AB+","AB-"};
        signup_city=(Spinner) findViewById(R.id.city_signup);
        signup_name=(EditText)findViewById(R.id.name_signup);
        signup_group=(Spinner) findViewById(R.id.group_signup);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,city);
        signup_city.setAdapter(arrayAdapter);
        signup_city.setPrompt("Select City:");
        ArrayAdapter<String> arrayAdapter1=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,group);
        signup_group.setAdapter(arrayAdapter1);
        signup_group.setPrompt("Select Blood Group:");
        signup_password=(EditText) findViewById(R.id.password_signup);
        btn_signup=(Button) findViewById(R.id.signup_btn);
        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail=signup_email.getText().toString().trim();
                String mPassword=signup_password.getText().toString().trim();
                String mName=signup_name.getText().toString().trim();
                if(TextUtils.isEmpty(mName)){
                    signup_email.setError("Required Field...");
                    return;
                }
                if(TextUtils.isEmpty(mEmail)){
                    signup_email.setError("Required Field...");
                    return;
                }
                if(TextUtils.isEmpty(mPassword)){
                    signup_password.setError("Required Field...");
                    return;
                }
                progressDialog.setMessage("Processing...");
                progressDialog.show();
                firebaseAuth.createUserWithEmailAndPassword(mEmail,mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(),"Registration Successful. Please check your email for verification ",Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"Registration Failed",Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                        }else
                        {
                            Toast.makeText(getApplicationContext(),"Registration Failed",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
            }
        });
    }
}
