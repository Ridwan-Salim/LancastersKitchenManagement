public class Booking {
    private int ID;
    private int date;
    private int time;
    private int partySize;
    private String customerName;
    private String customerContact;
    private String source;

    public Booking(int ID, int date, int time, int partySize, String customerName, String customerContact, String source) {
        this.ID = ID;
        this.date = date;
        this.time = time;
        this.partySize = partySize;
        this.customerName = customerName;
        this.customerContact = customerContact;
        this.source = source;
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

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getPartySize() {
        return partySize;
    }

    public void setPartySize(int partySize) {
        this.partySize = partySize;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
