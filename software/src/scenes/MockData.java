package scenes;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;


/**
 * Put this in main call to generate the data
 *         MockData mockData = new MockData();
 *         mockData.generateYearPredictions();
 * Then you can call the public Maps / Lists directly
 * 
 * 
 * FOR TABLE DATA @ARTEM Functions start at line 121
 * Main:
 *         MockData mockData = new MockData();
 *         mockData.generateTablePredictions();
 *         mockData.printAllTablePredictions();
 * */

public class MockData {
    public Map<String, List<String>> menuData = new HashMap<>();  // Dish -> Ingredients list
    public Map<String, Integer> ingredients = new HashMap<>(); // Ingredients -> Price

    public Map<String,  List<List<String>>> bookings = new HashMap<>(); // Everyday -> booking predictions

    public HashMap<String, List<Integer>> popularity = new HashMap<>(); // Every Dish -> popularity
    public List<Integer> popularityData = new ArrayList<>();

    public Map<String, Map<Integer, List<List<String>>>> tablePrediction = new HashMap<>();

    public void addIngredients(){
        ingredients.put("Cheddar Cheese", 7);
        ingredients.put("Carrots", 3);
        ingredients.put("Potato", 3);
        ingredients.put("Onion", 3);
        ingredients.put("Garlic", 5);
        ingredients.put("Ginger", 27);
        ingredients.put("Tomato Puree", 13);
        ingredients.put("Cinnamon Powder", 7);
        ingredients.put("Salad Dressing", 3);
        ingredients.put("Chicken Thigh", 3);
        ingredients.put("Mushrooms", 3);
        ingredients.put("Penne Pasta", 5);
        ingredients.put("Salmon Fillets", 27);
        ingredients.put("Lemon Juice", 13);

        // Add more
    }

    public void addDishWithIngredients(String dishName, String... ingredients) {
        List<String> validIngredients = new ArrayList<>();
        for (String ingredient : ingredients) {
            if (this.ingredients.containsKey(ingredient)) {
                validIngredients.add(ingredient);
            } else {
                System.out.println("Ingredient " + ingredient + " is not available.");
            }
        }
        menuData.put(dishName, validIngredients);
    }

    public List<List<String>> getBookingData(String date) {
        return bookings.get(date);
    }

    public String int4ToDate(int int4Timestamp) {
        int year = int4Timestamp / 10000;
        int month = (int4Timestamp % 10000) / 100;
        int day = int4Timestamp % 100;
        return String.format("%02d/%02d/%04d", day, month, year);
    }

    public List<List<String>> addPastYTDPrediction() {
        List<List<String>> pastYTDPredcition = new ArrayList<>();
        for (int hour = 17; hour < 23; hour++) {
            for (int minute = 0; minute < 60; minute += 30) {
                int bookings = new Random().nextInt(10) + 1;
                int averageBill = new Random().nextInt(91) + 10;

                String timeSlot = String.format("%02d:%02d-%02d:%02d", hour, minute, hour, minute + 30);
                ArrayList<String> pastYTDTimeslotPredcition = new ArrayList<>(Arrays.asList(String.valueOf(bookings), timeSlot, String.valueOf(averageBill)));
                pastYTDPredcition.add(pastYTDTimeslotPredcition);
            }
        }
        return pastYTDPredcition;
    }

    // This function was generated using ChatGPT to handle the date int correction
    public void generateYearPredictions() {
        LocalDate startDate = LocalDate.of(2023, Month.JANUARY, 1);
        LocalDate endDate = LocalDate.of(2023, Month.DECEMBER, 31);

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            int dateInt4 = date.getYear() * 10000 + date.getMonthValue() * 100 + date.getDayOfMonth();
            List<List<String>> dayPrediction = addPastYTDPrediction();
            String dateString = int4ToDate(dateInt4);
            bookings.put(dateString, dayPrediction);
        }
    }

    // This function was generated using ChatGPT to print out the data
    public void printAllBookingData() {
        List<Map.Entry<String, List<List<String>>>> sortedEntries = new ArrayList<>(bookings.entrySet());

        Collections.sort(sortedEntries, Comparator.comparing(Map.Entry::getKey));

        for (Map.Entry<String, List<List<String>>> entry : sortedEntries) {
            String date = entry.getKey();
            List<List<String>> predictions = entry.getValue();

            System.out.println("Date: " + date);
            for (List<String> prediction : predictions) {
                System.out.println("Time Slot Prediction: " + prediction);
            }
            System.out.println();
        }
    }


// ===============================================================================
    public void generateTablePredictions(){
        LocalDate startDate = LocalDate.of(2023, Month.JANUARY, 1);
        LocalDate endDate = LocalDate.of(2023, Month.DECEMBER, 31);

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            int dateInt4 = date.getYear() * 10000 + date.getMonthValue() * 100 + date.getDayOfMonth();
            Map<Integer, List<List<String>>> tablePredicitions = tablePredictions();
            String dateString = int4ToDate(dateInt4);
            this.tablePrediction.put(dateString, tablePredicitions);
        }
    }

    public Map<Integer, List<List<String>>> tablePredictions(){
        Map<Integer, List<List<String>>> eachDayTablePredictions = new HashMap<>();
        for (int i = 1; i < 31; i++){
            List<List<String>> eachSlotTablePredictions = new ArrayList<>();
            for (int hour = 17; hour < 23; hour++) {
                for (int minute = 0; minute < 60; minute += 30) {
                    Random random = new Random();
                    boolean randomBoolean = random.nextBoolean();
                    String isOccupied = Boolean.toString(randomBoolean);

                    String timeSlot = String.format("%02d:%02d-%02d:%02d", hour, minute, hour, minute + 30);
                    ArrayList<String> eachSlotInfo = new ArrayList<>(Arrays.asList(timeSlot, String.valueOf(randomBoolean)));
                    eachSlotTablePredictions.add(eachSlotInfo);
                }
                eachDayTablePredictions.put(i, eachSlotTablePredictions);
            }
        }
        return eachDayTablePredictions;
    }

    // This function was generated using ChatGPT to print out the data
    public void printAllTablePredictions() {
        List<String> sortedDates = new ArrayList<>(tablePrediction.keySet());
        Collections.sort(sortedDates); // Sort the dates

        // Iterate over each date
        for (String date : sortedDates) {
            Map<Integer, List<List<String>>> tablePredictions = tablePrediction.get(date);

            System.out.println("Date: " + date);
            // Iterate over each table prediction for the date
            for (int tableNumber : tablePredictions.keySet()) {
                List<List<String>> tablePrediction = tablePredictions.get(tableNumber);

                System.out.println("Table Number: " + tableNumber);
                // Print each time slot prediction for the table
                for (int slotIndex = 0; slotIndex < tablePrediction.size(); slotIndex++) {
                    List<String> timeSlotInfo = tablePrediction.get(slotIndex);
                    String timeSlot = timeSlotInfo.get(0);
                    String isOccupied = timeSlotInfo.get(1);
                    System.out.println("Time Slot: " + timeSlot + ", Is Occupied: " + isOccupied);
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    /*public static void main(String[] args) {
        MockData mockData = new MockData();
        mockData.generateTablePredictions();
        mockData.printAllTablePredictions();
    }*/
}

