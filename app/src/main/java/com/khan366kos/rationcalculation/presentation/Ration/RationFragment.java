package com.khan366kos.rationcalculation.presentation.Ration;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.khan366kos.rationcalculation.Fragments.TemplateFragment;
import com.khan366kos.rationcalculation.Model.Product;
import com.khan366kos.rationcalculation.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.khan366kos.rationcalculation.ProductContract.ProductEntry.TAG;

public class RationFragment extends TemplateFragment implements ContractRational.RationView {

    private BottomNavigationView bnv;
    private TextView tvHeading;
    private RecyclerView recyclerView;
    private RationAdapter adapter;
    private RationPresenter presenter;
    private SearchView svComponent;
    private Menu menu;
    private MenuItem menuItemSvComponent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        adapter = new RationAdapter();
        presenter = new RationPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ration, container, false);
        init(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        tvHeading.setText(currentDate());

        bnv.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.pick_product:
                    menuItem.setChecked(true);
                    getMenu().findItem(R.id.mi_sv_component).expandActionView();
                    getSvComponent().setQueryHint("Выберите продукт");
                    return true;
                case R.id.clean_dish:
                    return true;
                case R.id.pick_dish:
                    getMenu().findItem(R.id.mi_sv_component).expandActionView();
                    getSvComponent().setQueryHint("Выберите блюдо");
                    return true;
            }
            return false;
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(null);

        presenter.onBindViewHolder();
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);

        this.menu = menu;

        menuItemSvComponent = menu.findItem(R.id.mi_sv_component);
        svComponent = (SearchView) menuItemSvComponent.getActionView();

        // Устанавливаем вспомогательную надпись в поле поиска.
        svComponent.setQueryHint("Выберите продукт");
    }

    private void init(View view) {
        tvHeading = getActivity().findViewById(R.id.tv_heading);
        bnv = view.findViewById(R.id.bottom_menu);
        recyclerView = view.findViewById(R.id.rl_ration_component);
    }

    // Метод для получеия текущей даты в виде строики в заданном формате "02 янв. 2020 г."
    private String currentDate() {
        Date date = new Date();
        Locale locale = new Locale("ru", "RU");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy", locale);
        return simpleDateFormat.format(date) + " г." + " (Сегодня) ";
    }

    @Override
    public void showComponent(List<Product> components) {
        adapter.setComponents(components);
    }
}
