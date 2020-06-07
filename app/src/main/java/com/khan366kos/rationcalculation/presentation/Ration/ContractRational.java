package com.khan366kos.rationcalculation.presentation.Ration;

import com.khan366kos.rationcalculation.Model.Product;
import com.khan366kos.rationcalculation.Model.Ration;

import java.util.List;

public interface ContractRational {
    interface RationView {
        void showComponent(List<Product> components);
        void notifyCursorAdapter(List<Product> components);
        void setRation(Ration ration);
    }

    interface RationPresenter {
        void onBindViewHolder();

        void onQueryTextChangeProduct(String s, List<Product> components);

        void onQueryTextChangeDish(String newText, List<Product> components);

        void onShowRation(String date);

        void onSuggestionClick(Ration ration);

        void onClickBtnDelete(String date);
    }
}
