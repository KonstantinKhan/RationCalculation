package com.khan366kos.rationcalculation.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.khan366kos.rationcalculation.presentation.Dish.DishAdapter;
import com.khan366kos.rationcalculation.R;

public class TemplateFragment extends Fragment {

    private DishAdapter componentAdapter;

    private SearchView svComponent;
    private Menu menu;
    private TextView tvHeading;
    private EditText etDishName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Позволяем фрагменту работать с меню.
        setHasOptionsMenu(true);

        tvHeading = getActivity().findViewById(R.id.tv_heading);
        etDishName = getActivity().findViewById(R.id.et_dish_name);

        tvHeading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvHeading.getText().toString().equals(getString(R.string.dish_name))) {
                    editDishName(true);
                } else if (tvHeading.getText().toString().equals(etDishName.getText().toString())) {
                    editDishName(false);
                }
            }
        });

        etDishName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    etDishName.setVisibility(View.GONE);
                    tvHeading.setVisibility(View.VISIBLE);
                }
            }
        });

        etDishName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                // Запрещаем первым символом вводить пробел.
                if (charSequence.length() > 0) {
                    if (charSequence.charAt(0) == ' ') {
                        etDishName.setText(charSequence.subSequence(1, charSequence.length()));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        this.menu = menu;
        svComponent = (SearchView) menu.findItem(R.id.mi_sv_component).getActionView();
        super.onPrepareOptionsMenu(menu);
    }

    public void setComponentAdapter(DishAdapter.OnMove onMove) {
        this.componentAdapter = new DishAdapter(onMove);
    }

    public DishAdapter getComponentAdapter() {
        return componentAdapter;
    }

    public SearchView getSvComponent() {
        return svComponent;
    }

    public Menu getMenu() {
        return menu;
    }

    public EditText getEtDishName() {
        return etDishName;
    }

    public TextView getTvHeading() {
        return tvHeading;
    }

    // Метод для регулирования отображения полей, отвечающих за ввод наименования блюда
    // и его отображение в зависимости от значения переданного параметра.
    public void editDishName(boolean edit) {
        if (edit) {
            tvHeading.setVisibility(View.GONE);
            etDishName.setVisibility(View.VISIBLE);
            //etDishName.setFocusAndToggleSoftKey();
        } else {
            tvHeading.setVisibility(View.VISIBLE);
            etDishName.setVisibility(View.GONE);
        }
    }
}
