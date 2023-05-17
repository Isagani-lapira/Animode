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
//            String fn = etFname.getText().toString().trim();
//            String ln = etLname.getText().toString().trim();
//
//            String ea = etEmail.getText().toString().trim();
//            String pw = etPass.getText().toString().trim();
//            String cpw = etConPass.getText().toString().trim();

            if(isFieldMissing()){
                Toast.makeText(context, "Answer the all input fields", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "complete lahat", Toast.LENGTH_SHORT).show();
            }
//            if (fn.isEmpty()){
//                etFname.setError("You must enter a first name!");
//            }
//
//            if (ln.isEmpty()){
//                etLname.setError("You must enter a last name!");
//            }
//
//            if (ea.isEmpty()) {
//                etEmail.setError("You must enter an email!");
//            }
//
//            if (pw.isEmpty()) {
//                etPass.setError("You must enter a password!");
//            }
//
//            if (cpw.isEmpty()) {
//                etConPass.setError("Password and confirm password must match");
//            }

//            else {
//                rauth.createUserWithEmailAndPassword(ea, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(registrationpage.this, "Registered Succesfully!", Toast.LENGTH_SHORT).show();
//
//                            Map<String, Object> person = new HashMap<>();
//
//                            person.put("email", ea);
//                            person.put("fname", fn);
//                            person.put("lname", ln);
//
//                            fs.collection("Persons")
//                                    .add(person)
//                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                        @Override
//                                        public void onSuccess(DocumentReference documentReference) {
//                                            Toast.makeText(registrationpage.this, "CONGRATS!!!!!", Toast.LENGTH_SHORT).show();
//                                        }
//                                    })
//                                    .addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            Toast.makeText(registrationpage.this, "ERROR!!!!!", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//
//                            startActivity(new Intent(registrationpage.this, loginpage.class));
//                        }
//
//                        else {
//                            Toast.makeText(registrationpage.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
//                        }
//
//
//
//
//
////                        Person prsn = new Person(fn, ln, ea);
////                        DBPerson dbp = new DBPerson();
////                        dbp.add(prsn).addOnSuccessListener(success->{
////                            Toast.makeText(registrationpage.this, "Registered Succesfully!", Toast.LENGTH_SHORT).show();
////                        }).addOnFailureListener(error->{
////                            Toast.makeText(registrationpage.this, "Error!", Toast.LENGTH_SHORT).show();
////                        });
//                    }
//                });
//            }
//
        });

    }


//    private void listener() {
//        btnReg.setOnClickListener(v->{
//            if(checkField()){
//                String password = etPass.getText().toString();
//                String confirmPass = etConPass.getText().toString();
//
//                DBPerson dbPerson = new DBPerson();
//
//                //check first if the 2 password matches
//                if(password.equals(confirmPass)){
//                    String FName = etFname.getText().toString();
//                    String LName = etLname.getText().toString();
//                    String email = etEmail.getText().toString();
//
//                    Person person = new Person(FName,LName,email);
//                    dbPerson.add(person).addOnSuccessListener(success -> {
//                        Toast.makeText(this, "Account is created", Toast.LENGTH_SHORT).show();
//
//                    }).addOnFailureListener(failure->{
//                        Toast.makeText(this, "Please try again", Toast.LENGTH_SHORT).show();
//                    });
//
//                }
//                else
//                    Toast.makeText(this, "Password not matched!", Toast.LENGTH_SHORT).show();
//
//            }
//            else
//                Toast.makeText(this, "Incomplete input", Toast.LENGTH_SHORT).show();
//
//        });
//    }

    private boolean isFieldMissing() {
        EditText[] editTexts = {etFname,etLname,etEmail,etPass,etConPass};

        int flag = 0;

        //check if the field has a missing input
        for(EditText editText: editTexts){
            if(editText.getText().toString().equals("")){
                editText.setError("You must enter an email!");
                flag=1;
            }

        }

        if (flag == 1) {
            return true;
        }
        return false;
    }

}