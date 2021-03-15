package seedu.fridgefriend.food;

import seedu.fridgefriend.exception.InvalidDateException;

/**
 * Represents a portion of a specific food that is stored in the smart fridge.
 * When calling constructor, FoodCategory and foodName are NECESSARY fields.
 * The date fields are represented by strings for now, they are OPTIONAL as well as storage location.
 * For Jingjie: for now you can use the first constructor.
 * todo: If needed, make the class abstract and add children classes(only when necessary unique methods are needed).
 */
public class Food {
    protected FoodCategory category;
    protected String foodName;
    protected ExpiryDate expiryDate;
    protected FoodStorageLocation storageLocation;

    public Food(FoodCategory category, String foodName) {
        this.setCategory(category);
        this.setFoodName(foodName);
    }

    public Food(FoodCategory category, String foodName, String expiryString,
            FoodStorageLocation storageLocation) throws InvalidDateException {
        this.setCategory(category);
        this.setFoodName(foodName);
        this.setExpiryDate(expiryString);
        this.setStorageLocation(storageLocation);
    }

    @Override
    public String toString() {
        String format = "Food name: %1$s, category: %2$s, expiry: %3$s, stored in: %4$s";
        return String.format(
                format,
                getFoodName(),
                getCategory().name(), 
                getExpiryDate().toString(), 
                getStorageLocation().name());
    }

    public FoodCategory getCategory() {
        return category;
    }

    public void setCategory(FoodCategory category) {
        this.category = category;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public ExpiryDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryString) throws InvalidDateException {
        ExpiryDate expiryDate = new ExpiryDate(expiryString);
        this.expiryDate = expiryDate;
    }

    public FoodStorageLocation getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(FoodStorageLocation storageLocation) {
        this.storageLocation = storageLocation;
    }
}
