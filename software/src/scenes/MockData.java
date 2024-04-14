package scenes;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * Put this in main call to generate the data
 *         MockData mockData = new MockData();
 *         mockData.generateYearPredictions();
 * Then you can call the public Maps / Lists directly
 *
 *
 * FOR TABLE DATA @ARTEM
 * Main:
 *         MockData mockData = new MockData();
 *         mockData.generateTablePredictions();
 *         mockData.printAllTablePredictions();
 * FOR BILL DATA @ARTEM
 * Main:
 *         MockData mockData = new MockData();
 *         mockData.addMenuData();
 *         mockData.createMenu();
 *         mockData.generateBills();
 *         mockData.printBills();
 * */

public class MockData {
    public Map<String, List<String>> menuData = new HashMap<>();  // Dish -> Ingredients list
    public Map<String, Double> ingredients = new HashMap<>(); // Ingredients -> Price
    public static Map<Integer, String[]> wines = new HashMap<>(); // WineID -> Wine Name Price Vintage
    public static Map<Integer, String[]> menu = new HashMap<>(); // ID -> DishName, DishPrice, Description, Allergens, Wines
    public static int dishInfoCapacity = 5;
    public Map<String, List<List<String>>> bookings = new HashMap<>(); // Everyday -> booking predictions

    public HashMap<String, List<Integer>> popularity = new HashMap<>(); // Every Dish -> popularity
    public List<Integer> popularityData = new ArrayList<>();

    public Map<String, Map<Integer, List<List<String>>>> tablePrediction = new HashMap<>();

    public Map<Integer, List<List<String>>> bill = new HashMap<>();

    public Random random = new Random();
    public int markup = random.nextInt(11) + 15; //random markup between 15-25

