package com.example.animode;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.Length;

public class profile_tab extends Fragment {

    private TextView tvFname, tvLname, tvEmail, tvPass;
    private Button edit;
    private FirebaseAuth auth;
    private FirebaseFirestore fs;
    private FirebaseUser user;

    public profile_tab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_tab, container, false);
//
//        i();
//        l();

        return view;
    }

    private void i() {
//        tvFname = findViewById(R.id.tvFname);
//        tvLname = findViewById(R.id.tvLname);
//        tvEmail = findViewById(R.id.tvEmail);
//        tvPass = findViewById(R.id.tvPass);

        auth = FirebaseAuth.getInstance();
        fs = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();
    }

    private void l() {
        if (user != null) {
            String uemail = user.getEmail();

            fs.collection("Persons")
                    .whereEqualTo("email", uemail)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot ss = task.getResult();
                            DocumentSnapshot docu = ss.getDocuments().get(0);
                            String em = docu.getString("email");
                            String pw = docu.getString("password"); //To fix to replace it with asterisk based on its length
                            String fn = docu.getString("fname");
                            String ln = docu.getString("lname");
                            tvEmail.setText(em);
                            tvFname.setText(fn);
                            tvLname.setText(ln);
                            tvPass.setText(pw); //To fix to replace it with asterisk based on its length
                        }
                    });
        }
    }
}