package com.example.animode;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class profile_tab extends Fragment {

    private TextView tvFname, tvEmail, tvPass,tvSignout;
    private FirebaseFirestore fs;
    private FirebaseUser user;
    Button btUpdate;
    private View view;
    private String FName, LName,Pass;

    public profile_tab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.accent_color));
        view = inflater.inflate(R.layout.fragment_profile_tab, container, false);
        initialize();
        listener();

        return view;
    }
    private void initialize() {
        tvFname = view.findViewById(R.id.tvFname);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvPass = view.findViewById(R.id.tvPass);
        tvSignout = view.findViewById(R.id.tvSignout);
        btUpdate = view.findViewById(R.id.btUpdate);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        fs = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();

    }

    private void listener() {
        String userID = user.getUid();

        DocumentReference userRef = fs.collection("Persons").document(userID);
        userRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()) {
                        // User data found
                        Map<String, Object> userData = documentSnapshot.getData();
                        // Access specific fields

                        String em = (String) userData.get("email");
                        Pass = (String) userData.get("password"); //To fix to replace it with asterisk based on its length
                        FName= (String) userData.get("fname");
                        LName = (String) userData.get("lname");

                        // Replace each character in password with an asterisk just for profile tab
                        int inputLength = Pass.length();
                        StringBuilder rep = new StringBuilder();
                        for (int i = 0; i < inputLength; i++) {
                            rep.append("*");
                        }

                        //set up details to profile field
                        tvEmail.setText(em);
                        String fullname = FName+" "+LName;
                        tvFname.setText(fullname);
                        tvPass.setText(rep.toString()); //To fix to replace it with asterisk based on its length

                    } else {
                        Toast.makeText(getActivity(), "Can't retrieve data. Try again", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {

                });

        tvSignout.setOnClickListener(v-> logOut());
        btUpdate.setOnClickListener(v-> {
            Intent intent = new Intent(getContext(), UpdateAct.class);
            intent.putExtra("firstname",FName);
            intent.putExtra("lastname",LName);
            intent.putExtra("password",Pass);
            startActivity(intent);
        });

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