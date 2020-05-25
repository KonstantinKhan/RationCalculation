package com.khan366kos.rationcalculation.di.module;

import android.content.Context;

import com.khan366kos.rationcalculation.Data.ProductDbHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return this.context;
    }

    @Provides
    @Singleton
    ProductDbHelper productDbHelper() {
        return ProductDbHelper.getInstance(context);
    }

    public Context getContext() {
        return context;
    }
}
