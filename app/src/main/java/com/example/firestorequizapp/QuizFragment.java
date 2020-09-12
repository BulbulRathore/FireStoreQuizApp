package com.example.firestorequizapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.DistributionOrBuilder;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class QuizFragment extends Fragment implements View.OnClickListener {

    private FirebaseFirestore firebaseFirestore;
    private CollectionReference questionsRef;
    private List<QuestionsModel> questionsList = new ArrayList<>();
    private List<QuestionsModel> questionRandomData = new ArrayList<>();

    private NavController navController;

    private int position;
    private String documentId;
    private int totalQuestions = 10;

    private TextView quizCrossBtn;
    private TextView quizLoadingTxt;
    private TextView quizQuesNumTxt;
    private TextView quizProgressNum;
    private TextView quizFetchDataTXt;
    private TextView quizVerifyTxt;

    private Button quizOptionOneBtn;
    private Button quizOptionTwoBtn;
    private Button quizOptionThreeBtn;

    private Button quizNextBtn;
    private ProgressBar quizProgress;
    private CountDownTimer downTimer;

    private int questionNum = 0;
    private int rightQues = 0;
    private int wrongQues = 0;
    private int missedQues = 0;

    public QuizFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        position = QuizFragmentArgs.fromBundle(getArguments()).getPosition();
        documentId = QuizFragmentArgs.fromBundle(getArguments()).getDocumentId();
        totalQuestions = QuizFragmentArgs.fromBundle(getArguments()).getTotalQuestions();
        Log.i("documentId",documentId);
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseFirestore = FirebaseFirestore.getInstance();

        quizCrossBtn = view.findViewById(R.id.quiz_cross_btn);
        quizLoadingTxt = view.findViewById(R.id.quiz_loading_txt);
        quizQuesNumTxt = view.findViewById(R.id.quiz_qes_num_txt);
        quizProgressNum = view.findViewById(R.id.quiz_progress_num);
        quizFetchDataTXt = view.findViewById(R.id.quiz_fetch_data_txt);
        quizVerifyTxt = view.findViewById(R.id.quiz_verify_txt);

        quizOptionOneBtn = view.findViewById(R.id.quiz_option_one_btn);
        quizOptionTwoBtn = view.findViewById(R.id.quiz_option_two_btn);
        quizOptionThreeBtn = view.findViewById(R.id.quiz_option_three_btn);

        quizNextBtn = view.findViewById(R.id.quiz_next_btn);
        quizProgress = view.findViewById(R.id.quiz_progress);

        quizOptionOneBtn.setOnClickListener(this);
        quizOptionTwoBtn.setOnClickListener(this);
        quizOptionThreeBtn.setOnClickListener(this);

        navController = Navigation.findNavController(view);

        questionsRef = firebaseFirestore.collection("Quiz").document(documentId).collection("Questions");
        questionsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    questionsList = task.getResult().toObjects(QuestionsModel.class);

                    int max = 20;
                    for(int i=0;i<10;i++){
                        int pos = getRandomNumber(max);
                        QuestionsModel model = questionsList.get(pos);
                        questionRandomData.add(model);
                        Log.i("question",questionRandomData.get(i).getQuestion());
                        questionsList.remove(pos);
                        max--;
                    }

                    loadUI();
                }
             }
        });
    }

    private void loadUI() {
        quizLoadingTxt.setText("Quiz is Loaded");
        quizQuesNumTxt.setText("1");
        quizFetchDataTXt.setText("First question is loaded");

        //enable the option button
        enableOptionBtn();

        //load questions
        loadQuestion(questionNum);
    }

    private void loadQuestion(int quesNum) {
        position = quesNum;
        quizQuesNumTxt.setText(quesNum + 1 +"");
        //load question
        quizFetchDataTXt.setText(questionRandomData.get(quesNum).getQuestion());

        //load options
        quizOptionOneBtn.setText(questionRandomData.get(quesNum).getOption_a());
        quizOptionTwoBtn.setText(questionRandomData.get(quesNum).getOption_b());
        quizOptionThreeBtn.setText(questionRandomData.get(quesNum).getOption_c());

        startTimer(quesNum);
    }

    private void startTimer(int quesNum) {
        final long timer = questionRandomData.get(quesNum).getTimer();
        quizProgress.setVisibility(View.VISIBLE);
        downTimer = new CountDownTimer(timer*1000,10) {
            @Override
            public void onTick(long l) {
                quizProgressNum.setText(l/1000+"");
                Long percent = l /(timer * 10);
                quizProgress.setProgress(percent.intValue());
            }

            @Override
            public void onFinish() {
                missedQuestion();
            }
        };
        downTimer.start();
    }

    private void missedQuestion() {
        Log.i("missed","question missed");
        missedQues++;
        if(questionNum < totalQuestions-1) {
            questionNum++;
            downTimer.cancel();
            loadUI();
        } else{
            quizOver();
        }
    }

    private void enableOptionBtn() {
        quizOptionOneBtn.setVisibility(View.VISIBLE);
        quizOptionTwoBtn.setVisibility(View.VISIBLE);
        quizOptionThreeBtn.setVisibility(View.VISIBLE);

        quizOptionOneBtn.setBackground(getResources().getDrawable(R.drawable.option_btn_bg,null));
        quizOptionTwoBtn.setBackground(getResources().getDrawable(R.drawable.option_btn_bg,null));
        quizOptionThreeBtn.setBackground(getResources().getDrawable(R.drawable.option_btn_bg,null));

        //enable the buttons
        quizOptionOneBtn.setEnabled(true);
        quizOptionTwoBtn.setEnabled(true);
        quizOptionThreeBtn.setEnabled(true);

        //hide feedback and next btn
        quizVerifyTxt.setVisibility(View.INVISIBLE);
        quizNextBtn.setVisibility(View.INVISIBLE);
        quizNextBtn.setEnabled(false);
    }

    int getRandomNumber(int max){
        int num = (int) (Math.random() * max);
        return num;
    }

    @Override
    public void onClick(View view) {
        String clickedAns;
        switch (view.getId()){
            case R.id.quiz_option_one_btn:
                clickedAns = questionRandomData.get(position).getOption_a();
                checkAnswer(clickedAns,1);
                break;
            case R.id.quiz_option_two_btn:
               clickedAns = questionRandomData.get(position).getOption_b();
                checkAnswer(clickedAns,2);
               break;
            case R.id.quiz_option_three_btn:
                clickedAns = questionRandomData.get(position).getOption_c();
                checkAnswer(clickedAns,3);
                break;

        }

    }

    private boolean checkAnswer(String optionValue,int btnPos){
        String ans = questionRandomData.get(position).getAnswer();
        Log.i("writeAns",ans);
        if(optionValue.equals(ans)){
            Log.i("answer","right");
            changeBtnColor(btnPos,true);
            return true;
        } else{
            Log.i("answer","wrong");
            changeBtnColor(btnPos,false);
            return false;
        }
    }

    private void changeBtnColor(int btnPos, boolean rigOrWrong){
        if(rigOrWrong){
            switch (btnPos){
                case 1:
                    quizOptionOneBtn.setBackground(getResources().getDrawable(R.drawable.detail_btn_bg,null));
                    quizOptionTwoBtn.setEnabled(false);
                    quizOptionThreeBtn.setEnabled(false);
                    break;
                case 2:
                    quizOptionTwoBtn.setBackground(getResources().getDrawable(R.drawable.detail_btn_bg,null));
                    quizOptionOneBtn.setEnabled(false);
                    quizOptionThreeBtn.setEnabled(false);
                    break;
                case 3:
                    quizOptionThreeBtn.setBackground(getResources().getDrawable(R.drawable.detail_btn_bg,null));
                    quizOptionOneBtn.setEnabled(false);
                    quizOptionThreeBtn.setEnabled(false);
                    break;

            }

            enableNextBtn(true);
        } else{
            switch (btnPos){
                case 1:
                    quizOptionOneBtn.setBackground(getResources().getDrawable(R.drawable.btn_wrong_bg,null));
                    quizOptionTwoBtn.setEnabled(false);
                    quizOptionThreeBtn.setEnabled(false);
                    break;
                case 2:
                    quizOptionTwoBtn.setBackground(getResources().getDrawable(R.drawable.btn_wrong_bg,null));
                    quizOptionOneBtn.setEnabled(false);
                    quizOptionThreeBtn.setEnabled(false);
                    break;
                case 3:
                    quizOptionThreeBtn.setBackground(getResources().getDrawable(R.drawable.btn_wrong_bg,null));
                    quizOptionTwoBtn.setEnabled(false);
                    quizOptionOneBtn.setEnabled(false);
                    break;
            }

            enableNextBtn(false);
        }

    }

    private void enableNextBtn(boolean check){
        if(check){
            quizVerifyTxt.setText("Right Answer");
            quizVerifyTxt.setTextColor(getResources().getColor(R.color.colorPrimary,null));
            rightQues++;
        } else{
            quizVerifyTxt.setText("Wrong Answer");
            quizVerifyTxt.setTextColor(getResources().getColor(R.color.colorAccent,null));
            wrongQues++;
        }
        quizVerifyTxt.setVisibility(View.VISIBLE);
        quizNextBtn.setEnabled(true);
        quizNextBtn.setVisibility(View.VISIBLE);

        quizNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(questionNum < totalQuestions-1) {
                    questionNum++;
                    downTimer.cancel();
                    loadUI();
                } else{
                   quizOver();
                }
            }
        });
    }

    private void quizOver(){
        Log.i("quizOver","quiz is over = " + rightQues + " " + wrongQues + " " + missedQues);
        //move to result fragment and show the result
        downTimer.cancel();
        QuizFragmentDirections.ActionQuizFragmentToResultFragment action = QuizFragmentDirections.actionQuizFragmentToResultFragment();
        action.setRightAnswer(rightQues);
        action.setWrongAnswer(wrongQues);
        action.setMissedAnswer(missedQues);
        action.setResult((rightQues*100)/totalQuestions);
        navController.navigate(action);
    }

}