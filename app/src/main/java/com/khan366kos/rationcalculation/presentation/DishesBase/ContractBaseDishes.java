package com.khan366kos.rationcalculation.presentation.DishesBase;

import com.khan366kos.rationcalculation.Model.Dish;

import java.util.List;

public interface ContractBaseDishes {

    interface BaseDishesView {
        void showDishes(List<Dish> dishes);
    }

    interface BaseDishesPresenter {
        void onBindViewHolder();

        void onClickBtnDeleteDish(String name);
    }
}
