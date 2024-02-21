public class WineSales extends Sales{
    private Wine wine; // the actual wine we are tracking the sale of ? maybe make it an Array of wines sold for that day?
    // wine sales would be an object made in the wine class?
    public WineSales(int ID, int date, int quantity, Wine wine) {
        super(ID, date, quantity);
        this.wine = wine;
    }
}
