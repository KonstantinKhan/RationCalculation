package com.khan366kos.rationcalculation.presentation.Dish;

import com.khan366kos.rationcalculation.Model.Product;
import com.khan366kos.rationcalculation.getway.DataProvider;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.khan366kos.rationcalculation.presentation.Dish.ContractDishFragment.*;

public class DishPresenter implements ContractDishFragment.DishPresenter {

    private DishView dishView;
    private DataProvider dataProvider;

    public DishPresenter(DishView dishView) {
        this.dishView = dishView;
        dataProvider = new DataProvider();
    }

    @Override
    public void onQueryTextChange(String string, List<Product> items) {
        dataProvider.getCursor(string, items)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(products -> dishView.notifyCursorAdapter(products));
    }
}
