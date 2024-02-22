package interfaces.FoH;

public interface IBooking {
    void getBookings(int UNIXtimestamp_Date); // returns a 2D array of all the bookings for a given date
    void getTableSize_FrequencyAverageForDayOfWeek(int UNIXtimestamp_Date); // returns a table size - frequency hashmap for how many of each table size there was for a given day from the entire previous year
    void getTableSize_Frequency(int UNIXtimestamp_Date); // returns a table size - frequency hashmap for how many of each table size there was for a given day
}
