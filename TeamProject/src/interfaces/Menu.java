package interfaces;

import java.time.LocalDate;

public interface Menu {
    void getMenuData(); // returns the raw menu data for a certain day
    void getMenuPDF(); // returns a printable PDF of the menu that can be downloaded

}
