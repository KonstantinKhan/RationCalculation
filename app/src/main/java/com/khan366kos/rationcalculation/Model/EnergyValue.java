package com.khan366kos.rationcalculation.Model;

import java.io.Serializable;

/**
 * Абстрактный класс, описывающий характеристики продукта
 */
public abstract class EnergyValue implements Serializable {

    private double calories;
    private double proteins;
    private double fats;
    private double carbohydrates;

    /**
     * Конструктор без параметров для создания пустого экземпляра.
     */
    protected EnergyValue() {
    }

    public double getCalories() {
        return calories;
    }

    public double getProteins() {
        return proteins;
    }

    public double getFats() {
        return fats;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }

    protected void setCalories(int weight, double caloriesDefault) {
        double d = weight * caloriesDefault / 100;
        this.calories = round(d);
    }

    protected void setProteins(int weight, double proteinsDefault) {
        double d = weight * proteinsDefault / 100;
        this.proteins = round(d);
    }

    protected void setFats(int weight, double fatsDefault) {
        double d = weight * fatsDefault / 100;
        this.fats = round(d);
    }

    protected void setCarbohydrates(int weight, double carbohydratesDefault) {
        double d = weight * carbohydratesDefault / 100;
        this.carbohydrates = round(d);
    }

    private double round(double value) {
        return (double) Math.round(value * 100.0) / 100.0;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public void setProteins(double proteins) {
        this.proteins = proteins;
    }

    public void setFats(double fats) {
        this.fats = fats;
    }

    public void setCarbohydrates(double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }
}
