package com.example.animode;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class registrationpage extends AppCompatActivity {

    EditText etFname,etLname,etEmail,etPass,etConPass;
    Button btnReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initialize();
        listener();
    }

    private void listener() {
        btnReg.setOnClickListener(v->{
            if(checkField()){
                String password = etPass.getText().toString();
                String confirmPass = etConPass.getText().toString();

                DBPerson dbPerson = new DBPerson();

                //check first if the 2 password matches
                if(password.equals(confirmPass)){
                    String FName = etFname.getText().toString();
                    String LName = etLname.getText().toString();
                    String email = etEmail.getText().toString();

                    Person person = new Person(FName,LName,email);
                    dbPerson.add(person).addOnSuccessListener(success -> {
                        Toast.makeText(this, "Account is created", Toast.LENGTH_SHORT).show();

                    }).addOnFailureListener(failure->{
                        Toast.makeText(this, "Please try again", Toast.LENGTH_SHORT).show();
                    });

                }
                else
                    Toast.makeText(this, "Password not matched!", Toast.LENGTH_SHORT).show();

            }
            else
                Toast.makeText(this, "Incomplete input", Toast.LENGTH_SHORT).show();

        });
    }

    private boolean checkField() {
        EditText[] editTexts = {etFname,etLname,etEmail,etPass,etConPass};

        //check if the field has a missing input
        for(EditText editText: editTexts){
            if(editText.getText().toString().equals(""))
                return false;
        }
        return true;
    }

    private void initialize() {
        etFname = findViewById(R.id.etFname);
        etLname = findViewById(R.id.etLname);
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        etConPass = findViewById(R.id.etConPass);
        btnReg = findViewById(R.id.btnReg);
    }
}