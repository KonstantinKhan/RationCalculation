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


    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        this.menu = menu;
        svComponent = (SearchView) menu.findItem(R.id.mi_sv_component).getActionView();
        super.onPrepareOptionsMenu(menu);
    }

    /*public void setComponentAdapter(DishAdapter.OnMove onMove) {
        this.componentAdapter = new DishAdapter(onMove);
    }*/

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
