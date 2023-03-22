package com.example.firstapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstapp.databinding.FragmentSecondBinding;

import java.util.ArrayList;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    private Integer gradeNumber;
    private ArrayList<Grade> grades = new ArrayList<>();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        assert getArguments() != null;
        gradeNumber = getArguments().getInt("gradesNumber", 0);

        super.onCreate(savedInstanceState);

        String[] gradeNames = getResources().getStringArray(R.array.gradeTypes);
        for(int i = 0; i < gradeNumber; i++){
            grades.add(new Grade(gradeNames[i], 2));
        }

        GradeAdapter gradesAdapter = new GradeAdapter(this.getActivity(), grades);

        RecyclerView gradeList = binding.gradesListContainer;
        gradeList.setAdapter(gradesAdapter);
        gradeList.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                double mean = 0;
                for (Grade g : grades) {
                    mean += g.getValue();
                }
                mean /= grades.size();
                NavController navigation = NavHostFragment.findNavController(SecondFragment.this);
                navigation.getPreviousBackStackEntry().getSavedStateHandle().set("mean", mean);
                navigation.popBackStack();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}