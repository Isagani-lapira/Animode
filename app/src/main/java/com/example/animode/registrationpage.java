package com.example.animode;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class registrationpage extends AppCompatActivity {

    private EditText etFname,etLname,etEmail,etPass,etConPass;
    private Button btnReg;
    private FirebaseAuth rauth;
    private FirebaseFirestore fs;
    private TextView tvBack;
    private final Context CONTEXT = registrationpage.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        getWindow().setStatusBarColor(getResources().getColor(R.color.accent_color));
        initialize();
        fnctn();
        listener();
    }

    private void listener() {
        tvBack.setOnClickListener(v-> startActivity(new Intent(CONTEXT, loginpage.class)));
    }

    private void initialize() {
        etFname = findViewById(R.id.etFname);
        etLname = findViewById(R.id.etLname);

        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        etConPass = findViewById(R.id.etConPass);
        btnReg = findViewById(R.id.btnReg);
        tvBack = findViewById(R.id.tvBack);

        rauth = FirebaseAuth.getInstance();
        fs = FirebaseFirestore.getInstance();
    }

    private void fnctn() {
        btnReg.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPass.getText().toString();
            String firstname = etFname.getText().toString();
            String lastname = etLname.getText().toString();

            if(isFieldMissing()){
                Toast.makeText(CONTEXT, "Please answer all the input fields.", Toast.LENGTH_SHORT).show();
            }
           else{
                rauth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        Map<String, Object> accs = new HashMap<>();
                        accs.put("email", email);
                        accs.put("password", password);
                        accs.put("fname", firstname);
                        accs.put("lname", lastname);
                        String userId = rauth.getCurrentUser().getUid();

                        DocumentReference userDocRef = fs.collection("Persons").document(userId);
                        userDocRef.set(accs)
                                .addOnSuccessListener(documentReference -> Toast.makeText(registrationpage.this, "Registered Succesfully!", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(registrationpage.this, "Error, please try again!!!!!", Toast.LENGTH_SHORT).show());

                        startActivity(new Intent(registrationpage.this, loginpage.class));
                    } else {
                        Toast.makeText(registrationpage.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });

            }


        });

    }

    private boolean isFieldMissing() {
        EditText[] editTexts = {etFname,etLname,etEmail,etPass,etConPass};

        int flag = 0;

        //check if the field has a missing input
        for(EditText editText: editTexts){
            if(editText.getText().toString().equals("")){
                editText.setError("Missing field");
                flag=1;
            }

        }
        return (flag==1)?true:false;
    }

}