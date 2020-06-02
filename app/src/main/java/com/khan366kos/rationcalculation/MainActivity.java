package com.khan366kos.rationcalculation;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import static com.khan366kos.rationcalculation.ProductContract.ProductEntry.TAG;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private DrawerLayout drawerLayout;
    private NavController navController;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView tvHeading;
    private EditText etDishName;

    private MenuItem menuItemAddComponent;

    private int newFragmentId;
    private int oldFragmentId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        this.setTitle("");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        int[] fragmentsId = new int[]{R.id.ration, R.id.products_base, R.id.dish, R.id.dishes_base};
        appBarConfiguration = new AppBarConfiguration.Builder(fragmentsId)
                .setDrawerLayout(drawerLayout)
                .build();
        navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        NavOptions navOptions = new NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in_right)
                //.setExitAnim(R.anim.slide_out_left)
                .build();

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller,
                                             @NonNull NavDestination destination,
                                             @Nullable Bundle arguments) {
                switch (destination.getId()) {
                    case R.id.products_base:
                        Log.d(TAG, "onDestinationChanged: setVisible");
                        tvHeading.setText(R.string.products_base);
                        break;
                    case R.id.dish:
                        tvHeading.setText(R.string.dish_name);
                        break;
                    case R.id.dishes_base:
                        tvHeading.setText(R.string.dishes_base);
                        break;
                    default:
                        tvHeading.setText(currentDate());
                }
            }
        });

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            newFragmentId = menuItem.getItemId();
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        });

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                if (newFragmentId != 0) {
                    if (newFragmentId != oldFragmentId) {
                        navController.navigate(newFragmentId, null, navOptions);
                        oldFragmentId = newFragmentId;
                    }
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.tollbar_menu_ration, menu);
        menuItemAddComponent = menu.findItem(R.id.add_component);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (etDishName.hasFocus()) {
            etDishName.clearFocus();
        } else super.onBackPressed();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (newFragmentId == R.id.products_base) menuItemAddComponent.setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void init() {
        tvHeading = findViewById(R.id.tv_heading);
        tvHeading.setText(currentDate());
        etDishName = findViewById(R.id.et_dish_name);
    }

    // Метод для получеия текущей даты в виде строики в заданном формате "02 янв. 2020 г."
    private String currentDate() {
        Date date = new Date();
        Locale locale = new Locale("ru", "RU");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy", locale);
        return simpleDateFormat.format(date) + " г." + " (Сегодня) ";
    }

    @Override
    protected void onResume() {
        super.onResume();
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                InputMethodManager inputMethodManager =
                        (InputMethodManager) drawerView.getContext()
                                .getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(drawerView.getWindowToken(), 0);
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
    }
}
