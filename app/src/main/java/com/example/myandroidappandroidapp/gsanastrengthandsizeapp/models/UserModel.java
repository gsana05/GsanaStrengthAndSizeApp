package com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class UserModel {

    public void signUp(String email, String password, final DataModelResult<Boolean> callback){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    callback.onComplete(true, null);
                }
                else {
                    callback.onComplete(false, task.getException());
                }

            }
        });
    }
}
