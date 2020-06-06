package com.khan366kos.rationcalculation.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.khan366kos.rationcalculation.R;

public class FragmentAddFirstProduct extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_first_product,
                container, false);
        ImageButton btnAddFirstButton = view.findViewById(R.id.btn_add_first_product);

        switch (((ViewGroup) container.getParent()).getId()) {
            case R.id.rl_fragment_ration:
                btnAddFirstButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_ration_to_base_products));
                break;
            case R.id.rl_fragment_dishes:
                btnAddFirstButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_dish_to_base_products));
                break;
        }
        return view;
    }
}
