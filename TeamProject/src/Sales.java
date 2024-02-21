public class Sales {
    private int ID;
    private int date;
    private int quantity;
    // Sales are daily sales made up of bills ?

    public Sales(int ID, int date, int quantity) {
        this.ID = ID;
        this.date = date;
        this.quantity = quantity;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
