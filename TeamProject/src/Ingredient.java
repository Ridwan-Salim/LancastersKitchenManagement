public class Ingredient {
    private int ID;
    private String name;
    private int quantity;
    private boolean isAllergen;
    private int price;

    public Ingredient(int ID, String name, int quantity, boolean isAllergen, int price) {
        this.ID = ID;
        this.name = name;
        this.quantity = quantity;
        this.isAllergen = isAllergen;
        this.price = price;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isAllergen() {
        return isAllergen;
    }

    public void setAllergen(boolean allergen) {
        isAllergen = allergen;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
