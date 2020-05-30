package com.khan366kos.rationcalculation.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, описывающий блюдо.
 */
public class Dish extends Product implements Serializable {

    private List<Product> composition = new ArrayList<>();

    /**
     * Конструктор для создания пустого экземпляра блюда.
     */
    public Dish() {
        setCooked(true);
    }

    /**
     * Конструктор для создания блюда с заданными КБЖУ
     * @param calories - калорийность блюда.
     * @param proteins - белки блюда.
     * @param fats - жиры блюда.
     * @param carbohydrates - углеводы блюда
     */
    public Dish(double calories, double proteins, double fats, double carbohydrates) {
        this.setCalories(calories);
        this.setProteins(proteins);
        this.setFats(fats);
        this.setCarbohydrates(carbohydrates);
    }

    public Dish(String name, int weight, double calories, double proteins, double fats, double carbohydrates) {
        super(name, weight, calories, proteins, fats, carbohydrates);
    }

    /**
     * Метод для добавления продукта в блюдо.
     *
     * @param product - добавляемый продукт.
     */
    public void addProduct(Product product) {
        composition.add(product);
    }

    public void removeProduct(Product product) {
        for (Product p : composition) {
            if (p.equals(product)) {
                composition.remove(product);
            }
        }
    }

    /**
     * Метод для обнуления значений макронутриентов рациона.
     */
    private void cleanNutrients() {
        setWeight(0);
        setCalories(0);
        setProteins(0);
        setFats(0);
        setCarbohydrates(0);
    }

    /**
     * Метод для подсчета макронутриентов и веса блюда.
     */
    public void setNutrients() {
        int  weightTemp = 0;
        double caloriesTemp = 0;
        double proteinsTemp = 0;
        double fatsTemp = 0;
        double carbohydratesTemp = 0;
        cleanNutrients();
        for (Product product : composition) {
            weightTemp = weightTemp + product.getWeight();
            caloriesTemp = caloriesTemp + product.getCalories();
            proteinsTemp = proteinsTemp + product.getProteins();
            fatsTemp = fatsTemp + product.getFats();
            carbohydratesTemp = carbohydratesTemp + product.getCarbohydrates();
        }
        setWeight(weightTemp);
        setCalories(caloriesTemp);
        setProteins(proteinsTemp);
        setFats(fatsTemp);
        setCarbohydrates(carbohydratesTemp);
        setNutrientsRawStr();
    }

    public int getProductCount() {
        return composition.size();
    }

    public List<Product> getComposition() {
        return composition;
    }

    public Product getProduct(String productName) {
        Product result = null;
        for (Product product : composition) {
            if (product.getName().equals(productName))
                result = product;
        }
        return result;
    }
}
