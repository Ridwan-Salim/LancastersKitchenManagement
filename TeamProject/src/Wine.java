public class Wine {
    private int ID;
    private String name;
    private String vintage;
    private int cellarQuantity;
    private int price;

    public Wine(int ID, String name, String vintage, int cellarQuantity, int price) {
        this.ID = ID;
        this.name = name;
        this.vintage = vintage;
        this.cellarQuantity = cellarQuantity;
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

    public String getVintage() {
        return vintage;
    }

    public void setVintage(String vintage) {
        this.vintage = vintage;
    }

    public int getCellarQuantity() {
        return cellarQuantity;
    }

    public void setCellarQuantity(int cellarQuantity) {
        this.cellarQuantity = cellarQuantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
