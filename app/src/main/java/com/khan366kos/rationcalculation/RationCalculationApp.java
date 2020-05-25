package com.khan366kos.rationcalculation;

import android.app.Application;

import com.khan366kos.rationcalculation.di.components.AppComponent;
import com.khan366kos.rationcalculation.di.components.DaggerAppComponent;
import com.khan366kos.rationcalculation.di.module.AppModule;

public class RationCalculationApp extends Application {

    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public static AppComponent getComponent() {
        return component;
    }
}
