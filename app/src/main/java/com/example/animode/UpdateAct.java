package com.example.animode;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class UpdateAct extends AppCompatActivity {

    EditText etFname,etLname,etPass,etConPass;
    Button btCancel, btUpdate;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        getWindow().setStatusBarColor(getColor(R.color.accent_color));
        initialize();
        listener();
    }

    private void initialize() {
        Intent intent = getIntent();
        String fname = intent.getStringExtra("firstname");
        String lname = intent.getStringExtra("lastname");
        String password = intent.getStringExtra("password");

        etFname = findViewById(R.id.etFname);
        etLname = findViewById(R.id.etLname);
        etPass = findViewById(R.id.etPass);
        etConPass = findViewById(R.id.etConPass);
        btCancel = findViewById(R.id.btCancel);
        btUpdate = findViewById(R.id.btUpdate);

        etFname.setText(fname);
        etLname.setText(lname);
        etPass.setText(password);
        etConPass.setText(password);
    }
    private void listener() {
        btCancel.setOnClickListener(v-> startActivity(new Intent(context, homepage.class)));
        btUpdate.setOnClickListener(v-> proceed());
    }

    private void updateInfo() {

        FirebaseFirestore fbStore = FirebaseFirestore.getInstance();
        FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();
        String userID = FirebaseAuth.getInstance().getUid();
        DocumentReference docRef = fbStore.collection("Persons").document(userID);

        //fields
        String FName = etFname.getText().toString();
        String LName = etLname.getText().toString();
        String Pass = etPass.getText().toString();

        docRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {

                        //store a new data
                        Map<String, Object> newData = new HashMap<>();
                        newData.put("fname", FName);
                        newData.put("lname", LName);
                        newData.put("password", Pass);

                        //update new old data using the store data above
                        docRef.update(newData)
                                .addOnSuccessListener(success -> {
                                    //update first the password
                                    auth.updatePassword(Pass).addOnSuccessListener(unused -> {
                                        Toast.makeText(context, "Updated successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(context, homepage.class));
                                    }).addOnFailureListener(e -> Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show());

                                })
                                .addOnFailureListener(failure -> Toast.makeText(context, failure.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show());



    }

    //alert dialog
    private void proceed(){

        LayoutInflater inflater = LayoutInflater.from(context);
        View customLayout = inflater.inflate(R.layout.alert_dial_layout, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.TransparentAlertDialogTheme);
        builder.setView(customLayout);

        Dialog dialog = builder.create();
        dialog.show();

        Button btCancel = customLayout.findViewById(R.id.btCancel);
        Button btOkay = customLayout.findViewById(R.id.btOkay);
        TextView tvDescript = customLayout.findViewById(R.id.tvDescript);

        tvDescript.setText("You are about to update information \nDo you want to continue?");
        //cancel the log out
        btCancel.setOnClickListener(v -> dialog.dismiss());
        //log out the user
        btOkay.setOnClickListener(v -> updateInfo());

    }

}

