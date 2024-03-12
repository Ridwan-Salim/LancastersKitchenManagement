package interfaces.FoH;

public interface ICustomer {
    void getCustomerLimitPerHalfHour(int UNIXtimestamp_24HRtime); // returns the maximum number of customers that can dine at the restaurant in the given half hour, which is changed periodically by management, but is usually 6
}
