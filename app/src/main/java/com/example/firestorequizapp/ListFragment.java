package com.example.firestorequizapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import java.util.List;

public class ListFragment extends Fragment implements ListAdapter.OnItemClickedInList {

    private RecyclerView listRecyclerView;
    private QuizListViewModel quizListViewModel;
    private ListAdapter adapter;

    private ProgressBar listProgress;
    private Animation fadeIn, fadeOut;

    private NavController navController;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listRecyclerView = view.findViewById(R.id.list_recycler_view);
        adapter = new ListAdapter(this);
        listProgress = view.findViewById(R.id.list_progress);
        fadeIn = AnimationUtils.loadAnimation(getContext(),R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(getContext(),R.anim.fade_out);

        listRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listRecyclerView.setHasFixedSize(true);
        listRecyclerView.setAdapter(adapter);

        navController = Navigation.findNavController(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        quizListViewModel = new ViewModelProvider(getActivity()).get(QuizListViewModel.class);
        quizListViewModel.getQuizListLiveData().observe(getViewLifecycleOwner(), new Observer<List<ListQuizModel>>() {
            @Override
            public void onChanged(List<ListQuizModel> listQuizModels) {
                adapter.setListQuizModels(listQuizModels);
                listRecyclerView.setAnimation(fadeIn);
                listProgress.setAnimation(fadeOut);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void getPositionOfList(int position) {
        ListFragmentDirections.ActionListFragmentToDetailsFragment action = ListFragmentDirections.actionListFragmentToDetailsFragment();
        action.setPosition(position);
        navController.navigate(action);
    }
}