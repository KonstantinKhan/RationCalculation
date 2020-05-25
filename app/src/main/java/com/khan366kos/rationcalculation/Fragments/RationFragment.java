package com.khan366kos.rationcalculation.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.khan366kos.rationcalculation.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.khan366kos.rationcalculation.ProductContract.ProductEntry.TAG;

public class RationFragment extends TemplateFragment {

    private BottomNavigationView bnv;
    //private TextView tvHeading;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //Log.d(TAG, "RationFragment.onCreate");
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ration, container, false);

        //tvHeading = getActivity().findViewById(R.id.tv_heading);
        bnv = view.findViewById(R.id.bottom_menu);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //tvHeading.setText(currentDate());

        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.pick_product:
                        menuItem.setChecked(true);
                        Log.d(TAG, "Выбираем продукт");
                        getMenu().findItem(R.id.mi_sv_component).expandActionView();
                        getSvComponent().setQueryHint("Выберите продукт");
                        return true;
                    case R.id.clean_dish:
                        Log.d(TAG, "Очищаем рацион");
                        return true;
                    case R.id.pick_dish:
                        Log.d(TAG, "Выбираем блюдо");
                        getMenu().findItem(R.id.mi_sv_component).expandActionView();
                        getSvComponent().setQueryHint("Выберите блюдо");
                        return true;
                }
                return false;
            }
        });
    }

    // Метод для получеия текущей даты в виде строики в заданном формате "02 янв. 2020 г."
    private String currentDate() {
        Date date = new Date();
        Locale locale = new Locale("ru", "RU");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy", locale);
        return simpleDateFormat.format(date) + " г." + " (Сегодня) ";
    }
}
