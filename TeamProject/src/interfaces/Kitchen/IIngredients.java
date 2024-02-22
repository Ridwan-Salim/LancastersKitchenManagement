package interfaces.Kitchen;

public interface IIngredients {
    void getOrder(int UNIXtimestamp_Date); // get a 2D array order list based on the date of order [ingredient, quantity]
    void getSupplierIngredients(int UNIXtimestamp_Month); // get a 2D array list of all available ingredients from the supplier each month [ingredient, quantity]
}
