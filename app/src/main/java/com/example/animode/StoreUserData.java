package com.example.animode;

import android.content.Context;
import android.widget.Toast;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class StoreUserData {

    private final String TITLE, EPISODE, USERID;
    private final FirebaseFirestore FBSTORE ;
    private final Context CONTEXT;

    public StoreUserData(Context context,String userID ,String title, String episode, FirebaseFirestore fbStore){
        TITLE = title;
        EPISODE = episode;
        CONTEXT = context;
        FBSTORE = fbStore;
        USERID = userID;
    }

    public void insertToWatch(){
        String Title = "Title";
        String episode = "Episode";

        DocumentReference userMoviesRef = FBSTORE.collection("userAnime")
                .document(USERID); //add to the collection of user anime data

        Map<String, Object> userAnime = new HashMap<>();
        userAnime.put(Title,TITLE);
        userAnime.put(episode,EPISODE);
        userMoviesRef.get()
                        .addOnSuccessListener(documentSnapshot -> {
                            //check if there are data stored
                            if(documentSnapshot.exists()){
                                DocumentReference animeCollectionRef = userMoviesRef.collection("toWatch").document();

                                //save the anime to the user's data
                                animeCollectionRef.set(userAnime)
                                        .addOnSuccessListener(success-> Toast.makeText(CONTEXT,
                                                "Successfully added", Toast.LENGTH_SHORT).show())

                                        .addOnFailureListener(fail-> Toast.makeText(CONTEXT,
                                                fail.getMessage(), Toast.LENGTH_SHORT).show());
                            }
                            else{
                                userMoviesRef.set(userAnime)
                                        .addOnSuccessListener(success->{
                                            Toast.makeText(CONTEXT,
                                                    "Successfully added", Toast.LENGTH_SHORT).show();
                                            insertToWatch();
                                        })
                                        .addOnFailureListener(failure->{
                                            Toast.makeText(CONTEXT,
                                                    failure.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            }
                        }).addOnFailureListener(e -> Toast.makeText(CONTEXT, e.getMessage(), Toast.LENGTH_SHORT).show());

    }

}
