package com.khan366kos.rationcalculation.di.components;

import com.khan366kos.rationcalculation.di.module.AppModule;
import com.khan366kos.rationcalculation.getway.DataProvider;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {
    void injectDataProvider(DataProvider provider);
}
