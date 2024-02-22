package interfaces;

public interface Record {
    <R> void saveReceipt(R receipt); // saves a receipt to the Management database
    <B> void saveBooking(B booking); // saves a booking to the Management database

    // Unknown params for saving
}
