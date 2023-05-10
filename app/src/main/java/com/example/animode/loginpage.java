package com.example.animode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class loginpage extends AppCompatActivity {

    private EditText etEmail, etPass;
    private TextView tvRegister;
    private Button signin;
    private Intent intent;
    private FirebaseAuth li_auth;
    private final Context context = loginpage.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initialize();
        listener();

    }

    private void listener() {

        etPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            //enable the button to be enabled
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String email = etEmail.getText().toString().trim();
                if(!email.equals("")){
                    signin.setEnabled(true);
                    signin.setBackgroundColor(getColor(R.color.accent_color));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //login
        signin.setOnClickListener(v -> login());

        //register
        tvRegister.setOnClickListener(v-> {
            intent = new Intent(context, registrationpage.class);
            startActivity(intent);
        });
    }


    private void initialize() {
        li_auth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        tvRegister = findViewById(R.id.tvRegister);
        signin = findViewById(R.id.signin);
    }

    private void login() {
        String u, p;

        u = etEmail.getText().toString().trim();
        p = etPass.getText().toString().trim();

        if(u.isEmpty()){
            etEmail.setError("You must enter a username!");
        }

        if(p.isEmpty()){
            etPass.setError("You must enter a password!");
        }

        else {
            li_auth.signInWithEmailAndPassword(u,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(loginpage.this, "Logged in succesfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(loginpage.this, homepage.class));
                    }
                    else {
                        Toast.makeText(loginpage.this, "Log in failed. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}