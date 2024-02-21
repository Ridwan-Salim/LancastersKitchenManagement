public class Order {
    private int ID;
    private int date;
    private String status;
    private String supplier;

    public Order(int ID, int date, String status, String supplier) {
        this.ID = ID;
        this.date = date;
        this.status = status;
        this.supplier = supplier;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
}
