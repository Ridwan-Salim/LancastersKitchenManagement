package interfaces.FoH;

public interface IMenu {
    void getMenuData(int UNIXtimestamp_Date); // returns the menu for a certain day with descriptions etc, updated weekly
    void getMenuPDF(int UNIXtimestamp_Date); // returns a printable PDF of the menu, updated weekly

}
