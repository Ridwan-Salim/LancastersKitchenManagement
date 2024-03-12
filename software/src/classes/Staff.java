package classes;

public class Staff {
    private int ID;
    private String name;
    private boolean isAvailable;
    private boolean isOnHoliday;
    private String availability;

    public Staff(int ID, String name, boolean isAvailable, boolean isOnHoliday, String availability) {
        this.ID = ID;
        this.name = name;
        this.isAvailable = isAvailable;
        this.isOnHoliday = isOnHoliday;
        this.availability = availability;
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

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public boolean isOnHoliday() {
        return isOnHoliday;
    }

    public void setOnHoliday(boolean onHoliday) {
        isOnHoliday = onHoliday;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}
