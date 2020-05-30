package com.khan366kos.rationcalculation.presentation.Dish;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.widget.SearchView;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.khan366kos.rationcalculation.Model.Product;
import com.khan366kos.rationcalculation.Data.ProductDbHelper;
import com.khan366kos.rationcalculation.Fragments.TemplateFragment;
import com.khan366kos.rationcalculation.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.khan366kos.rationcalculation.ProductContract.ProductEntry.*;

public class DishFragment extends Fragment implements ContractDishFragment.DishView {

    private RecyclerView recyclerView;
    private Menu menu;
    private SearchView svComponent;

    private TextView tvDishCaloriesRaw;
    private TextView tvDishProteinsRaw;
    private TextView tvDishFatsRaw;
    private TextView tvDishCarbohydratesRaw;

    private EditText etDishWeightCooked;
    private TextView tvDishCaloriesCooked;
    private TextView tvDishProteinsCooked;
    private TextView tvDishFatsCooked;
    private TextView tvDishCarbohydratesCooked;

    private BottomNavigationView bnv;

    private MenuItem menuItemSvComponent;


    private DishPresenter presenter;
    private DishAdapter dishAdapter;

    private MatrixCursor cursor;
    private SimpleCursorAdapter simpleCursorAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dish, container, false);
        init(view);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        setHasOptionsMenu(true); // Позволяем фрагменту работать с меню.

        setSimpleCursorAdapter(); // Получаем экземпляр SimpleCursorAdapter.

        presenter = new DishPresenter(this); // Получаем экземпляр DishPresenter.

        dishAdapter = new DishAdapter(new DishAdapter.OnMove() {

            @Override
            public void makeValues() {

                if (dishAdapter.getDish().getWeight() != 0) {
                    setValuesRaw();
                }
                try {
                    setValuesCooked();
                } catch (NumberFormatException e) {
                }

                // Обработчик событий при введении данных в EditText для хранения данных о весе
                // готового продукта.
                etDishWeightCooked.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence,
                                                  int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        setValuesCooked();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }

            @Override
            public void deleteProductComponent() {
                if (!etDishWeightCooked.getText().toString().equals("-") &&
                        dishAdapter.getDish().getWeight() != 0) {
                    setValuesRaw();
                    setValuesCooked();
                }
            }

            @Override
            public void collapseMenuItemSvComponent() {
                // Если MenuItem, было развернуто в виде SearchView, то сворачиваем его.
                if (menuItemSvComponent.isActionViewExpanded()) {
                    menuItemSvComponent.collapseActionView();
                }
            }
        });
    }

    @Override
    public void onResume() {

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(dishAdapter);

        etDishWeightCooked.setOnFocusChangeListener((view, b) -> {

            // Выполняем действия при наведении фокуса на EditText,
            // отвечающего за значение веса готового блюда.
            if (b) {

                // Заменяем значение "-" на "".
                if (((EditText) view).getText().toString().equals("-")) {
                    ((EditText) view).setText("");
                }

                // Закрываем SearchView.
                if (menuItemSvComponent != null) {
                    if (menuItemSvComponent.isActionViewExpanded()) {
                        menuItemSvComponent.collapseActionView();
                    }
                }
            }
        });

        bnv.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.add_product_in_dish:
                    menu.findItem(R.id.mi_sv_component).expandActionView();
                    return true;
                case R.id.clean_dish:
                    //getComponentAdapter().getDish().getComposition().clear();
                    //getComponentAdapter().notifyDataSetChanged();
                    return true;

                case R.id.save_dish:

                    // Проверяем, указано ли название продукта.
                    // Если да, то выводим сообщение и активируем поле для ввода названия блюда.
                    /*if (getComponentAdapter().getDish().getName() == null) {
                        Toast.makeText(getActivity(), "Укажите название блюда",
                                Toast.LENGTH_SHORT).show();
                        editDishName(true);
                        return true;
                    }*/

                    // Проверяем, есть ли в блюде добавленные продукты.
                    // Если нет, то выводи сообщение и активируем поле для выбора продукта.
                   /* else if (getComponentAdapter().getDish().getComposition().size() == 0) {
                        Toast.makeText(getActivity(), "Выберите продукт",
                                Toast.LENGTH_SHORT).show();
                        addProductToDish();
                        return true;
                    }*/

                    // Проверяем, у всех ли продуктов указан вес.
                    /*else if (getComponentAdapter().checkFillWeightProducts()) {
                        Toast.makeText(getActivity(), "Укажите вес продуктов",
                                Toast.LENGTH_SHORT).show();
                        return true;
                    }*/

                    // Проверяем, заполнено ли поле с весом готового продукта.
                    /*else if (etDishWeightCooked.getText().toString().length() == 0 ||
                            etDishWeightCooked.getText().toString().equals("-")) {
                        Toast.makeText(getActivity(), "Укажите вес готового блюда",
                                Toast.LENGTH_SHORT).show();
                        etDishWeightCooked.requestFocus();
                        etDishWeightCooked.selectAll();
                        return true;*/
                   /* } else {
                        // Сериализация блюда для хранения его в базе данных.
                        ByteArrayOutputStream byteArrayOutputStream =
                                new ByteArrayOutputStream();
                        try {
                            ObjectOutputStream outputStream =
                                    new ObjectOutputStream(byteArrayOutputStream);
                            outputStream.writeObject(getComponentAdapter().getDish());
                            outputStream.close();

                            // Записываем блюдо в базу данных
                            productDbHelper.insertDish(productDbHelper.getDb(),
                                    getComponentAdapter().getDish(), byteArrayOutputStream);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                    }*/
            }
            return false;
        });

        /*getEtDishName().setOnEditorActionListener((textView, i, keyEvent) -> {

            // Присваиваем значение из EditText TextView, если EditText не пустой.
            if (getEtDishName().getText().toString().length() > 0) {
                getTvHeading().setText(getEtDishName().getText().toString());
                getComponentAdapter().getDish().setName(getTvHeading().getText().toString());
            }
            return true;
        });*/
        super.onResume();
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);

        this.menu = menu;

        menuItemSvComponent = menu.findItem(R.id.mi_sv_component);
        svComponent = (SearchView) menuItemSvComponent.getActionView();

        // Устанавливаем вспомогательную надпись в поле поиска.
        svComponent.setQueryHint("Выберите продукт");

        svComponent.setSuggestionsAdapter(simpleCursorAdapter);

        // Устанавливаем максимальную ширину поля отображения вариантов поиска.
        svComponent.setMaxWidth(Integer.MAX_VALUE);

        svComponent.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                svComponent.post(() -> presenter.onQueryTextChange(s,
                        dishAdapter.getDish().getComposition()));
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

                int i = dishAdapter.getItemCount(); // Количество продуктов в блюде.

                // Создаем курсор в выпадающем меню.
                Cursor cursor = svComponent.getSuggestionsAdapter().getCursor();
                cursor.moveToPosition(position);

                // Новый продукт с данными, выбранными из поиска.
                Product product = new Product(cursor.getString(1),
                        cursor.getDouble(2),
                        cursor.getDouble(3),
                        cursor.getDouble(4),
                        cursor.getDouble(5));

                // Добавляем продукт в блюдо
                dishAdapter.getDish().getComposition().add(product);

                // Прокручивает RecyclerView до добавленного блюда.
                recyclerView.smoothScrollToPosition(dishAdapter
                        .getDish().getComposition().size() - 1);

                // Обнуляем строку запроса.
                svComponent.setQuery("", false);
                dishAdapter.notifyItemInserted(i);

                return true;
            }
        });

        // Закрываем SearchView, если у него пропадает фокус.
        svComponent.setOnQueryTextFocusChangeListener((view, b) ->
                svComponent.post(() -> {
                    if (!b) {
                        if (menuItemSvComponent.isActionViewExpanded()) {
                            menuItemSvComponent.collapseActionView();
                        }
                    }
                }));
    }

    // Метод для инициализации View.
    private void init(View view) {

        // Блок View, отражающих параметры сырого блюда.
        tvDishCaloriesRaw = view.findViewById(R.id.tv_dish_calories_raw);
        tvDishProteinsRaw = view.findViewById(R.id.tv_dish_proteins_raw);
        tvDishFatsRaw = view.findViewById(R.id.tv_dish_fats_raw);
        tvDishCarbohydratesRaw = view.findViewById(R.id.tv_dish_carbohydrates_raw);

        // Блок View, отражающих параметры готового блюда.
        etDishWeightCooked = view.findViewById(R.id.et_dish_weight_cooked);
        tvDishCaloriesCooked = view.findViewById(R.id.tv_dish_calories_cooked);
        tvDishProteinsCooked = view.findViewById(R.id.tv_dish_proteins_cooked);
        tvDishFatsCooked = view.findViewById(R.id.tv_dish_fats_cooked);
        tvDishCarbohydratesCooked = view.findViewById(R.id.tv_dish_carbohydrates_cooked);

        bnv = view.findViewById(R.id.bnv_dish);

        recyclerView = view.findViewById(R.id.rv_products_in_database_component);
        recyclerView.setItemAnimator(null); // убираем анимацию элементов при изменениях.
    }

    // Устанавливаем зачения параметров блюда на 100 г. в готовом виде.
    private void setValuesCooked() {
        if (etDishWeightCooked.getText().toString().length() != 0 &&
                !etDishWeightCooked.getText().toString().equals("-")) {
            dishAdapter.getDish().setWeightCooked(Integer.parseInt(
                    etDishWeightCooked.getText().toString()));
            dishAdapter.getDish().setNutrientsCookedStr();
            dishAdapter.getDish().setNutrientsCooked();
            tvDishCaloriesCooked.setText(dishAdapter.getDish().getCaloriesCookedStr());
            tvDishProteinsCooked.setText(dishAdapter.getDish().getProteinsCookedStr());
            tvDishFatsCooked.setText(dishAdapter.getDish().getFatsCookedStr());
            tvDishCarbohydratesCooked.setText(dishAdapter.getDish().getCarbohydratesCookedStr());
        } else if (etDishWeightCooked.getText().toString().length() == 0) {
            tvDishCaloriesCooked.setText("-");
            tvDishProteinsCooked.setText("-");
            tvDishFatsCooked.setText("-");
            tvDishCarbohydratesCooked.setText("-");
        }

    }

    // Устанавливаем зачения параметров блюда на 100 г. в сыром виде.
    private void setValuesRaw() {
        tvDishCaloriesRaw.setText(dishAdapter.getDish().getCaloriesDefaultStr());
        tvDishProteinsRaw.setText(dishAdapter.getDish().getProteinsDefaultStr());
        tvDishFatsRaw.setText(dishAdapter.getDish().getFatsDefaultStr());
        tvDishCarbohydratesRaw.setText(dishAdapter.getDish().getCarbohydratesDefaultStr());
    }

    @Override
    public void notifyCursorAdapter(List<Product> products) {

        setCursor();
        String[] temp = new String[6];

        // Заполняем курсор данными из полученного списка продуктов.
        for (int i = 0; i < products.size(); i++) {
            temp[0] = String.valueOf(i);
            temp[1] = products.get(i).getName();
            temp[2] = String.valueOf(products.get(i).getCaloriesDefault());
            temp[3] = String.valueOf(products.get(i).getProteinsDefault());
            temp[4] = String.valueOf(products.get(i).getFatsDefault());
            temp[5] = String.valueOf(products.get(i).getCarbohydratesDefault());
            cursor.addRow(temp);
        }

        // Обновляем курсор.
        simpleCursorAdapter.changeCursor(cursor);
    }

    // Метод для получения курсора.
    private void setCursor() {
        String[] columnName = {_ID, COLUMN_PRODUCT_NAME, COLUMN_PRODUCT_CALORIES,
                COLUMN_PRODUCT_PROTEINS, COLUMN_PRODUCT_FATS, COLUMN_PRODUCT_CARBOHYDRATES};
        cursor = new MatrixCursor(columnName);
    }

    // Метод для получения экземпляра SimpleCursorAdapter.
    public void setSimpleCursorAdapter() {
        simpleCursorAdapter = new SimpleCursorAdapter(getContext(),
                R.layout.fragment_suggestions,
                null,
                new String[]{COLUMN_PRODUCT_NAME,
                        COLUMN_PRODUCT_CALORIES,
                        COLUMN_PRODUCT_PROTEINS,
                        COLUMN_PRODUCT_FATS,
                        COLUMN_PRODUCT_CARBOHYDRATES},
                new int[]{R.id.tv_suggestion_product_name,
                        R.id.tv_suggestion_product_calories,
                        R.id.tv_suggestion_product_proteins,
                        R.id.tv_suggestion_product_fats,
                        R.id.tv_suggestion_product_carbohydrates},
                0);
    }
}
