package com.khan366kos.rationcalculation.presentation.DishesBase;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khan366kos.rationcalculation.Model.Dish;
import com.khan366kos.rationcalculation.R;
import com.khan366kos.rationcalculation.presentation.Dish.DishFragment;

import java.util.List;

import static com.khan366kos.rationcalculation.Data.ProductContract.ProductEntry.TAG;

public class BaseDishesFragment extends Fragment implements ContractBaseDishes.BaseDishesView {

    private RecyclerView recyclerView;
    private BaseDishesPresenter presenter;
    private BaseDishesAdapter adapter;
    private Fragment thisFragment;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        presenter = new BaseDishesPresenter(this);
        presenter.onBindViewHolder();
        thisFragment = this;
        adapter = new BaseDishesAdapter(new BaseDishesAdapter.OnMove() {
            @Override
            public void onClickBtnDeleteDish(String name) {
                presenter.onClickBtnDeleteDish(name);
            }

            @Override
            public void onClickBtnEditDish(Dish dish) {
                Fragment fragment = new DishFragment(dish, new DishFragment.OnUpdateBaseDishes() {
                    @Override
                    public void onUpdateBaseDishes() {
                        FragmentManager fm = getParentFragmentManager();
                        fm.beginTransaction()
                                .replace(R.id.fragment, thisFragment)
                                .commit();
                    }
                });
                FragmentManager fm = getParentFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.fragment, fragment)
                        .commit();
            }
        });
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
