package classes;

public class Menu {
    private int ID;
    private String name;
    private String description;
    private int dateCreated;
    private int dateValidFrom;
    private int dateValidTo;
    private String type;

    public Menu(int ID, String name, String description, int dateCreated, int dateValidFrom, int dateValidTo, String type) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.dateCreated = dateCreated;
        this.dateValidFrom = dateValidFrom;
        this.dateValidTo = dateValidTo;
        this.type = type;
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

    public int getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(int dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getDateValidFrom() {
        return dateValidFrom;
    }

    public void setDateValidFrom(int dateValidFrom) {
        this.dateValidFrom = dateValidFrom;
    }

    public int getDateValidTo() {
        return dateValidTo;
    }

    public void setDateValidTo(int dateValidTo) {
        this.dateValidTo = dateValidTo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
