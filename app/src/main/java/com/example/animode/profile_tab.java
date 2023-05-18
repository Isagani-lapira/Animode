package com.example.animode;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
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

public class profile_tab extends Fragment {

    private TextView tvFname, tvLname, tvEmail, tvPass,tvSignout;
    private FirebaseFirestore fs;
    private FirebaseUser user;
    private View view;

    public profile_tab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile_tab, container, false);
        initialize();
        listener();

        return view;
    }
    private void initialize() {
        tvFname = view.findViewById(R.id.tvFname);
        tvLname = view.findViewById(R.id.tvLname);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvPass = view.findViewById(R.id.tvPass);
        tvSignout = view.findViewById(R.id.tvSignout);


        FirebaseAuth auth = FirebaseAuth.getInstance();
        fs = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();

    }

    private void listener() {
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

                            // Replace each character in password with an asterisk just for profile tab
                            int inputLength = pw.length();
                            StringBuilder rep = new StringBuilder();
                            for (int i = 0; i < inputLength; i++) {
                                rep.append("*");
                            }

                            //set up details to profile field
                            tvEmail.setText(em);
                            tvFname.setText(fn);
                            tvLname.setText(ln);
                            tvPass.setText(rep.toString()); //To fix to replace it with asterisk based on its length
                        }
                    });
        }

        tvSignout.setOnClickListener(v-> logOut());
    }

    private void logOut() {
        //ask first if really want to sign out

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customLayout = inflater.inflate(R.layout.alert_dial_layout, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.TransparentAlertDialogTheme);
        builder.setView(customLayout);

        Dialog dialog = builder.create();
        dialog.show();
        Button btCancel = customLayout.findViewById(R.id.btCancel);
        Button btOkay = customLayout.findViewById(R.id.btOkay);
        //cancel the log out

        btCancel.setOnClickListener(v-> dialog.dismiss());
        //log out the user
        btOkay.setOnClickListener(v->{
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getContext(),loginpage.class));
        });


    }
}