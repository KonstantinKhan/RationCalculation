package com.khan366kos.rationcalculation.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.khan366kos.Objects.Product;
import com.khan366kos.rationcalculation.Adapters.SuggestionComponentAdapter;
import com.khan366kos.rationcalculation.Data.ProductDbHelper;
import com.khan366kos.rationcalculation.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static com.khan366kos.rationcalculation.ProductContract.ProductEntry.*;

public class DishFragment extends TemplateFragment {

    private RecyclerView recyclerView;
    private ProductDbHelper productDbHelper;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dish, container, false);
        recyclerView = view.findViewById(R.id.rv_products_in_database_component);
        recyclerView.setItemAnimator(null); // убираем анимацию элементов при изменениях.
        init(view);

        bnv = view.findViewById(R.id.bnv_dish);

        return view;
    }

    @Override
    public void onResume() {

        // Вызываем экземляр ProductDbHelper.
        productDbHelper = ProductDbHelper.getInstance(getActivity());

        // Создаем и запускаем поток.
        // См. реализацию в ProductDbHelper.run()
       /* Thread t = new Thread(productDbHelper);
        t.start();

        try {
            // Ждем завершения работы нити.
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        etDishWeightCooked.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    if (((EditText) view).getText().toString().equals("-")) {
                        ((EditText) view).setText("");
                    }
                }
            }
        });

        // Создаем адаптер для обработки запросов в svComponent.
        setComponentAdapter(new SuggestionComponentAdapter.OnSetWeightListener() {
            @Override
            public void makeValues() {
                setValuesRaw();
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
                        if (charSequence.toString().length() > 0) {
                            getComponentAdapter().getDish().setWeightCooked(Integer.parseInt(
                                    charSequence.toString()));
                            setValuesCooked();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }

            @Override
            public void deleteProductComponent() {
                setValuesRaw();
                setValuesCooked();
            }

            @Override
            public void collapseMenuItemSvComponent() {
                // Если MenuItem, было развернуто в виде SearchView, то сворачиваем его.
                if (menuItemSvComponent.isActionViewExpanded()) {
                    menuItemSvComponent.collapseActionView();
                }
            }

            @Override
            public void clearFocusEt() {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etDishWeightCooked.getWindowToken(), 0);
                etDishWeightCooked.setFocusableInTouchMode(false);
                etDishWeightCooked.setFocusable(false);
                etDishWeightCooked.setFocusableInTouchMode(true);
                etDishWeightCooked.setFocusable(true);
            }
        });

        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView
                .OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.add_product_in_dish:
                        return addProductToDish();
                    case R.id.clean_dish:
                        getComponentAdapter().getDish().getComposition().clear();
                        getComponentAdapter().notifyDataSetChanged();
                        return true;

                    case R.id.save_dish:

                        // Проверяем, указано ли название продукта.
                        // Если да, то выводим сообщение и активируем поле для ввода названия блюда.
                        if (getComponentAdapter().getDish().getName() == null) {
                            Toast.makeText(getActivity(), "Укажите название блюда",
                                    Toast.LENGTH_SHORT).show();
                            editDishName(true);
                            return true;
                        }

                        // Проверяем, есть ли в блюде добавленные продукты.
                        // Если нет, то выводи сообщение и активируем поле для выбора продукта.
                        else if (getComponentAdapter().getDish().getComposition().size() == 0) {
                            Toast.makeText(getActivity(), "Выберите продукт",
                                    Toast.LENGTH_SHORT).show();
                            addProductToDish();
                            return true;
                        }

                        // Проверяем, у всех ли продуктов указан вес.
                        else if (getComponentAdapter().checkFillWeightProducts()) {
                            Toast.makeText(getActivity(), "Укажите вес продуктов",
                                    Toast.LENGTH_SHORT).show();
                            return true;
                        }

                        // Проверяем, заполнено ли поле с весом готового продукта.
                        else if (etDishWeightCooked.getText().toString().length() == 0 ||
                                etDishWeightCooked.getText().toString().equals("-")) {
                            Toast.makeText(getActivity(), "Укажите вес готового блюда",
                                    Toast.LENGTH_SHORT).show();
                            etDishWeightCooked.requestFocus();
                            etDishWeightCooked.selectAll();
                            return true;
                        } else {
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
                        }
                }
                return false;
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(getComponentAdapter());

        getEtDishName().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                getEtDishName().hideSoftKey();

                // Присваиваем значение из EditText TextView, если EditText не пустой.
                if (getEtDishName().getText().toString().length() > 0) {
                    getTvHeading().setText(getEtDishName().getText().toString());
                    getComponentAdapter().getDish().setName(getTvHeading().getText().toString());
                }
                return true;
            }
        });
        super.onResume();
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menuItemSvComponent = menu.findItem(R.id.mi_sv_component);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.mi_sv_component) {
            getSvComponent().setMaxWidth(Integer.MAX_VALUE);
            getSvComponent().setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    getSvComponent().setSuggestionsAdapter(getCursorAdapter(s));
                    return false;
                }
            });

            // Слушатель для ослеживания событий, связанных с предложениями.
            getSvComponent().setOnSuggestionListener(new SearchView.OnSuggestionListener() {
                @Override
                public boolean onSuggestionSelect(int position) {
                    return true;
                }

                @Override
                public boolean onSuggestionClick(int position) {
                    return onClickHandlerSuggestions(position);
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    private SimpleCursorAdapter getCursorAdapter(String string) {
        return new SimpleCursorAdapter(getActivity(),
                R.layout.fragment_suggestions,
                cursor(where(string).toString()),
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

    // Метод для получения курсора с учетом использованных продуктов.
    private Cursor cursor(String selection) {
        return productDbHelper.getDb().query(TABLE_NAME_PRODUCTS,
                null,
                selection,
                null,
                null,
                null,
                null,
                null);
    }

    // Метод для получения значений продуктов, которые уже использованы в блюде в виде строки,
    // которая передается в SQL-запрос.
    private StringBuilder where(String string) {
        String selection = "";
        StringBuilder values = new StringBuilder();
        if (getComponentAdapter().getDish().getComposition().size() > 0) {
            selection = COLUMN_PRODUCT_NAME + " NOT LIKE ";
            for (int i = 0; i < getComponentAdapter().getDish().getComposition().size(); i++) {
                if (i == 0) {
                    values.append("'" + getComponentAdapter().getDish().getComposition().get(i).getName() + "'");
                } else {
                    values.append(" AND " + selection + "'" + getComponentAdapter().getDish().getComposition().get(i).getName() + "'");
                }
            }
        }

        StringBuilder val = new StringBuilder();
        if (getComponentAdapter().getDish().getComposition().size() > 0) {
            if (string.length() > 0) {
                val.append(" AND " + COLUMN_PRODUCT_NAME + " LIKE '%" + string + "%'");
            }
        } else {
            if (string.length() > 0) {
                val.append(COLUMN_PRODUCT_NAME + " LIKE '%" + string + "%'");
            }
        }
        return new StringBuilder(selection).append(values).append(val);
    }

    // Метод для инициализации View.
    private void init(View view) {

        tvDishCaloriesRaw = view.findViewById(R.id.tv_dish_calories_raw);
        tvDishProteinsRaw = view.findViewById(R.id.tv_dish_proteins_raw);
        tvDishFatsRaw = view.findViewById(R.id.tv_dish_fats_raw);
        tvDishCarbohydratesRaw = view.findViewById(R.id.tv_dish_carbohydrates_raw);

        etDishWeightCooked = view.findViewById(R.id.et_dish_weight_cooked);
        tvDishCaloriesCooked = view.findViewById(R.id.tv_dish_calories_cooked);
        tvDishProteinsCooked = view.findViewById(R.id.tv_dish_proteins_cooked);
        tvDishFatsCooked = view.findViewById(R.id.tv_dish_fats_cooked);
        tvDishCarbohydratesCooked = view.findViewById(R.id.tv_dish_carbohydrates_cooked);
    }

    // Устанавливаем зачения параметров блюда (калорийность, количество белков, жиров
    // и углеводов на 100 г. в готовом виде.
    private void setValuesCooked() {
        getComponentAdapter().getDish().setNutrientsCookedStr();
        getComponentAdapter().getDish().setNutrientsCooked();
        tvDishCaloriesCooked.setText(getComponentAdapter().getDish().getCaloriesCookedStr());
        tvDishProteinsCooked.setText(getComponentAdapter().getDish().getProteinsCookedStr());
        tvDishFatsCooked.setText(getComponentAdapter().getDish().getFatsCookedStr());
        tvDishCarbohydratesCooked.setText(getComponentAdapter().getDish().getCarbohydratesCookedStr());
    }

    // Устанавливаем зачения параметров блюда (калорийность, количество белков, жиров
    // и углеводов на 100 г. в сыром виде.
    private void setValuesRaw() {
        tvDishCaloriesRaw.setText(getComponentAdapter().getDish().getCaloriesDefaultStr());
        tvDishProteinsRaw.setText(getComponentAdapter().getDish().getProteinsDefaultStr());
        tvDishFatsRaw.setText(getComponentAdapter().getDish().getFatsDefaultStr());
        tvDishCarbohydratesRaw.setText(getComponentAdapter().getDish().getCarbohydratesDefaultStr());
    }

    // Метод для обработки нажания на предложении.
    private boolean onClickHandlerSuggestions(int position) {

        int i = getComponentAdapter().getItemCount(); // Количество продуктов в блюде.

        // Создаем курсор в выпадающем меню.
        Cursor cursor = getSvComponent().getSuggestionsAdapter().getCursor();
        cursor.moveToPosition(position);

        // Новый продукт с данными, выбранными из поиска.
        Product product = new Product(cursor.getString(1),
                cursor.getInt(2),
                cursor.getDouble(3),
                cursor.getDouble(4),
                cursor.getDouble(5));

        // Добавляем продукт в блюдо
        getComponentAdapter().getDish().getComposition().add(product);

        // Обнуляем параметры SQL-запроса в адаптере.
        getSvComponent().setSuggestionsAdapter(getCursorAdapter(""));

        // Прокручивает RecyclerView до добавленного блюда.
        recyclerView.smoothScrollToPosition(getComponentAdapter()
                .getDish().getComposition().size() - 1);
        cursor.close();

        // Обнуляем строку запроса.
        getSvComponent().setQuery("", false);
        getComponentAdapter().notifyItemInserted(i);

        return true;
    }

    // Обратный вызов для обработки взаимодействия с предложениями.
    private SearchView.OnSuggestionListener suggestionHandler() {
        return new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                return onClickHandlerSuggestions(position);
            }
        };
    }

    // Обратный вызов для обработки ввода запросов в SearchView
    private SearchView.OnQueryTextListener searchProduct() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (getSvComponent().isIconified()) {
                    getMenu().findItem(R.id.mi_sv_component).collapseActionView();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getSvComponent().setSuggestionsAdapter(getCursorAdapter(newText));
                return false;
            }
        };
    }

    // Метода для вызова события добавления продукта в блюдо
    private boolean addProductToDish() {

        // Активируем ItemMenu, отвечающий за поиск продуктов.
        getMenu().findItem(R.id.mi_sv_component).expandActionView();

        // Устанавливаем вспомогательную надпись в поле поиска.
        getSvComponent().setQueryHint("Выберите продукт");

        // Устанавливаем максимальную ширину поля отображения вариантов поиска.
        getSvComponent().setMaxWidth(Integer.MAX_VALUE);

        // Слушатель для отслеживания ввода в SearchView.
        getSvComponent().setOnQueryTextListener(searchProduct());

        // Слушатель для ослеживания событий, связанных с предложениями.
        getSvComponent().setOnSuggestionListener(suggestionHandler());

        return true;
    }
}
