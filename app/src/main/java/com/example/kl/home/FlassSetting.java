package com.example.kl.home;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FlassSetting {
    final String TAG = "FlassSetting";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //ip address
    static String ip="140.136.7.27";

    void setIp(){
        DocumentReference docRef = db.collection("System").document("system");
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                   this.ip = document.getString("ip");
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }


    static String getIp() {
        return ip;
    }



}
