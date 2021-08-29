package com.oyc0401.pubuk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.oyc0401.pubuk.ScrollingActivity111;
import com.oyc0401.pubuk.ViewModel;
import com.oyc0401.pubuk.databinding.FragmentDashboardBinding;

import java.util.Objects;

public class DashboardFragment extends Fragment {


    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        ViewModel model = new ViewModelProvider(requireActivity()).get(ViewModel.class);
        //model.setTextTable("431dadsadasd343124sd");


        binding.btnUniv.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), ScrollingActivity111.class);
            startActivity(intent);
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}