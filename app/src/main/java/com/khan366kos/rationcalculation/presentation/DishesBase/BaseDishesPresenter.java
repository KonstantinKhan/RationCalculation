package com.khan366kos.rationcalculation.presentation.DishesBase;

import com.khan366kos.rationcalculation.getway.DataProvider;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.khan366kos.rationcalculation.presentation.DishesBase.ContractBaseDishes.*;

public class BaseDishesPresenter implements ContractBaseDishes.BaseDishesPresenter {

    private BaseDishesView baseDishesView;
    private DataProvider dataProvider;

    public BaseDishesPresenter(BaseDishesView baseDishesView) {
        this.baseDishesView = baseDishesView;
        dataProvider = new DataProvider();
    }

    public void onBindViewHolder() {
        dataProvider.getAllDishes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dishes -> baseDishesView.showDishes(dishes));
    }

    @Override
    public void onClickBtnDeleteDish(String name) {
        dataProvider.deleteDish(name)
                .subscribeOn(Schedulers.newThread())
                .subscribe();
    }
}
