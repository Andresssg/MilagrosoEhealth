package com.milagroso.database;


import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class Firebase {
    private FirebaseFirestore db;

    public Firebase() {
        db = FirebaseFirestore.getInstance();
    }

    public void getPacientes(String email) {
        db.collection("Pacientes")
                .whereEqualTo("email", email)
                //.orderBy("nombres", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() != 0) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //Devuelve toda la informacion
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    //Devuelve solo un campo
                                    Log.d(TAG, document.getId() + " => " + document.get("email"));
                                    //TODO: Validar email and password
                                }
                            }else{
                                System.out.println("No hay datos");
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

}
