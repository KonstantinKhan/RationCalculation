package com.khan366kos.rationcalculation.Model;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.khan366kos.rationcalculation.Data.ProductContract.ProductEntry.TAG;

/**
 * Класс, описыващий дневной рацион
 */
public class Ration implements Serializable {
    private double caloriesLimit; // Лимит калорий дневного рациона.
    private double caloriesRation; // Фактическое количество калорий дневного рациона.
    private double proteinsLimit; // Лимит белков дневного рацона.
    private double proteinsRation; // Фактическое количество белков дневного рациона.
    private double fatsLimit; // Лимит жиров дневного рациона.
    private double fatsRation; // Фактическое количество жиров дневного рациона.
    private double carbohydratesLimit; // Лимит углеводов дневного рациона.
    private double carbohydratesRation; // Фактическое количество углеводов дневного рациона.
    private List<Product> composition; // состав рациона.
    private String data; // Дата рациона.


    public Ration() {
        composition = new ArrayList<>();
    }

    public Ration(double caloriesLimit, double proteinsLimit, double fatsLimit, double carbohydratesLimit) {
        this.caloriesLimit = caloriesLimit;
        this.proteinsLimit = proteinsLimit;
        this.fatsLimit = fatsLimit;
        this.carbohydratesLimit = carbohydratesLimit;
        composition = new ArrayList<>();
    }

    /**
     * Метод для добавления продукта в рацион.
     *
     * @param product - добавляемый продукт.
     */
    public void addProduct(Product product) {
        composition.add(product);
    }

    /**
     * Метод для удаления продукта из рациона.
     *
     * @param product - проверяемый продукт.
     */
    public void remove(Product product) {
        for (Product p : composition) {
            if (p.equals(product)) {
                composition.remove(product);
            }
        }
    }

    public List<Product> getComposition() {
        return composition;
    }

    public double getCaloriesLimit() {
        return caloriesLimit;
    }

    public void setCaloriesLimit(double caloriesLimit) {
        this.caloriesLimit = caloriesLimit;
    }

    public double getProteinsLimit() {
        return proteinsLimit;
    }

    public void setProteinsLimit(double proteinsLimit) {
        this.proteinsLimit = proteinsLimit;
    }

    public double getFatsLimit() {
        return fatsLimit;
    }

    public void setFatsLimit(double fatsLimit) {
        this.fatsLimit = fatsLimit;
    }

    public double getCarbohydratesLimit() {
        return carbohydratesLimit;
    }

    public void setCarbohydratesLimit(double carbohydratesLimit) {
        this.carbohydratesLimit = carbohydratesLimit;
    }

    /**
     * Метод для подсчета макронутриентов рациона.
     */
    public void setNutrients() {
        cleanNutrients();
        for (Product product : composition) {
            caloriesRation = caloriesRation + product.getCalories();
            proteinsRation = proteinsRation + product.getProteins();
            fatsRation = fatsRation + product.getFats();
            carbohydratesRation = carbohydratesRation + product.getCarbohydrates();
        }
    }

    /**
     * Метод для обнуления значений макронутриентов рациона.
     */
    private void cleanNutrients() {
        caloriesRation = 0;
        proteinsRation = 0;
        fatsRation = 0;
        carbohydratesRation = 0;
    }

    public double getCaloriesRation() {
        return caloriesRation;
    }

    public double getProteinsRation() {
        return proteinsRation;
    }

    public double getFatsRation() {
        return fatsRation;
    }

    public double getCarbohydratesRation() {
        return carbohydratesRation;
    }

    public void setComposition(List<Product> composition) {
        this.composition = composition;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
