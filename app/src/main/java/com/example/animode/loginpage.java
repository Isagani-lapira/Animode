package com.example.animode;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;


public class loginpage extends AppCompatActivity {

    private EditText etEmail, etPass;
    private Button signin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.login_activity);

        initialize();
    }

    private void initialize() {
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        signin = findViewById(R.id.signin);

        signin.setOnClickListener(v -> {});
    }
}