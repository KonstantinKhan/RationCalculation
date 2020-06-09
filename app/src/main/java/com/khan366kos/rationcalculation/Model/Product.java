package com.khan366kos.rationcalculation.Model;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.khan366kos.rationcalculation.Data.ProductContract.ProductEntry.TAG;

/**
 * Класс, описывающий конкретный продукт.
 */
@JsonAutoDetect
public class Product extends EnergyValue implements Serializable {

    private double caloriesDefault; // калории на 100 гр. в сыром виде
    private double proteinsDefault; // белки на 100 гр. в сыром виде
    private double fatsDefault; // жиры на 100 гр. в сыром виде
    private double carbohydratesDefault; // углеводы на 100 гр. в сыром виде

    private String caloriesDefaultStr; // калории на 100 гр. в сыром виде
    private String proteinsDefaultStr; // белки на 100 гр. в сыром виде
    private String fatsDefaultStr; // жиры на 100 гр. в сыром виде
    private String carbohydratesDefaultStr; // углеводы на 100 гр. в сыром виде

    private int weightCooked;

    private String caloriesCookedStr; // калории на 100 гр. в готовом виде.
    private String proteinsCookedStr; // белки на 100 гр. в готовом виде.
    private String fatsCookedStr; // жиры на 100 гр. в готовом виде.
    private String carbohydratesCookedStr; // углеводы на 100 гр. в готовом виде.

    private int weightPortion;
    private double caloriesPortion;
    private double proteinsPortion;
    private double fatsPortion;
    private double carbohydratesPortion;

    private boolean cooked;

    private String name; // Наименование продукта.
    private int weight; // Вес продукта.

    /**
     * Конструктор для создания путого экземпляра продукта.
     */
    public Product() {
        System.out.println("Создан пустой экземпляр продукта");
    }

    /**
     * Конструктор для создания экземпляра продукта с заданными параметрами без указания веса.
     *
     * @param name          - наименование продукта. Не может быть пустым.
     * @param calories      - калорийность продукта.
     * @param proteins      - белки продукта.
     * @param fats          - жиры продукта.
     * @param carbohydrates - углеводы продукта.
     */
    public Product(String name, double calories, double proteins, double fats, double carbohydrates) {
        caloriesDefault = calories;
        proteinsDefault = proteins;
        fatsDefault = fats;
        carbohydratesDefault = carbohydrates;
        this.name = name;
        cooked = false;
    }

    /**
     * Конструктор для созданния экземпляра продукта с заданными параметрами.
     *
     * @param name          - наименование продукта. Не может быть пустым.
     * @param weight        - вес продукта.
     * @param calories      - калорийность продукта.
     * @param proteins      - белки продукта.
     * @param fats          - жиры продукта.
     * @param carbohydrates - углеводы продукта.
     */
    public Product(String name, int weight, double calories, double proteins, double fats, double carbohydrates) {
        this(name, calories, proteins, fats, carbohydrates);
        setWeight(weight);
        System.out.println("Создан объект Product с заданным параметром веса");
    }

    public void setMacronutrientsPortion() {
      /*  if (!cooked) {
            weightCooked = weight;
            weightPortion = weight;
        } else {
            if (weightPortion == 0) {
                System.out.println("Укажите вес порции");
            }
            if (weightCooked == 0) {
                System.out.println("Укажите вес приготовленного блюда");
            }
            if (weight == 0) {
                System.out.println("Укажите вес блюда в сыром виде");
            }
        }*/
        // if (weight != 0 && weightCooked != 0) {
        caloriesPortion = new BigDecimal(weightPortion * getCaloriesDefault() / 100)
                .setScale(1, RoundingMode.HALF_EVEN).doubleValue();
        proteinsPortion = new BigDecimal(weightPortion * getProteinsDefault() / 100)
                .setScale(1, RoundingMode.HALF_EVEN).doubleValue();
        fatsPortion = new BigDecimal(weightPortion * getFatsDefault() / 100)
                .setScale(1, RoundingMode.HALF_EVEN).doubleValue();
        carbohydratesPortion = new BigDecimal(weightPortion * getCarbohydratesDefault() / 100)
                .setScale(1, RoundingMode.HALF_EVEN).doubleValue();
        // }
    }

    public void setCooked(boolean cooked) {
        this.cooked = cooked;
    }

    public int getWeightCooked() {
        return weightCooked;
    }

    public int getWeightPortion() {
        return weightPortion;
    }

    public double getCaloriesPortion() {
        return caloriesPortion;
    }

    public double getProteinsPortion() {
        return proteinsPortion;
    }

    public double getFatsPortion() {
        return fatsPortion;
    }

    public double getCarbohydratesPortion() {
        return carbohydratesPortion;
    }

    public String getCaloriesDefaultStr() {
        return caloriesDefaultStr;
    }

    public String getProteinsDefaultStr() {
        return proteinsDefaultStr;
    }

