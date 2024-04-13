package scenes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuotaChecker {

    public static Map<LocalDate, Map<String, List<String>>> checkQuotas(String shiftsFilePath, String quotaDirectoryPath) throws IOException {
        Map<LocalDate, Map<String, List<String>>> result = new HashMap<>();

        // Read shift information
        Map<LocalDate, Map<String, Map<String, Integer>>> shifts = readShifts(shiftsFilePath);

        // Read quota information from all files in the directory
        File quotaDirectory = new File(quotaDirectoryPath);
        File[] quotaFiles = quotaDirectory.listFiles();
        if (quotaFiles != null) {
            for (File quotaFile : quotaFiles) {
                if (quotaFile.isFile()) {
                    Map<LocalDate, Map<String, Map<String, Integer>>> quotas = readQuotas(quotaFile.toPath());
                    for (LocalDate date : quotas.keySet()) {
                        if (!result.containsKey(date)) {
                            result.put(date, new HashMap<>());
                        }
                        Map<String, List<String>> dailyResult = result.get(date);
                        Map<String, Map<String, Integer>> dailyQuota = quotas.get(date);
                        Map<String, Map<String, Integer>> shift = shifts.getOrDefault(date, new HashMap<>());
                        shift = shift(shift);
                        for (String timeSlot : dailyQuota.keySet()) {
                            Map<String, Integer> quotaHashMap = dailyQuota.get(timeSlot);
                            boolean isSatisfied;
                            if (shift.get(timeSlot) == null)
                                isSatisfied = false;
                            else
                                isSatisfied = shift.get(timeSlot).equals(quotaHashMap);
                            List<String> finalList = new ArrayList<>();
                            Map<String, Integer> diff = calculateDifference(shift.get(timeSlot), quotaHashMap);

                            StringBuilder valuesString = new StringBuilder();
                            for (Integer value : diff.values()) {
                                valuesString.append(value).append(", ");
                            }

                            String allValues = valuesString.length() > 2 ? valuesString.substring(0, valuesString.length() - 2) : "";
                            finalList.add(0, String.valueOf(isSatisfied));
                            finalList.add(1, allValues);
                            dailyResult.put(timeSlot, finalList);
                        }
                    }

                }
            }
        }

        return result;
    }
    public static Map<String, Integer> calculateDifference(Map<String, Integer> m1, Map<String, Integer>m2) {
        Map<String, Integer> difference = new HashMap<>();
        if (m1 == null)
            return m2;
        if (m2 == null)
            return m1;
        for (Map.Entry<String, Integer> entry : m2.entrySet()) {
            String key = entry.getKey();
            int value2 = entry.getValue();
            int value1 = m1.getOrDefault(key, 0);
            difference.put(key, value2 - value1);
        }

        return difference;
    }
    public static Map<String, Map<String, Integer>> shift(Map<String, Map<String, Integer>> originalMap) {
        Map<String, Map<String, Integer>> shiftedMap = new HashMap<>();

        for (Map.Entry<String, Map<String, Integer>> entry : originalMap.entrySet()) {
            String originalKey = entry.getKey();
            Map<String, Integer> value = entry.getValue();

            String[] parts = originalKey.split(":");
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);

            int newHour = hour;
            int newMinute = minute + 30;
            String newKey = newHour + ":" + newMinute;
            String finalKey = originalKey + "-" + newKey;
            shiftedMap.put(finalKey, value);
        }

        return shiftedMap;
    }


    private static Map<LocalDate, Map<String, Map<String, Integer>>> readQuotas(Path quotaFilePath) throws IOException {
        Map<LocalDate, Map<String, Map<String, Integer>>> quotas = new HashMap<>();
        Files.lines(quotaFilePath).forEach(line -> {
            String[] parts = line.split(";");
            LocalDate date = LocalDate.parse(parts[0], java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String timeSlot = parts[1];
            int numWaitersNeeded = Integer.parseInt(parts[2]);
            int numChefsNeeded = Integer.parseInt(parts[3]);
            int numManagersNeeded = Integer.parseInt(parts[4]);
            Map<String, Integer> quotaHashMap = new HashMap<>();
            quotaHashMap.put("waiter", numWaitersNeeded);
            quotaHashMap.put("chef", numChefsNeeded);
            quotaHashMap.put("manager", numManagersNeeded);

            if (!quotas.containsKey(date)) {
                quotas.put(date, new HashMap<>());
            }
            quotas.get(date).put(timeSlot, quotaHashMap);
        });
        return quotas;
    }

    private static Map<LocalDate, Map<String, Map<String, Integer>>> readShifts(String shiftsFilePath) throws IOException {
        Map<LocalDate, Map<String, Map<String, Integer>>> shifts = new HashMap<>();
        Files.lines(Path.of(shiftsFilePath)).forEach(line -> {
            String[] parts = line.split(";");
            LocalDate date = LocalDate.parse(parts[4]);
            String worker = parts[0].split(" ")[0];
            worker = worker.toLowerCase(); // Convert to lowercase
            if (!worker.equals("chef") && !worker.equals("manager"))
                worker = "waiter";
            String timeSlot = parts[2];
            shifts.putIfAbsent(date, new HashMap<>());
            shifts.get(date).putIfAbsent(timeSlot, new HashMap<>());
            shifts.get(date).get(timeSlot).merge(worker, 1, Integer::sum);
        });
        return shifts;
    }

    public static void main(String[] args) throws IOException {
        String shiftsFilePath = "D:\\homework\\Lancasters\\vpp\\LancastersKitchenMgmt\\software\\src\\scenes\\utils\\shifts.csv";
        String quotaDirectoryPath = "quotas"; // Replace with the actual directory path
        Map<LocalDate, Map<String, List<String>>> quotaStatus = checkQuotas(shiftsFilePath, quotaDirectoryPath);

    }
}
