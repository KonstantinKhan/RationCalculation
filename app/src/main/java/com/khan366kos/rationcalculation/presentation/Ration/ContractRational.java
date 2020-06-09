package com.khan366kos.rationcalculation.presentation.Ration;

import com.khan366kos.rationcalculation.Model.Dish;
import com.khan366kos.rationcalculation.Model.Product;
import com.khan366kos.rationcalculation.Model.Ration;

import java.util.List;

public interface ContractRational {
    interface RationView {
        void showComponent(List<Dish> components);

        void notifyCursorAdapter(List<Product> components);

        void setRation(Ration ration);

        void editDish(Dish dish);

        void addDish(Dish dish);
    }

    interface RationPresenter {
        void onBindViewHolder();

        void onQueryTextChangeProduct(String s, List<Product> components);

        void onQueryTextChangeDish(String newText, List<Product> components);

        void onShowRation(String date);

        void onUpdateRation(Ration ration);

        void onClickBtnDelete(String date);

        void onClickBtnEdit(String name);

        void onSuggestionClick(String name);
    }
}
