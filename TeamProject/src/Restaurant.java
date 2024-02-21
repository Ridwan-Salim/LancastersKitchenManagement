public class Restaurant {
    private String name;
    private int maxCapacity;
    private int workOpenTime;
    private int workCloseTime;
    private int customerOpenTime;
    private int customerCloseTime;
    private int dayCapacity;
    private int staffTotal;
    private int staffDay;

    public Restaurant(String name, int maxCapacity, int workOpenTime, int workCloseTime, int customerOpenTime, int customerCloseTime, int dayCapacity, int staffTotal, int staffDay) {
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.workOpenTime = workOpenTime;
        this.workCloseTime = workCloseTime;
        this.customerOpenTime = customerOpenTime;
        this.customerCloseTime = customerCloseTime;
        this.dayCapacity = dayCapacity;
        this.staffTotal = staffTotal;
        this.staffDay = staffDay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getWorkOpenTime() {
        return workOpenTime;
    }

    public void setWorkOpenTime(int workOpenTime) {
        this.workOpenTime = workOpenTime;
    }

    public int getWorkCloseTime() {
        return workCloseTime;
    }

    public void setWorkCloseTime(int workCloseTime) {
        this.workCloseTime = workCloseTime;
    }

    public int getCustomerOpenTime() {
        return customerOpenTime;
    }

    public void setCustomerOpenTime(int customerOpenTime) {
        this.customerOpenTime = customerOpenTime;
    }

    public int getCustomerCloseTime() {
        return customerCloseTime;
    }

    public void setCustomerCloseTime(int customerCloseTime) {
        this.customerCloseTime = customerCloseTime;
    }

    public int getDayCapacity() {
        return dayCapacity;
    }

    public void setDayCapacity(int dayCapacity) {
        this.dayCapacity = dayCapacity;
    }

    public int getStaffTotal() {
        return staffTotal;
    }

    public void setStaffTotal(int staffTotal) {
        this.staffTotal = staffTotal;
    }

    public int getStaffDay() {
        return staffDay;
    }

    public void setStaffDay(int staffDay) {
        this.staffDay = staffDay;
    }
}
