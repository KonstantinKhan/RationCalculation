package com.khan366kos.rationcalculation.presentation.Ration;

import com.khan366kos.rationcalculation.getway.DataProvider;

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
}
