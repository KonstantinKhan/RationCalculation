package com.khan366kos.rationcalculation.presentation.Dish;

import android.database.Cursor;

import com.khan366kos.rationcalculation.Model.Product;

import java.util.List;

public interface ContractDishFragment {

    interface DishView {
        void notifyCursorAdapter(List<Product> products);
    }

    interface DishPresenter {

        void onQueryTextChange(String s, List<Product> itemCount);

    }
}
