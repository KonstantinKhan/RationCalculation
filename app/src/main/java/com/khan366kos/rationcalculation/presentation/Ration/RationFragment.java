package com.khan366kos.rationcalculation.presentation.Ration;

import android.app.DatePickerDialog;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.khan366kos.rationcalculation.Fragments.TemplateFragment;
import com.khan366kos.rationcalculation.Model.Dish;
import com.khan366kos.rationcalculation.Model.Product;
import com.khan366kos.rationcalculation.Model.Ration;
import com.khan366kos.rationcalculation.R;
import com.khan366kos.rationcalculation.Service.AppCursorAdapter.CursorAdapterFactory;
import com.khan366kos.rationcalculation.Service.AppCursorAdapter.CursorAdapterTypes;
import com.khan366kos.rationcalculation.presentation.Dish.DishFragment;

import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static com.khan366kos.rationcalculation.Data.ProductContract.ProductEntry.*;
import static java.math.BigDecimal.*;

public class RationFragment extends TemplateFragment implements ContractRational.RationView {

    private BottomNavigationView bnv;
    private TextView tvHeading;
    private RecyclerView recyclerView;
    private RationAdapter adapter;
    private RationPresenter presenter;
    private SearchView svComponent;
    private MenuItem menuItemSvComponent;
    private MatrixCursor cursor;
    private SimpleCursorAdapter simpleCursorAdapter;
    private int bnvMenu;
    private CursorAdapterFactory cursorAdapterFactory;
    private String[] columnName;
    private TextView tvWeightRation;
    private TextView tvCaloriesRation;
    private TextView tvProteinsRation;
    private TextView tvFatsRation;
    private TextView tvCarbohydratesRation;
    private DatePickerDialog datePickerDialog;
    private Fragment thisFragment;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        cursorAdapterFactory = new CursorAdapterFactory();
        datePickerDialog = new DatePickerDialog(getContext());
        thisFragment = this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        presenter = new RationPresenter(this);
        presenter.onShowRation(currentDate());
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

        tvHeading.setOnClickListener(view -> {
            datePickerDialog.show();
        });

        datePickerDialog.setOnDateSetListener((datePicker, i, i1, i2) -> {
            Calendar calendar = new GregorianCalendar(i, i1, i2);
            Locale locale = new Locale("ru", "RU");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy", locale);
            Date date = calendar.getTime();
            String dateStr = simpleDateFormat.format(date) + "г.";
            tvHeading.setText(dateStr);
            presenter.onShowRation(dateStr);
            setValueRation();
        });

        bnv.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.pick_product:
                    columnName = new String[]{_ID, COLUMN_PRODUCT_NAME, COLUMN_PRODUCT_CALORIES,
                            COLUMN_PRODUCT_PROTEINS, COLUMN_PRODUCT_FATS, COLUMN_PRODUCT_CARBOHYDRATES};
                    simpleCursorAdapter = cursorAdapterFactory.getCursorAdapter(getContext(),
                            CursorAdapterTypes.PRODUCTS);
                    svComponent.setSuggestionsAdapter(simpleCursorAdapter);
                    bnvMenu = R.id.pick_product;
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
                    bnvMenu = R.id.pick_dish;
                    getMenu().findItem(R.id.mi_sv_component).expandActionView();
                    getSvComponent().setQueryHint("Выберите блюдо");
                    return true;
            }
            return false;
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);

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
                adapter.getRation().addProduct(product);

                // Прокручивает RecyclerView до добавленного компонента.
                recyclerView.smoothScrollToPosition(adapter.getComponents().size() - 1);

                // Обнуляем строку запроса.
                svComponent.setQuery("", false);

                adapter.notifyItemInserted(i);

                presenter.onSuggestionClick(adapter.getRation());

                cursor.close();
                return true;
            }
        });
    }

    private void init(View view) {
        tvHeading = getActivity().findViewById(R.id.tv_heading);
        bnv = view.findViewById(R.id.bottom_menu);
        recyclerView = view.findViewById(R.id.rl_ration_component);
        tvWeightRation = view.findViewById(R.id.tv_rational_weight_small);
        tvCaloriesRation = view.findViewById(R.id.tv_ration_calories_small);
        tvProteinsRation = view.findViewById(R.id.tv_ration_proteins_small);
        tvFatsRation = view.findViewById(R.id.tv_ration_fats_small);
        tvCarbohydratesRation = view.findViewById(R.id.tv_ration_carbohydrates_small);
    }

    // Метод для получения текущей даты в виде строики в заданном формате "02 янв. 2020 г."
    private String currentDate() {
        Date date = new Date();
        Locale locale = new Locale("ru", "RU");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy", locale);
        return simpleDateFormat.format(date) + "г.";
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

    private double[] setNutrientsRation() {
        double[] nutrients = new double[5];

        if (adapter.getComponents().size() != 0) {
            for (Product product : adapter.getComponents()) {
                nutrients[0] = nutrients[0] + product.getWeight();
                nutrients[1] = nutrients[1] + product.getCalories();
                nutrients[2] = nutrients[2] + product.getProteins();
                nutrients[3] = nutrients[3] + product.getFats();
                nutrients[4] = nutrients[4] + product.getCarbohydrates();
            }

            nutrients[0] = valueOf(nutrients[0]).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
            nutrients[1] = valueOf(nutrients[1]).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
            nutrients[2] = valueOf(nutrients[2]).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
            nutrients[3] = valueOf(nutrients[3]).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
            nutrients[4] = valueOf(nutrients[4]).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
        }
        return nutrients;
    }

    @Override
    public void setRation(Ration ration) {
        adapter = new RationAdapter(new RationAdapter.OnMove() {
            @Override
            public void onSetWeightComponent() {
                setValueRation();
                presenter.onSuggestionClick(adapter.getRation());
            }

            @Override
            public void onClickBtnDelete() {
                setValueRation();
                presenter.onSuggestionClick(adapter.getRation());
            }

            @Override
            public void onClickBtnEdit() {
                String name = adapter.getRation().getComposition().get(adapter.getEditPosition()).getName();
                presenter.onClickBtnEdit(name);
            }
        }, ration);
        adapter.getRation().setData(currentDate());
        recyclerView.setAdapter(adapter);
        setValueRation();
        recyclerView.setItemAnimator(null);
    }

    @Override
    public void editDish(Dish dish) {
        Fragment fragment = new DishFragment(dish);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit();
    }

    private void setValueRation() {
        double[] nutrientsRation = RationFragment.this.setNutrientsRation();
        tvWeightRation.setText(String.valueOf(nutrientsRation[0]).replace(".", ","));
        tvCaloriesRation.setText(String.valueOf(nutrientsRation[1]).replace(".", ","));
        tvProteinsRation.setText(String.valueOf(nutrientsRation[2]).replace(".", ","));
        tvFatsRation.setText(String.valueOf(nutrientsRation[3]).replace(".", ","));
        tvCarbohydratesRation.setText(String.valueOf(nutrientsRation[4]).replace(".", ","));
    }
}
