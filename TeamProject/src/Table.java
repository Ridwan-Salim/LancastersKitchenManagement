public class Table {
    private int ID;
    private int capaticty;
    private boolean specialRequest;
    private String status;

    public Table(int ID, int capaticty, boolean specialRequest, String status) {
        this.ID = ID;
        this.capaticty = capaticty;
        this.specialRequest = specialRequest;
        this.status = status;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getCapaticty() {
        return capaticty;
    }

    public void setCapaticty(int capaticty) {
        this.capaticty = capaticty;
    }

    public boolean isSpecialRequest() {
        return specialRequest;
    }

    public void setSpecialRequest(boolean specialRequest) {
        this.specialRequest = specialRequest;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
