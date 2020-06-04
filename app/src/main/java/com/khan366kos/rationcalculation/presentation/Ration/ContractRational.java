package com.khan366kos.rationcalculation.presentation.Ration;

import com.khan366kos.rationcalculation.Model.Product;

import java.util.List;

public interface ContractRational {
    interface RationView {
        void showComponent(List<Product> components);
        void notifyCursorAdapter(List<Product> components);
    }

    interface RationPresenter {
        void onBindViewHolder();

        void onQueryTextChangeProduct(String s, List<Product> components);

        void onQueryTextChangeDish(String newText, List<Product> components);
    }
}
