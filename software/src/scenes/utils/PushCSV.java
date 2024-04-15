package scenes.utils;

import core.DBConnect;

import java.io.BufferedReader;
import java.io.FileReader;

public class PushCSV {

    private String csvFile = "shifts.csv";
    private String csvSplitBy = ";";

    public boolean pushCSVToDatabase(){

        String line = "";

        try{
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null){
                String[] data = line.split(csvSplitBy);


            }

            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
