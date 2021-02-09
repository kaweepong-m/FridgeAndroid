package com.example.fridge;

public class Food {
    int id,quantity;
    String foodName,foodEXP,foodType,canCheckPrice;

    public Food(int id, int quantity, String canCheckPrice, String foodName, String foodEXP,String foodType) {
        this.id = id;
        this.quantity = quantity;
        this.canCheckPrice = canCheckPrice;
        this.foodName = foodName;
        this.foodEXP = foodEXP;
        this.foodType = foodType;
    }

    public int getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getCanCheckPrice() {
        return canCheckPrice;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getFoodEXP() {
        return foodEXP;
    }

    public String getFoodType() {
        return foodType;
    }
}
