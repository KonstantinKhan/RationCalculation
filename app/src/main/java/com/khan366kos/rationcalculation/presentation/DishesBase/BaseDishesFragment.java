package com.khan366kos.rationcalculation.presentation.DishesBase;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khan366kos.rationcalculation.Model.Dish;
import com.khan366kos.rationcalculation.R;

import java.util.List;

public class BaseDishesFragment extends Fragment implements ContractBaseDishes.BaseDishesView {

    private RecyclerView recyclerView;
    private BaseDishesPresenter presenter;
    private BaseDishesAdapter adapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        presenter = new BaseDishesPresenter(this);
        presenter.onBindViewHolder();
        adapter = new BaseDishesAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_dishes, container, false);
        recyclerView = view.findViewById(R.id.rv_dishes_in_database);
        recyclerView.setAnimation(null); // отключаем анимацию нажаний на item.
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(null);
        presenter.onBindViewHolder();
    }

    @Override
    public void showDishes(List<Dish> dishes) {
        adapter.setItems(dishes);
    }
}
