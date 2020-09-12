package com.example.firestorequizapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class DetailsFragment extends Fragment implements View.OnClickListener {

    private QuizListViewModel quizListViewModel;
    private int position,totalQues;
    private String documentId;

    private ImageView detailImage;
    private TextView detailTitle;
    private TextView detailDesc;
    private TextView detailDiff;
    private TextView detailTotalQues;
    private TextView detailLastScore;
    private TextView detailDiffNum;
    private TextView detailTotalQuesNum;
    private TextView detailLastScoreNum;
    private Button detailStartQuiz;

    private NavController navController;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        position = DetailsFragmentArgs.fromBundle(getArguments()).getPosition();
        Log.i("pos",position+"");
        return inflater.inflate(R.layout.fragment_details, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        detailImage = view.findViewById(R.id.detail_image);
        detailTitle = view.findViewById(R.id.detail_title);
        detailDesc = view.findViewById(R.id.detail_desc);
        detailDiff = view.findViewById(R.id.detail_diff);
        detailTotalQues = view.findViewById(R.id.detail_total_qes);
        detailLastScore = view.findViewById(R.id.detail_last_score);
        detailDiffNum = view.findViewById(R.id.detail_diff_num);
        detailTotalQuesNum = view.findViewById(R.id.detail_total_que_num);
        detailLastScoreNum = view.findViewById(R.id.detail_last_score_num);
        detailStartQuiz = view.findViewById(R.id.detail_start_quiz);

        detailStartQuiz.setOnClickListener(this);
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        quizListViewModel = new ViewModelProvider(getActivity()).get(QuizListViewModel.class);
        quizListViewModel.getQuizListLiveData().observe(getViewLifecycleOwner(), new Observer<List<ListQuizModel>>() {
            @Override
            public void onChanged(List<ListQuizModel> listQuizModels) {
                Glide.with(getContext()).load(listQuizModels.get(position).getImage()).into(detailImage);
                detailTitle.setText(listQuizModels.get(position).getName());
                detailDesc.setText(listQuizModels.get(position).getDesc());
                detailDiffNum.setText(listQuizModels.get(position).getLevel());
                detailTotalQuesNum.setText(listQuizModels.get(position).getQuestions()+"");
                documentId = listQuizModels.get(position).getQuiz_id();
                totalQues = (int)listQuizModels.get(position).getQuestions();
            }
        });
    }

    @Override
    public void onClick(View view) {
       DetailsFragmentDirections.ActionDetailsFragmentToQuizFragment action = DetailsFragmentDirections.actionDetailsFragmentToQuizFragment();
       action.setPosition(position);
       action.setDocumentId(documentId);
       action.setTotalQuestions(totalQues);

       navController.navigate(action);
    }
}