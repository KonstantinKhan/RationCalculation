package com.khan366kos.rationcalculation.presentation.Ration;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
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
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.khan366kos.rationcalculation.Fragments.TemplateFragment;
import com.khan366kos.rationcalculation.Model.Product;
import com.khan366kos.rationcalculation.R;
import com.khan366kos.rationcalculation.Service.AppCursorAdapter.CursorAdapterFactory;
import com.khan366kos.rationcalculation.Service.AppCursorAdapter.CursorAdapterTypes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.khan366kos.rationcalculation.ProductContract.ProductEntry.*;

public class RationFragment extends TemplateFragment implements ContractRational.RationView {

    private BottomNavigationView bnv;
    private TextView tvHeading;
    private RecyclerView recyclerView;
    private RationAdapter adapter;
    private RationPresenter presenter;
    private SearchView svComponent;
    private Menu menu;
    private MenuItem menuItemSvComponent;
    private MatrixCursor cursor;
    private SimpleCursorAdapter simpleCursorAdapter;
    private int bnvMenu;
    private CursorAdapterFactory cursorAdapterFactory;
    private String[] columnName;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        cursorAdapterFactory = new CursorAdapterFactory();
    }

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
                    columnName = new String[]{_ID, COLUMN_PRODUCT_NAME, COLUMN_PRODUCT_CALORIES,
                            COLUMN_PRODUCT_PROTEINS, COLUMN_PRODUCT_FATS, COLUMN_PRODUCT_CARBOHYDRATES};
                   /* simpleCursorAdapter = cursorAdapterFactory.getCursorAdapter(getContext(),
                            CursorAdapterTypes.PRODUCTS);*/
                    bnvMenu = R.id.pick_product;
                    //menuItem.setChecked(true);
                    getMenu().findItem(R.id.mi_sv_component).expandActionView();
                    getSvComponent().setQueryHint("Выберите продукт");
                    return true;
                case R.id.clean_dish:
                    return true;
                case R.id.pick_dish:
                    columnName = new String[]{_ID, COLUMN_DISH_NAME, COLUMN_DISH_CALORIES,
                            COLUMN_DISH_PROTEINS, COLUMN_DISH_FATS, COLUMN_DISH_CARBOHYDRATES};
                    simpleCursorAdapter = cursorAdapterFactory.getCursorAdapter(getContext(),
                            CursorAdapterTypes.DISHES);
                    svComponent.setSuggestionsAdapter(simpleCursorAdapter);
                    svComponent.setSuggestionsAdapter(simpleCursorAdapter);
                    bnvMenu = R.id.pick_dish;
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

        simpleCursorAdapter = cursorAdapterFactory.getCursorAdapter(getContext(),
                CursorAdapterTypes.PRODUCTS);

        svComponent.setSuggestionsAdapter(simpleCursorAdapter);

        // Устанавливаем максимальную ширину поля отображения вариантов поиска.
        svComponent.setMaxWidth(Integer.MAX_VALUE);

        svComponent.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                switch (bnvMenu) {
                    case R.id.pick_product:
                        svComponent.post(() -> presenter
                                .onQueryTextChangeProduct(newText, adapter.getComponents()));
                        break;
                    case R.id.pick_dish:
                        svComponent.post(() -> presenter
                                .onQueryTextChangeDish(newText, adapter.getComponents()));
                        break;
                    default:
                }
                return false;
            }
        });

        svComponent.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {

                int i = adapter.getItemCount(); // Количество компонентов в блюде.

                Cursor cursor = svComponent.getSuggestionsAdapter().getCursor();
                cursor.moveToPosition(position);

                Product product = new Product(cursor.getString(1),
                        Double.parseDouble(cursor.getString(2).replace(",", ".")),
                        Double.parseDouble(cursor.getString(3).replace(",", ".")),
                        Double.parseDouble(cursor.getString(4).replace(",", ".")),
                        Double.parseDouble(cursor.getString(5).replace(",", ".")));

                // Добавляем продукт в рацион.
                adapter.getComponents().add(product);

                // Прокручивает RecyclerView до добавленного компонента.
                recyclerView.smoothScrollToPosition(adapter.getComponents().size() - 1);

                // Обнуляем строку запроса.
                svComponent.setQuery("", false);

                adapter.notifyItemInserted(i);
                cursor.close();
                return true;
            }
        });
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

    // Метод для получения курсора.
    private void setCursor() {
        cursor = new MatrixCursor(columnName);
    }

    @Override
    public void showComponent(List<Product> components) {
        adapter.setComponents(components);
    }

    @Override
    public void notifyCursorAdapter(List<Product> components) {
        setCursor();
        String[] temp = new String[6];

        // Заполняем курсор данными из полученного списка продуктов.
        for (int i = 0; i < components.size(); i++) {
            temp[0] = String.valueOf(i);
            temp[1] = components.get(i).getName();
            temp[2] = String.valueOf(components.get(i).getCaloriesDefault()).replace(".", ",");
            temp[3] = String.valueOf(components.get(i).getProteinsDefault()).replace(".", ",");
            temp[4] = String.valueOf(components.get(i).getFatsDefault()).replace(".", ",");
            temp[5] = String.valueOf(components.get(i).getCarbohydratesDefault()).replace(".", ",");
            cursor.addRow(temp);
        }

        // Обновляем курсор.
        simpleCursorAdapter.changeCursor(cursor);
    }
}
