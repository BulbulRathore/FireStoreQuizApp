package com.example.firestorequizapp;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class FirebaseRepository {

    private OnFireStoreTaskComplete onFireStoreTaskComplete;

    public FirebaseRepository(OnFireStoreTaskComplete onFireStoreTaskComplete){
        this.onFireStoreTaskComplete = onFireStoreTaskComplete;
    }

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference quizRef = firebaseFirestore.collection("Quiz");

    public void getQuizData(){
        quizRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    onFireStoreTaskComplete.quizListDataAdded(task.getResult().toObjects(ListQuizModel.class));
                } else{
                    onFireStoreTaskComplete.onError(task.getException());
                }
            }
        });
    }

    public interface OnFireStoreTaskComplete{
        void quizListDataAdded(List<ListQuizModel> listQuizModels);
        void onError(Exception e);
    }
}
