package com.example.animode;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
    private Context context = registrationpage.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initialize();
        fnctn();
        listener();
    }

    private void listener() {
        tvBack.setOnClickListener(v->{
            startActivity(new Intent(context, loginpage.class));
        });
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
            String ea = etEmail.getText().toString();
            String pw = etPass.getText().toString();
            String fn = etFname.getText().toString();
            String ln = etLname.getText().toString();
            String cpw = etConPass.getText().toString();

            if(isFieldMissing()){
                Toast.makeText(context, "Please answer all the input fields.", Toast.LENGTH_SHORT).show();
            }

//            else if (pw!=cpw){
//                Toast.makeText(context, "Password and Confirm Password must match!", Toast.LENGTH_SHORT).show();
//            }


           else{
                rauth.createUserWithEmailAndPassword(ea, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Map<String, Object> accs = new HashMap<>();
                            accs.put("email", ea);
                            accs.put("password", pw);
                            accs.put("fname", fn);
                            accs.put("lname", ln);

                            fs.collection("Persons")
                                    .add(accs)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(registrationpage.this, "Registered Succesfully!", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(registrationpage.this, "Error, please try again!!!!!", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            startActivity(new Intent(registrationpage.this, loginpage.class));
                        } else {
                            Toast.makeText(registrationpage.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
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
                editText.setError("Please answer all the input fields!");
                flag=1;
            }

        }

        if (flag == 1) {
            return true;
        }
        return false;
    }

}