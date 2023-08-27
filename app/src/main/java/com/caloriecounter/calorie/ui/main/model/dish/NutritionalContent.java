package com.caloriecounter.calorie.ui.main.model.dish;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public  class NutritionalContent implements Serializable {

    @Expose
    @SerializedName("trans_fat")
    private double transFat;
    @Expose
    @SerializedName("vitamin_a")
    private double vitaminA;
    @Expose
    @SerializedName("potassium")
    private double potassium;
    @Expose
    @SerializedName("polyunsaturated_fat")
    private double polyunsaturatedFat;
    @Expose
    @SerializedName("iron")
    private double iron;
    @Expose
    @SerializedName("fat")
    private double fat;
    @Expose
    @SerializedName("vitamin_c")
    private double vitaminC;
    @Expose
    @SerializedName("monounsaturated_fat")
    private double monounsaturatedFat;
    @Expose
    @SerializedName("calcium")
    private double calcium;
    @Expose
    @SerializedName("sodium")
    private double sodium;
    @Expose
    @SerializedName("carbohydrates")
    private double carbohydrates;
    @Expose
    @SerializedName("protein")
    private double protein;
    @Expose
    @SerializedName("saturated_fat")
    private double saturatedFat;
    @Expose
    @SerializedName("fiber")
    private double fiber;
    @Expose
    @SerializedName("sugar")
    private double sugar;
    private Energy energy;

    public Energy getEnergy() {
        return energy;
    }

    public void setEnergy(Energy energy) {
        this.energy = energy;
    }

    public double getTransFat() {
        return transFat;
    }

    public void setTransFat(double transFat) {
        this.transFat = transFat;
    }

    public double getVitaminA() {
        return vitaminA;
    }

    public void setVitaminA(double vitaminA) {
        this.vitaminA = vitaminA;
    }

    public double getPotassium() {
        return potassium;
    }

    public void setPotassium(double potassium) {
        this.potassium = potassium;
    }

    public double getPolyunsaturatedFat() {
        return polyunsaturatedFat;
    }

    public void setPolyunsaturatedFat(double polyunsaturatedFat) {
        this.polyunsaturatedFat = polyunsaturatedFat;
    }

    public double getIron() {
        return iron;
    }

    public void setIron(double iron) {
        this.iron = iron;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getVitaminC() {
        return vitaminC;
    }

    public void setVitaminC(double vitaminC) {
        this.vitaminC = vitaminC;
    }

    public double getMonounsaturatedFat() {
        return monounsaturatedFat;
    }

    public void setMonounsaturatedFat(double monounsaturatedFat) {
        this.monounsaturatedFat = monounsaturatedFat;
    }

    public double getCalcium() {
        return calcium;
    }

    public void setCalcium(double calcium) {
        this.calcium = calcium;
    }

    public double getSodium() {
        return sodium;
    }

    public void setSodium(double sodium) {
        this.sodium = sodium;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getSaturatedFat() {
        return saturatedFat;
    }

    public void setSaturatedFat(double saturatedFat) {
        this.saturatedFat = saturatedFat;
    }

    public double getFiber() {
        return fiber;
    }

    public void setFiber(double fiber) {
        this.fiber = fiber;
    }

    public double getSugar() {
        return sugar;
    }

    public void setSugar(double sugar) {
        this.sugar = sugar;
    }
}
