package com.khan366kos.rationcalculation.presentation.Dish;

import com.khan366kos.rationcalculation.Model.Dish;
import com.khan366kos.rationcalculation.Model.Product;

import java.util.List;

public interface ContractDishFragment {

    interface DishView {
        void notifyCursorAdapter(List<Product> products);

        Dish saveDish();

        void showErrorDuplicate();

        void setCreateDish(boolean b);
    }

    interface DishPresenter {

        void onQueryTextChange(String s, List<Product> itemCount);

        void onSaveDish();

        void onUpdateDish(Dish dish);
    }
}
