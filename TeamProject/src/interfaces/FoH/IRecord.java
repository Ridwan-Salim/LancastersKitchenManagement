package interfaces.FoH;

import java.util.ArrayList;

public interface IRecord {
    void saveReceipt(int UNIXtimestamp_BookingTime, int UNIXtimestamp_PaymentTime, int numberOfDiners, ArrayList<Integer> itemsPurchased, int tipAmount, int serviceCharge); // saves a receipt to the Management database
}
