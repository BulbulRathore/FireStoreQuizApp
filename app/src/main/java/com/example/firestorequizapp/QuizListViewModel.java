package com.example.firestorequizapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class QuizListViewModel extends ViewModel implements FirebaseRepository.OnFireStoreTaskComplete {

    private MutableLiveData<List<ListQuizModel>> quizLiveData = new MutableLiveData<>();

    private FirebaseRepository firebaseRepository = new FirebaseRepository(this);

    public LiveData<List<ListQuizModel>> getQuizListLiveData(){
        return quizLiveData;
    }

    public QuizListViewModel(){
        firebaseRepository.getQuizData();
    }

    @Override
    public void quizListDataAdded(List<ListQuizModel> listQuizModels) {
        quizLiveData.setValue(listQuizModels);
    }

    @Override
    public void onError(Exception e) {

    }
}
