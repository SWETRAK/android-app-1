package com.example.firstapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.firstapp.databinding.FragmentFirstBinding;

import com.example.firstapp.MainActivity;

import java.sql.SQLOutput;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private double mean = 0.0;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navigation = NavHostFragment.findNavController(FirstFragment.this);

        MutableLiveData<Double> liveData = navigation.getCurrentBackStackEntry()
                        .getSavedStateHandle().getLiveData("mean", 0D);

        liveData.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double mean) {
                FirstFragment.this.mean = mean;

                if(mean == 0.0) {
                    binding.calculateGradesMeanButton.setText(R.string.srednia);
                } else if (mean < 3.0) {
                    binding.meanTextBox.setText(String.format(getString(R.string.twoja_srednia), mean));
                    binding.calculateGradesMeanButton.setText(R.string.nie_udalo_sie);
                } else {
                    binding.meanTextBox.setText(String.format(getString(R.string.twoja_srednia), mean));
                    binding.calculateGradesMeanButton.setText(R.string.gratulacje);
                }
            }
        });

        binding.calculateGradesMeanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean available = true;
                if(FirstFragment.this.mean > 0.0 && binding.nameTextInput.getText().toString().isEmpty()){
                    binding.nameTextInput.setError("Imie jest obowiązkowe");
                    available = false;
                }

                if(FirstFragment.this.mean > 0.0 && binding.surnameTextInput.getText().toString().isEmpty()) {
                    binding.surnameTextInput.setError("Nazwisko jest obowiązkowe");
                    available = false;
                }

                if(FirstFragment.this.mean > 0.0 && binding.gradesTextInput.getText().toString().isEmpty()){
                    binding.gradesTextInput.setError("Ilość ocen jest wymagana");
                    available = false;
                }

                if(available) {
                    if(FirstFragment.this.mean == 0.0 && binding.gradesTextInput.getError() == null){
                        Bundle bundle = new Bundle();
                        String text = binding.gradesTextInput.getText().toString();
                        if(!text.isEmpty()){
                            int gradesNumber = Integer.parseInt(text);
                            bundle.putInt("gradesNumber", gradesNumber);
                            navigation.navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);
                        }
                    } else if(FirstFragment.this.mean < 3.0 && FirstFragment.this.mean >= 2.0) {
                        Toast.makeText(FirstFragment.this.getActivity(), "Niestety nie udało się", Toast.LENGTH_SHORT).show();
                        FirstFragment.this.getActivity().finishAffinity();
                    } else if (FirstFragment.this.mean >= 3.0) {
                        Toast.makeText(FirstFragment.this.getActivity(), "Gratulacje!!!", Toast.LENGTH_SHORT).show();
                        FirstFragment.this.getActivity().finishAffinity();
                    }
                }
            }
        });

        binding.gradesTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                if(!text.isEmpty()){
                    int gradesNumber = Integer.parseInt(text);

                    if (gradesNumber < 5 || gradesNumber > 15) {
                        binding.gradesTextInput.setError("Wartość z poza zakresu 5-15");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        binding.nameTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                if(text.isEmpty()){
                    binding.nameTextInput.setError("Imie jest obowiązkowe");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if(text.isEmpty()){
                    binding.nameTextInput.setError("Imie jest obowiązkowe");
                }
            }
        });

        binding.surnameTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                if(text.isEmpty()){
                    binding.surnameTextInput.setError("Nazwisko jest obowiązkowe");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}