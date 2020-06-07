package com.khan366kos.rationcalculation.presentation.Ration;

import android.util.Log;

import com.khan366kos.rationcalculation.Model.Product;
import com.khan366kos.rationcalculation.Model.Ration;
import com.khan366kos.rationcalculation.getway.DataProvider;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.khan366kos.rationcalculation.Data.ProductContract.ProductEntry.TAG;
import static com.khan366kos.rationcalculation.presentation.Ration.ContractRational.*;

public class RationPresenter implements ContractRational.RationPresenter {

    private RationView view;
    private DataProvider dataProvider;

    public RationPresenter(RationView view) {
        this.view = view;
        dataProvider = new DataProvider();
    }

    @Override
    public void onBindViewHolder() {

    }

    @Override
    public void onQueryTextChangeProduct(String s, List<Product> components) {
        dataProvider.getCursorProduct(s, components)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(products -> view.notifyCursorAdapter(products));
    }

    @Override
    public void onQueryTextChangeDish(String s, List<Product> components) {
        dataProvider.getCursorDish(s, components)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(products -> view.notifyCursorAdapter(products));
    }

    @Override
    public void onShowRation(String date) {
        dataProvider.getQueryRation(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ration -> {
                            Log.d(TAG, "onShowRation: ok");
                            view.setRation(ration);
                        },
                        throwable -> {
                            Log.d(TAG, "onShowRation: throwable");
                            view.setRation(new Ration());
                        });
    }

    @Override
    public void onSuggestionClick(Ration ration) {
        dataProvider.saveRation(ration)
                .subscribeOn(Schedulers.newThread())
                .subscribe();
    }

    @Override
    public void onClickBtnDelete(String date) {

    }
}