    public void addIngredients() {
        ingredients.put("Cheddar Cheese", 4.0);
        ingredients.put("Carrots", 1.0);
        ingredients.put("Potato", 1.0);
        ingredients.put("Onion", 1.0);
        ingredients.put("Garlic", 0.5);
        ingredients.put("Ginger", 2.0);
        ingredients.put("Tomato Puree", 2.0);
        ingredients.put("Cinnamon Powder", 2.0);
        ingredients.put("Salad Dressing", 2.0);
        ingredients.put("Chicken Thigh", 3.0);
        ingredients.put("Mushrooms", 2.0);
        ingredients.put("Penne Pasta", 1.5);
        ingredients.put("Salmon Fillets", 8.0);
        ingredients.put("Lemon Juice", 1.0);
        ingredients.put("Olive Oil", 5.0);
        ingredients.put("Butter", 2.0);
        ingredients.put("Salt", 0.5);
        ingredients.put("Black Pepper", 1.0);
        ingredients.put("Garlic Powder", 1.0);
        ingredients.put("Onion Powder", 1.0);
        ingredients.put("Paprika", 1.0);
        ingredients.put("Cumin", 2.0);
        ingredients.put("Coriander", 2.0);
        ingredients.put("Oregano", 1.0);
        ingredients.put("Thyme", 1.0);
        ingredients.put("Basil", 1.0);
        ingredients.put("Rosemary", 1.0);
        ingredients.put("Bay Leaves", 1.0);
        ingredients.put("Chili Powder", 1.0);
        ingredients.put("Cayenne Pepper", 1.0);
        ingredients.put("Crushed Red Pepper", 1.0);
        ingredients.put("Dried Parsley", 1.0);
        ingredients.put("Dried Basil", 1.0);
        ingredients.put("Dried Oregano", 1.0);
        ingredients.put("Dried Thyme", 1.0);
        ingredients.put("Dried Rosemary", 1.0);
        ingredients.put("Dried Bay Leaves", 1.0);
        ingredients.put("Ground Ginger", 2.0);
        ingredients.put("Ground Cinnamon", 2.0);
        ingredients.put("Ground Nutmeg", 2.0);
        ingredients.put("Ground Cloves", 2.0);
        ingredients.put("Ground Allspice", 2.0);
        ingredients.put("Ground Cardamom", 2.0);
        ingredients.put("Ground Turmeric", 2.0);
        ingredients.put("White Vinegar", 1.0);
        ingredients.put("Red Wine Vinegar", 2.0);
        ingredients.put("Balsamic Vinegar", 3.0);
        ingredients.put("Apple Cider Vinegar", 2.0);
        ingredients.put("Rice Vinegar", 1.5);
        ingredients.put("Soy Sauce", 2.0);
        ingredients.put("Worcestershire Sauce", 2.0);
        ingredients.put("Hot Sauce", 2.0);
        ingredients.put("Mustard", 1.5);
        ingredients.put("Ketchup", 1.5);
        ingredients.put("Mayonnaise", 2.0);
        ingredients.put("BBQ Sauce", 2.0);
        ingredients.put("Honey", 3.0);
        ingredients.put("Maple Syrup", 3.0);
        ingredients.put("Brown Sugar", 1.5);
        ingredients.put("White Sugar", 1.5);
        ingredients.put("Powdered Sugar", 2.0);
        ingredients.put("Molasses", 2.0);
        ingredients.put("Agave Syrup", 3.0);
        ingredients.put("Coconut Milk", 2.0);
        ingredients.put("Almond Milk", 2.0);
        ingredients.put("Oat Milk", 2.0);
        ingredients.put("Chicken Stock", 2.0);
        ingredients.put("Beef Stock", 2.0);
        ingredients.put("Vegetable Stock", 1.5);
        ingredients.put("Tomato Sauce", 1.5);
        ingredients.put("Tomato Paste", 1.5);
        ingredients.put("Crushed Tomatoes", 1.5);
        ingredients.put("Diced Tomatoes", 1.5);
        ingredients.put("Tomato Soup", 1.5);
        ingredients.put("Cream of Mushroom Soup", 2.0);
        ingredients.put("Cream of Chicken Soup", 2.0);
        ingredients.put("Cream of Celery Soup", 2.0);
        ingredients.put("Chicken Broth", 2.0);
        ingredients.put("Beef Broth", 2.0);
        ingredients.put("Vegetable Broth", 1.5);
        ingredients.put("Dijon Mustard", 2.0);
        ingredients.put("Whole Grain Mustard", 2.0);
        ingredients.put("Yellow Mustard", 1.5);
        ingredients.put("Hoisin Sauce", 2.0);
        ingredients.put("Fish Sauce", 2.0);
        ingredients.put("Miso Paste", 3.0);
        ingredients.put("Peanut Butter", 3.0);
        ingredients.put("Tahini", 3.0);
        ingredients.put("Sesame Oil", 3.0);
        ingredients.put("Coconut Oil", 3.0);
        ingredients.put("Fish Stock", 2.0);
        ingredients.put("Clam Juice", 2.0);
        ingredients.put("Capers", 3.0);
        ingredients.put("Anchovies", 3.0);
        ingredients.put("Sardines", 3.0);
        ingredients.put("Olives", 2.0);
        ingredients.put("Sun-Dried Tomatoes", 3.0);
        ingredients.put("Pickles", 2.0);
        ingredients.put("Sauerkraut", 2.0);
        ingredients.put("Roasted Red Peppers", 3.0);
        ingredients.put("Artichoke Hearts", 3.0);
        ingredients.put("Hearts of Palm", 3.0);
        ingredients.put("Cannellini Beans", 1.5);
        ingredients.put("Black Beans", 1.5);
        ingredients.put("Kidney Beans", 1.5);
        ingredients.put("Chickpeas", 1.5);
        ingredients.put("Lentils", 1.5);
        ingredients.put("Quinoa", 3.0);
        ingredients.put("Couscous", 2.0);
        ingredients.put("Bulgur", 2.0);
        ingredients.put("Farro", 2.0);
        ingredients.put("Arborio Rice", 2.0);
        ingredients.put("Jasmine Rice", 2.0);
        ingredients.put("Basmati Rice", 2.0);
    }
    public void addIngredientsFromList(String[] ingredientList) {
        for (String ingredient : ingredientList) {
            // Check if the ingredient already exists in the map
            if (ingredients.containsKey(ingredient)) {
                // If it exists, update its quantity
                double currentQuantity = ingredients.get(ingredient);
                ingredients.put(ingredient, currentQuantity + 1.0); // Increment by 1.0, you can change this as needed

    public static void addWines() {
        wines.put(1, new String[]{"Chardonnay", "15.0", "Description1", "2019", "20"});
        wines.put(2, new String[]{"Merlot", "12.0", "Description2", "2018", "25"});
        wines.put(3, new String[]{"Cabernet Sauvignon", "18.0", "Description3", "2017", "15"});
        wines.put(4, new String[]{"Pinot Noir", "20.0", "Description4", "2016", "30"});
        wines.put(5, new String[]{"Sauvignon Blanc", "14.0", "Description5", "2020", "18"});
        wines.put(6, new String[]{"Zinfandel", "16.0", "Description6", "2015", "22"});
        wines.put(7, new String[]{"Riesling", "17.0", "Description7", "2014", "28"});
        wines.put(8, new String[]{"Syrah", "22.0", "Description8", "2013", "12"});
        wines.put(9, new String[]{"Malbec", "19.0","Description9", "2012", "16"});
        wines.put(10, new String[]{"Rosé", "13.0", "Description10", "2021", "20"});
    }

    public void addDishWithIngredients(String dishName, String... ingredients) {
        List<String> validIngredients = new ArrayList<>();
        for (String ingredient : ingredients) {
            if (this.ingredients.containsKey(ingredient)) {
                validIngredients.add(ingredient);
            } else {
                Random random = new Random();
                double randomValue = 1 + (1.5) * random.nextDouble();
                this.ingredients.put(ingredient, randomValue);
                validIngredients.add(ingredient);
            }
        }
        menuData.put(dishName, validIngredients);
    }

    // Generated example dishes using ChatGPT
    public void addMenuData() {
        // American Dishes
        addDishWithIngredients("Cheeseburger", "Ground Beef", "Cheddar Cheese", "Lettuce", "Tomato", "Onion", "Pickles", "Ketchup", "Mustard", "Mayonnaise", "Hamburger Bun");
        addDishWithIngredients("Caesar Salad", "Romaine Lettuce", "Caesar Dressing", "Parmesan Cheese", "Croutons");
        addDishWithIngredients("Spaghetti and Meatballs", "Spaghetti Pasta", "Ground Beef", "Bread Crumbs", "Parmesan Cheese", "Marinara Sauce");
        addDishWithIngredients("Macaroni and Cheese", "Elbow Macaroni", "Cheddar Cheese", "Butter", "Milk");
        addDishWithIngredients("Buffalo Wings", "Chicken Wings", "Buffalo Sauce", "Blue Cheese Dressing", "Celery", "Carrots");

        // Italian Dishes
        addDishWithIngredients("Margherita Pizza", "Pizza Dough", "Tomato Sauce", "Fresh Mozzarella Cheese", "Fresh Basil");
        addDishWithIngredients("Chicken Alfredo", "Fettuccine Pasta", "Chicken Breast", "Heavy Cream", "Parmesan Cheese", "Garlic", "Butter");
        addDishWithIngredients("Lasagna", "Lasagna Noodles", "Ground Beef", "Italian Sausage", "Ricotta Cheese", "Mozzarella Cheese", "Marinara Sauce");
        addDishWithIngredients("Bruschetta", "Baguette", "Tomato", "Basil", "Garlic", "Balsamic Vinegar", "Olive Oil");
        addDishWithIngredients("Risotto", "Arborio Rice", "Chicken Broth", "Parmesan Cheese", "Onion", "White Wine", "Butter");

        // Asian Dishes
        addDishWithIngredients("Sushi Rolls", "Sushi Rice", "Nori", "Assorted Fish (Salmon, Tuna, etc.)", "Cucumber", "Avocado", "Soy Sauce", "Wasabi", "Pickled Ginger");
        addDishWithIngredients("Pad Thai", "Rice Noodles", "Chicken Breast", "Shrimp", "Tofu", "Bean Sprouts", "Egg", "Peanuts", "Green Onion", "Pad Thai Sauce");
        addDishWithIngredients("Beef Teriyaki", "Beef Steak", "Teriyaki Sauce", "Bell Pepper", "Onion", "Garlic", "Ginger", "Sesame Seeds");
        addDishWithIngredients("Chicken Curry", "Chicken Thigh", "Curry Paste", "Coconut Milk", "Potato", "Carrot", "Onion", "Garlic", "Ginger");
        addDishWithIngredients("Pho", "Beef Broth", "Rice Noodles", "Beef Slices", "Bean Sprouts", "Basil", "Lime", "Sriracha", "Hoisin Sauce");

        // Mexican Dishes
        addDishWithIngredients("Tacos", "Corn Tortillas", "Ground Beef", "Lettuce", "Tomato", "Cheddar Cheese", "Salsa", "Sour Cream", "Guacamole");
        addDishWithIngredients("Burritos", "Flour Tortillas", "Rice", "Black Beans", "Ground Beef", "Lettuce", "Tomato", "Cheddar Cheese", "Salsa");
        addDishWithIngredients("Enchiladas", "Corn Tortillas", "Shredded Chicken", "Enchilada Sauce", "Cheddar Cheese", "Black Olives", "Green Onion");
        addDishWithIngredients("Nachos", "Tortilla Chips", "Ground Beef", "Cheddar Cheese", "Black Beans", "Jalapenos", "Salsa", "Sour Cream", "Guacamole");
        addDishWithIngredients("Quesadillas", "Flour Tortillas", "Chicken Breast", "Cheddar Cheese", "Onion", "Bell Pepper", "Salsa", "Sour Cream");

        // Mediterranean Dishes
        addDishWithIngredients("Greek Salad", "Romaine Lettuce", "Tomato", "Cucumber", "Red Onion", "Kalamata Olives", "Feta Cheese", "Greek Dressing");
        addDishWithIngredients("Hummus and Pita", "Chickpeas", "Tahini", "Lemon Juice", "Garlic", "Olive Oil", "Pita Bread");
        addDishWithIngredients("Falafel", "Chickpeas", "Onion", "Garlic", "Parsley", "Cumin", "Coriander", "Tahini Sauce", "Pita Bread");
        addDishWithIngredients("Moussaka", "Eggplant", "Ground Lamb", "Tomato Sauce", "Bechamel Sauce", "Parmesan Cheese");
        addDishWithIngredients("Tabbouleh", "Bulgur Wheat", "Tomato", "Parsley", "Mint", "Lemon Juice", "Olive Oil");

        // Desserts
        addDishWithIngredients("Chocolate Cake", "Flour", "Sugar", "Cocoa Powder", "Eggs", "Butter", "Milk", "Vanilla Extract");
        addDishWithIngredients("Apple Pie", "Pie Crust", "Apples", "Sugar", "Cinnamon", "Butter", "Lemon Juice");
        addDishWithIngredients("Tiramisu", "Ladyfingers", "Espresso", "Mascarpone Cheese", "Egg", "Sugar", "Cocoa Powder");
        addDishWithIngredients("Cheesecake", "Graham Cracker Crust", "Cream Cheese", "Sugar", "Eggs", "Sour Cream", "Vanilla Extract");
        addDishWithIngredients("Ice Cream Sundae", "Vanilla Ice Cream", "Chocolate Syrup", "Whipped Cream", "Maraschino Cherry", "Sprinkles");

        // Breakfast Dishes
        addDishWithIngredients("Pancakes", "Flour", "Sugar", "Eggs", "Milk", "Butter", "Baking Powder", "Maple Syrup");
        addDishWithIngredients("French Toast", "Bread", "Eggs", "Milk", "Cinnamon", "Vanilla Extract", "Butter", "Maple Syrup");
        addDishWithIngredients("Omelette", "Eggs", "Cheese", "Onion", "Bell Pepper", "Mushrooms", "Tomato", "Spinach", "Ham");
        addDishWithIngredients("Eggs Benedict", "English Muffin", "Poached Egg", "Canadian Bacon", "Hollandaise Sauce");
        addDishWithIngredients("Breakfast Burrito", "Flour Tortilla", "Eggs", "Bacon", "Sausage", "Potato", "Cheddar Cheese", "Salsa");

        // Soups
        addDishWithIngredients("Chicken Noodle Soup", "Chicken Broth", "Chicken Breast", "Carrots", "Celery", "Egg Noodles", "Onion", "Garlic");
        addDishWithIngredients("Tomato Soup", "Tomato", "Chicken Broth", "Onion", "Garlic", "Cream", "Basil");
        addDishWithIngredients("Minestrone Soup", "Vegetable Broth", "Tomato", "Carrots", "Celery", "Onion", "Zucchini", "Kidney Beans", "Pasta", "Spinach");
        addDishWithIngredients("Clam Chowder", "Clam Juice", "Clams", "Potato", "Onion", "Celery", "Bacon", "Heavy Cream");
        addDishWithIngredients("Butternut Squash Soup", "Butternut Squash", "Onion", "Carrot", "Celery", "Chicken Broth", "Heavy Cream", "Nutmeg", "Cinnamon");

        // Sandwiches
        addDishWithIngredients("BLT Sandwich", "Bacon", "Lettuce", "Tomato", "Mayonnaise", "Bread");
        addDishWithIngredients("Club Sandwich", "Turkey", "Bacon", "Lettuce", "Tomato", "Mayonnaise", "Bread");
        addDishWithIngredients("Reuben Sandwich", "Corned Beef", "Sauerkraut", "Swiss Cheese", "Thousand Island Dressing", "Rye Bread");
        addDishWithIngredients("Grilled Cheese Sandwich", "Cheddar Cheese", "Bread", "Butter");
        addDishWithIngredients("Turkey Sandwich", "Turkey", "Lettuce", "Tomato", "Mayonnaise", "Bread");

        // Vegetarian Dishes
        addDishWithIngredients("Vegetable Stir-Fry", "Bell Pepper", "Broccoli", "Carrot", "Snap Peas", "Mushrooms", "Onion", "Garlic", "Ginger", "Soy Sauce");
        addDishWithIngredients("Quinoa Salad", "Quinoa", "Cucumber", "Tomato", "Bell Pepper", "Red Onion", "Feta Cheese", "Kalamata Olives", "Lemon Juice", "Olive Oil");
        addDishWithIngredients("Falafel Wrap", "Falafel", "Lettuce", "Tomato", "Cucumber", "Tahini Sauce", "Pita Bread");
        addDishWithIngredients("Vegetable Curry", "Potato", "Carrot", "Bell Pepper", "Broccoli", "Cauliflower", "Onion", "Garlic", "Ginger", "Curry Paste", "Coconut Milk");
        addDishWithIngredients("Caprese Salad", "Tomato", "Fresh Mozzarella Cheese", "Basil", "Balsamic Glaze", "Olive Oil");

        // Seafood Dishes
        addDishWithIngredients("Shrimp Scampi", "Shrimp", "Garlic", "Lemon Juice", "White Wine", "Parsley", "Butter", "Pasta");
        addDishWithIngredients("Salmon Salad", "Salmon Fillet", "Mixed Greens", "Tomato", "Cucumber", "Red Onion", "Balsamic Vinaigrette");
        addDishWithIngredients("Fish Tacos", "White Fish Fillet", "Corn Tortillas", "Cabbage", "Lime", "Cilantro", "Avocado", "Salsa");
        addDishWithIngredients("Crab Cakes", "Crab Meat", "Bread Crumbs", "Egg", "Mayonnaise", "Dijon Mustard", "Worcestershire Sauce", "Old Bay Seasoning");
        addDishWithIngredients("Lobster Bisque", "Lobster", "Heavy Cream", "Tomato Paste", "Sherry Wine", "Onion", "Garlic", "Thyme");

        // Vegan Dishes
        addDishWithIngredients("Vegan Buddha Bowl", "Quinoa", "Chickpeas", "Sweet Potato", "Broccoli", "Avocado", "Carrot", "Hummus", "Tahini Dressing");
        addDishWithIngredients("Vegan Chili", "Black Beans", "Kidney Beans", "Tomato", "Bell Pepper", "Onion", "Garlic", "Chili Powder", "Cumin", "Vegetable Broth");
        addDishWithIngredients("Vegan Stir-Fry", "Tofu", "Broccoli", "Bell Pepper", "Snap Peas", "Carrot", "Onion", "Garlic", "Ginger", "Soy Sauce");
        addDishWithIngredients("Vegan Pad Thai", "Rice Noodles", "Tofu", "Bean Sprouts", "Green Onion", "Peanuts", "Garlic", "Soy Sauce", "Tamarind Paste");
        addDishWithIngredients("Vegan Pizza", "Pizza Dough", "Tomato Sauce", "Vegan Cheese", "Assorted Vegetables (Mushrooms, Bell Pepper, Onion, etc.)", "Basil");
    }

    public void createMenu() {
        int id = 1;
        for (Map.Entry<String, List<String>> entry : menuData.entrySet()) {
            double price = 0;
            String[] dishItems = new String[dishInfoCapacity];
            for (String ingredient : entry.getValue()) {
                if (ingredients.containsKey(ingredient)) {
                    price += ingredients.get(ingredient);
                } else {
                    System.out.println("Ingredient '" + ingredient + "' not found.");
                }
            }
            //dishItems.add(Double.toString(price));
            dishItems[0] = entry.getKey();
            dishItems[1] = Double.toString(price);
            menu.put(id, dishItems);
            id++;
        }
    }

    public double getDishPrice(String dishName) {
        List<String> result = menuData.get(dishName);
        double price = 0;
        for (String ingredient : result) {
            if (ingredients.containsKey(ingredient)) {
                price += ingredients.get(ingredient);
            } else {
                System.out.println("Ingredient '" + ingredient + "' not found.");
            }
        }
        return price;
    }

    public static void printMenu(){
        for (Map.Entry<Integer, String[]> entry : menu.entrySet()) {
            Integer dishID = entry.getKey();
            String[] details = entry.getValue();
            System.out.println("ID: " + dishID);
            System.out.println("Dish: " + details[0]);
            System.out.println("Price: " + details[1]);
            System.out.println("Description: " + details[2]);
            System.out.println("Allergens: " + details[3]);
            System.out.println("Wines: " + details[4]);
            System.out.println();
        }
    }
//==================================================================================

    // timeslot generation made with ChatGPT
    public String generateRandomTimeSlot() {
        LocalTime startTime = generateRandomStartTime(); // Random start time
        int maxDurationMinutes = 120;                    // Maximum duration in minutes (2 hours)
        int slotDurationMinutes = 30;                    // Duration of each time slot in minutes

        // Generate a random number of slots within the maximum allowed
        Random random = new Random();
        int numberOfSlots = random.nextInt(maxDurationMinutes / slotDurationMinutes) + 1; // Add 1 to ensure at least one slot

        // Calculate the end time
        LocalTime endTime = startTime.plusMinutes(slotDurationMinutes * numberOfSlots);

        // If the duration exceeds 2 hours, adjust the end time
        if (startTime.plusMinutes(maxDurationMinutes).isBefore(endTime)) {
            endTime = startTime.plusMinutes(maxDurationMinutes);
        }

        // If the end time exceeds midnight, roll back to "23:30" of the same day
        if (endTime.isAfter(LocalTime.of(23, 30)) || endTime.getHour() == 0) {
            endTime = LocalTime.of(23, 30);
        }
        // Generate the time slot string
        String timeSlot = startTime.toString() + "-" + endTime.toString();
        return timeSlot;
    }

    // Random start time generated with ChatGPT
    public LocalTime generateRandomStartTime() {
        Random random = new Random();
        int hour = random.nextInt(6) + 17; // Random hour between 17 and 22
        int minute = random.nextInt(2) * 30; // Randomly select 0 or 30 for minutes
        return LocalTime.of(hour, minute);
    }

    // Random dish picker made with ChatGPT
    public List<String> getRandomDishNames(int count) {
        List<Integer> dishIDs = new ArrayList<>(menu.keySet()); // Get all dish IDs
        Collections.shuffle(dishIDs); // Shuffle the list of dish IDs
        List<String> randomDishNames = new ArrayList<>();
        for (int i = 0; i < Math.min(count, dishIDs.size()); i++) {
            int dishID = dishIDs.get(i);
            String[] dishInfo = menu.get(dishID);
            if (dishInfo != null && dishInfo.length > 0) {
                randomDishNames.add(dishInfo[0]); // Add the dish name to the list
            }
        }
        return randomDishNames; // Return a list of random dish names
    }

    // Random name picker made with ChatGPT
    private String getRandomName() {
        List<String> names = new ArrayList<>(Arrays.asList(
                "Emma", "Noah", "Olivia", "Liam", "Ava", "William", "Sophia",
                "James", "Isabella", "Oliver", "Mia", "Benjamin", "Charlotte",
                "Elijah", "Amelia"
        ));

        // Generate a random index
        Random random = new Random();
        int randomIndex = random.nextInt(names.size());
        // Get the name at the random index
        return names.get(randomIndex);
    }

    public void generateBills() {
        for (int i = 1; i < 101; i++) {
            int randomNumber = random.nextInt(6) + 1;
            List<String> dishNames = getRandomDishNames(randomNumber);
            List<String> billItems = new ArrayList<>();
            double totalBill = 0;

            for (String dishName : dishNames) {
                totalBill += getDishPrice(dishName);
            }
            double markupResult = totalBill * ((markup / 100d) + 1);
            totalBill += markupResult;

            int randomTip = random.nextInt(4) + 1;
            int result = randomTip * 5; // random number between 5 - 20 in increments of 5 for tip %
            double netBill = totalBill * ((result / 100d) + 1);

            String formattedTotalBill = String.format("%.2f", totalBill);
            String formattedNetBill = String.format("%.2f", netBill);

            // Generate random date in 2024
            LocalDate randomDate = LocalDate.of(2024, Month.JANUARY, 1)
                    .plusDays(random.nextInt(365)); // Add random days in the year

            // Generate random table number
            int tableNumber = random.nextInt(30) + 1;

            billItems.add(generateRandomTimeSlot());
            billItems.add(getRandomName());
            billItems.add(formattedTotalBill);
            billItems.add(Integer.toString(result));
            billItems.add(formattedNetBill);
            billItems.add(Integer.toString(tableNumber)); // Adding table number
            billItems.add(randomDate.toString()); // Adding random date

            List<List<String>> billInfo = new ArrayList<>();
            billInfo.add(dishNames);
            billInfo.add(billItems);

            bill.put(i, billInfo);
        }
    }

    // Printing bills partially generated with ChatGPT
    public void printBills() {
        for (Map.Entry<Integer, List<List<String>>> entry : bill.entrySet()) {
            System.out.println("Bill " + entry.getKey() + ":");
            List<List<String>> billInfo = entry.getValue();

            // Print dish names
            System.out.println("Dish Names:");
            List<String> dishNames = billInfo.get(0);
            for (String dishName : dishNames) {
                double markupResult = getDishPrice(dishName) * ((markup / 100d) + 1);
                String formattedDishPrice = String.format("%.2f", markupResult);
                System.out.println("- " + dishName + ": £" + formattedDishPrice);
            }

            // Print bill items including table number and date
            List<String> billItems = billInfo.get(1);
            System.out.println("Time Slot: " + billItems.get(0));
            System.out.println("Waiter Name: " + billItems.get(1));
            System.out.println("Total Bill: £" + billItems.get(2));
            System.out.println("Tip Amount: " + billItems.get(3) + "%");
            System.out.println("Net Bill: £" + billItems.get(4));
            System.out.println("Table Number: " + billItems.get(5)); // Printing table number
            System.out.println("Date: " + billItems.get(6)); // Printing date
            System.out.println();
        }
    }

    //==================================================================================
    public List<List<String>> getBookingData(String date) {
        return bookings.get(date);
    }

    public static String int4ToDate(int int4Timestamp) {
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
        LocalDate startDate = LocalDate.of(2024, Month.JANUARY, 1);
        LocalDate endDate = LocalDate.of(2024, Month.DECEMBER, 31);

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
        LocalDate startDate = LocalDate.of(2024, Month.JANUARY, 1);
        LocalDate endDate = LocalDate.of(2024, Month.DECEMBER, 31);

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
        // Convert dates to LocalDate objects
        List<LocalDate> sortedDates = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (String date : tablePrediction.keySet()) {
            LocalDate localDate = LocalDate.parse(date, formatter);
            sortedDates.add(localDate);
        }

        // Sort the dates
        Collections.sort(sortedDates);

        // Iterate over each sorted date
        for (LocalDate date : sortedDates) {
            String dateString = date.format(formatter);
            Map<Integer, List<List<String>>> tablePredictions = tablePrediction.get(dateString);

            System.out.println("Date: " + dateString);
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
    public Map<String, List<List<String>>> getYearPredictionsForMonth(String selectedMonth) {
        Map<String, List<List<String>>> yearPredictionsForMonth = new HashMap<>();

        LocalDate startDate = LocalDate.of(2024, Month.JANUARY, 1);
        LocalDate endDate = LocalDate.of(2024, Month.DECEMBER, 31);

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            int dateInt4 = date.getYear() * 10000 + date.getMonthValue() * 100 + date.getDayOfMonth();
            String dateString = int4ToDate(dateInt4);

            // Filter out predictions for the selected month
            String month = date.getMonth().toString();
            if (month.equalsIgnoreCase(selectedMonth)) {
                List<List<String>> predictions = bookings.get(dateString);
                if (predictions != null) {
                    yearPredictionsForMonth.put(dateString, predictions);
                }
            }
        }

        return yearPredictionsForMonth;
    }
    /*public static void main(String[] args) {
        MockData mockData = new MockData();

        mockData.addMenuData();
        mockData.createMenu();
        mockData.generateBills();
        mockData.printBills();
    }*/
}