    public String getFatsDefaultStr() {
        return fatsDefaultStr;
    }

    public String getCarbohydratesDefaultStr() {
        return carbohydratesDefaultStr;
    }

    public String getCaloriesCookedStr() {
        return caloriesCookedStr;
    }

    public String getProteinsCookedStr() {
        return proteinsCookedStr;
    }

    public String getFatsCookedStr() {
        return fatsCookedStr;
    }

    public String getCarbohydratesCookedStr() {
        return carbohydratesCookedStr;
    }

    public void setWeightCooked(int weightCooked) {
        this.weightCooked = weightCooked;
    }

    public void setWeightPortion(int weightPortion) {
        this.weightPortion = weightPortion;
    }

    public void setWeightThroughCalories(double calories) {
        double value = calories / getCaloriesDefault();
        setWeight((int) (value * 100));
    }

    public void setWeightThroughProteins(double proteins) {
        double value = proteins / getProteinsDefault();
        setWeight((int) (value * 100));
    }

    public void setWeightThroughFats(double fats) {
        double value = fats / getFatsDefault();
        setWeight((int) (value * 100));
    }

    public void setWeightThroughCarbohydrates(double carbohydrates) {
        double value = carbohydrates / getCarbohydratesDefault();
        setWeight((int) (value * 100));
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
        setCalories(getWeight(), getCaloriesDefault());
        setProteins(getWeight(), getProteinsDefault());
        setFats(getWeight(), getFatsDefault());
        setCarbohydrates(getWeight(), getCarbohydratesDefault());
    }

    public double getCaloriesDefault() {
        return caloriesDefault;
    }

    public double getProteinsDefault() {
        return proteinsDefault;
    }

    public double getFatsDefault() {
        return fatsDefault;
    }

    public double getCarbohydratesDefault() {
        return carbohydratesDefault;
    }

    /**
     * Перегруженный метод сравнения продуктов по названию.
     *
     * @param obj - Объект
     * @return - Возвращает true, если наименование продуктов совпадают, false, если не совпадают.
     */
    @Override
    public boolean equals(Object obj) {
        Product product = (Product) obj;
        if (product.name.equals(this.name))
            return true;
        else return false;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Метод для расчетного значения калорийности продукта.
     *
     * @return значение калорийности продукта, рассчитанное по макронутриентам.
     */
    public double checkCalories() {
        return getWeight() * (getProteinsDefault() * 4 + getFatsDefault() * 9 + getCarbohydratesDefault() * 4) / 100;
    }

    /**
     * Метод для расчета отклонения заявленного значения калорийности от расчетного.
     *
     * @return значение отклонения заявленного значения калорийности от расчетного.
     */
    public double deviation() {
        return (getCalories() - checkCalories()) / getCalories();
    }

    /**
     * Метод для вычисления значения нутриента на 100 гр.
     *
     * @param value
     * @param weight
     * @return
     */
    private String valuePer100Str(double value, double weight) {
        return String.valueOf(valuePer100(value, weight)).replace(".", ",");
    }

    private double valuePer100(double value, double weight) {
        double result;
        try {
            result = new BigDecimal((value / weight) * 100)
                    .setScale(1, RoundingMode.HALF_EVEN).doubleValue();

        } catch (NumberFormatException e) {
            result = 0;
        }
        return result;
    }

    protected void setNutrientsRawStr() {
        caloriesDefaultStr = valuePer100Str(getCalories(), this.weight);
        proteinsDefaultStr = valuePer100Str(getProteins(), this.weight);
        fatsDefaultStr = valuePer100Str(getFats(), this.weight);
        carbohydratesDefaultStr = valuePer100Str(getCarbohydrates(), this.weight);
    }

    public void setNutrientsCookedStr() {
        caloriesCookedStr = valuePer100Str(getCalories(), this.weightCooked);
        proteinsCookedStr = valuePer100Str(getProteins(), this.weightCooked);
        fatsCookedStr = valuePer100Str(getFats(), this.weightCooked);
        carbohydratesCookedStr = valuePer100Str(getCarbohydrates(), this.weightCooked);
    }

    public void setNutrientsCooked() {
        caloriesDefault = valuePer100(getCalories(), this.weightCooked);
        proteinsDefault = valuePer100(getProteins(), this.weightCooked);
        fatsDefault = valuePer100(getFats(), this.weightCooked);
        carbohydratesDefault = valuePer100(getCarbohydrates(), this.weightCooked);
    }

    public void setCaloriesDefault(double caloriesDefault) {
        this.caloriesDefault = caloriesDefault;
    }

    public void setProteinsDefault(double proteinsDefault) {
        this.proteinsDefault = proteinsDefault;
    }

    public void setFatsDefault(double fatsDefault) {
        this.fatsDefault = fatsDefault;
    }

    public void setCarbohydratesDefault(double carbohydratesDefault) {
        this.carbohydratesDefault = carbohydratesDefault;
    }
}
