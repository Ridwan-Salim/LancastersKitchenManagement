package interfaces;

import java.time.DayOfWeek;
import java.time.LocalDate;

public interface Booking {
    void getBookings(LocalDate date); // returns a 2D array of all the bookings for a given date
    void getBookingsFromDateLastYear(LocalDate date); // returns a 2D array of all the bookings for a given date from the previous year
    void getBookingsDayOfWeekAverage(DayOfWeek dayOfWeek); // returns an integer for the average number of bookings for a given day of the week from the previous year's data
    void getTableSize_Frequency(LocalDate date); // returns a table size - frequency hashmap for how many of each table size there was for a given day
}
