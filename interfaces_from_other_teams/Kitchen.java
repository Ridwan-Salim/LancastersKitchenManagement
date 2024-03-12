public interface Kitchen {

    void getDishMenu(); // gets a collection of all Dishes on the menu // Fine

    void getDrinkMenu(); // gets a collection of all Drinks on the menu
    
    // Each of these return only a collection of the class, and each class has its own fixed number of attributes
    // This will likely be an ArrayList<Array[]>
    // i.e. Wine wouldnt have the same properties as Dish

    // Finalised attributes for each class TBD

    void checkAllCurrentStock(); // returns a collection of all items and their quantities, including quantity=0

    // This will likely be a HashMap<item, quantity>
    // Should work for all items that arent made in the kitchen
    // i.e. Things like ingredients, drinks and wines

    void checkItemStock(Object item); // returns the quantity of a specific item, including quantity=0
    
    // The param passes any kind of Object
    // i.e. any ingredient, drink or wine object
    // And Kitchen can test this
}
