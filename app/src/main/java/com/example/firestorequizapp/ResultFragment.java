package com.example.firestorequizapp;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ResultFragment extends Fragment {

    private int rightAns, wrongAns, missedAns, result;
    private TextView resultCorrectAns, resultWrongAns, resultMissedAns, resultPercent;
    private ProgressBar progressBar;
    private Button homeBtn;

    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rightAns = ResultFragmentArgs.fromBundle(getArguments()).getRightAnswer();
        wrongAns = ResultFragmentArgs.fromBundle(getArguments()).getWrongAnswer();
        missedAns = ResultFragmentArgs.fromBundle(getArguments()).getMissedAnswer();
        result = ResultFragmentArgs.fromBundle(getArguments()).getResult();
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        resultCorrectAns = view.findViewById(R.id.result_correct_ans_num);
        resultWrongAns = view.findViewById(R.id.result_wrong_ans_num);
        resultMissedAns = view.findViewById(R.id.result_missed_num);
        resultPercent = view.findViewById(R.id.result_precent);
        progressBar = view.findViewById(R.id.result_progress);
        homeBtn = view.findViewById(R.id.result_home_btn);

        navController = Navigation.findNavController(view);

        resultCorrectAns.setText(rightAns+"");
        resultWrongAns.setText(wrongAns+"");
        resultMissedAns.setText(missedAns+"");
        resultPercent.setText(result+"%");

        progressBar.setProgress(result,true);
        progressBar.setVisibility(View.VISIBLE);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_resultFragment_to_startFragment);
            }
        });

    }
}