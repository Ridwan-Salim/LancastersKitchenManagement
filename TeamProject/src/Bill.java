public class Bill {
    private int ID;
    private int date;
    private int sum;

    public Bill(int ID, int date, int sum) {
        this.ID = ID;
        this.date = date;
        this.sum = sum;
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

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}
