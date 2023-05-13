package com.example.animode;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DBPerson {
    private DatabaseReference dbr;

    public DBPerson(){
        FirebaseDatabase fireDB = FirebaseDatabase.getInstance();
        dbr = fireDB.getReference(Person.class.getSimpleName());
    }
    public Task<Void>add(Person person){
        return dbr.push().setValue(person);
    }
}
