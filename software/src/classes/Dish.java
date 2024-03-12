package classes;

public class Dish {
    private int ID;
    private String name;
    private String description;
    private int price;
    private float percentageMarkup;
    private String allergenInfo;
    private String category;

    public Dish(int ID, String name, String description, int price, float percentageMarkup, String allergenInfo, String category) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.price = price;
        this.percentageMarkup = percentageMarkup;
        this.allergenInfo = allergenInfo;
        this.category = category;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public float getPercentageMarkup() {
        return percentageMarkup;
    }

    public void setPercentageMarkup(float percentageMarkup) {
        this.percentageMarkup = percentageMarkup;
    }

    public String getAllergenInfo() {
        return allergenInfo;
    }

    public void setAllergenInfo(String allergenInfo) {
        this.allergenInfo = allergenInfo;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
