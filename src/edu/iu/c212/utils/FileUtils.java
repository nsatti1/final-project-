package edu.iu.c212.utils;

import edu.iu.c212.models.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.*;
import java.time.*;

public class FileUtils {

    //private static String user_dir = System.getProperty("user.dir");
    private static File inputFile = new File("src/edu/iu/c212/resources/input.txt");
    private static File outputFile = new File("src/edu/iu/c212/resources/output.txt");

    private static File inventoryFile = new File("src/edu/iu/c212/resources/inventory.txt");
    private static File staffFile = new File("src/edu/iu/c212/resources/staff.txt");
    private static File staffAvailabilityFile = new File("src/edu/iu/c212/resources/staff_availability_IN.txt");
    private static File shiftSchedulesFile = new File("src/edu/iu/c212/resources/shift_schedules_IN.txt");
    private static File storeScheduleFile = new File("src/edu/iu/c212/resources/store_schedule_OUT.txt");

    /**
     * Reads inventory data from the inventory file and returns a list of Item objects
     * @return list of Item objects representing the inventory
     * @throws IOException if an I/O error occurs while reading the inventory file
     */
    public static List<Item> readInventoryFromFile() throws IOException {
        //System.out.println(inventoryFile.toURI().getPath() + "\n" + inventoryFile.exists());
        // depending on your OS, toURI() may need to be used when working with paths
        List<Item> items = new ArrayList<>();

        if(inventoryFile.exists()){
            Scanner inventoryReader = new Scanner(inventoryFile);
            String header = inventoryReader.nextLine();         //skip the header row.
            while(inventoryReader.hasNextLine()){
                String[] row = inventoryReader.nextLine().split(",");
                //System.out.println("item name="+row[0]+ " cost="+row[1]+ " quality="+row[2]+ " aisle="+row[3]);
                items.add(new Item(row[0], Double.parseDouble(row[1]), Integer.parseInt(row[2]),
                        Integer.parseInt(row[3])));
            }
            inventoryReader.close();
        }
        return items;
    }

    /**
     * Reads shift schedule data from the inventory file and returns a list of Hours objects
     * @return list of Item objects representing the inventory
     * @throws IOException if an I/O error occurs while reading the shift schedule file
     */
    public static List<Hours> readHours() throws IOException{
        List<Hours> hours = new ArrayList<>();
        if(shiftSchedulesFile.exists()) {
            Scanner hoursReader = new Scanner(shiftSchedulesFile);
            //String header = hoursReader.nextLine();

            while (hoursReader.hasNextLine()){
                String[] row = hoursReader.nextLine().split(" ");
                hours.add(new Hours(row[0], Integer.parseInt(row[1]), Integer.parseInt(row[2]) ) );
            }
            hoursReader.close();
        }
        return hours;
    }

    /**
     * Reads staff data from the inventory file and returns a list of Item objects
     * @return list of Item objects representing the inventory
     * @throws IOException if an I/O error occurs while reading the inventory file
     */
    public static List<Staff> readStaffFromFile() throws IOException {
        List<Staff> staff = new ArrayList<>();

        if(staffAvailabilityFile.exists()){
            Scanner staffReader = new Scanner(staffAvailabilityFile);
            //String header = staffReader.nextLine(); // skip the header row
            while (staffReader.hasNextLine()){
                String st = staffReader.nextLine();
                String staffRegex = "^(\\S+\\s+\\S+)\\s+(\\d+)\\s+(\\S)(?:\\s+(\\S+(?:\\.\\S+)))?$";

                Pattern staffPattern = Pattern.compile(staffRegex);
                Matcher staffMatcher = staffPattern.matcher(st);
                if(staffMatcher.matches()){
                    //System.out.println("inside matcher IF...");
                    String name = staffMatcher.group(1);
                    int age = Integer.parseInt(staffMatcher.group(2));
                    String role = staffMatcher.group(3);
                    String availability = staffMatcher.group(4);
                    //System.out.println("Name="+name+ " Age="+age+ " Role="+role+ " Avail="+availability);
                    Staff emp = new Staff(name, age, role, availability);
                    staff.add(emp);
                }
            }
            staffReader.close();
        }
        return staff;
    }

    /**
     * writes the given list of Item objects to the inventory file
     * @param items the list of Item objects to be written to the inventory file
     * @throws IOException if an I/O error occurs while writing to the inventory file
     */
    public static void writeInventoryToFile(List<Item> items) throws IOException{
        //System.out.println(inventoryFile.toURI().getPath() + " file exists?"+inventoryFile.exists());
        if(inventoryFile.exists()) inventoryFile.delete();
        FileWriter fw = new FileWriter(inventoryFile, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("// itemName,itemCost,itemQuantity,itemAisle");
        bw.newLine();
        bw.flush();
        for(Item item : items) {
            bw.write(item.toString());
            bw.newLine();
            bw.flush();
        }
        bw.close();
        fw.close();
    }


    /**
     * writes the given list of staff objects to the staff availability file
     * @param employees the list of staff objects to be written to the staff availability file
     * @throws IOException if an I/O error occurs while writing to the staff availability file
     */
    public static void writeStaffToFile(List<Staff> employees) throws IOException {
        if (staffAvailabilityFile.exists()) staffAvailabilityFile.delete();
        FileWriter fw = new FileWriter(staffAvailabilityFile, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("//fullName,age,role,availability");
        bw.newLine();
        //bw.flush();
        for(Staff staff: employees){
            bw.write(staff.toString());
            bw.newLine();
            bw.flush();
        }
        bw.close();
        fw.close();
    }

    /**
     * writes the given list of Hours objects to the shift schedule file
     * @param storeHours the list of staff objects to be written to the shift schedule file
     * @throws IOException if an I/O error occurs while writing to the shift schedule file
     */
    public static void writeHoursToFile(List<Hours> storeHours) throws IOException {
        if (shiftSchedulesFile.exists()) shiftSchedulesFile.delete();
        FileWriter fw = new FileWriter(shiftSchedulesFile, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("//Store Hours");
        //bw.newLine();
        //bw.flush();
        for(Hours hour: storeHours){
            bw.newLine();
            bw.write(hour.toString());
            bw.flush();
        }
        bw.close();
        fw.close();
    }

    /**
     * reads commands from the input file
     * @return list of commands
     * @throws IOException if an I/O error occurs while reading the input file
     */
    public static List<String> readCommandsFromFile() throws IOException {
        //System.out.println("readCommandsfromfile " + inputFile.exists() + inputFile.toURI().getPath());
        List<String> lst = new ArrayList<>();
        if(inputFile.exists()){
            Scanner inputReader = new Scanner(inputFile);
            while(inputReader.hasNextLine()){
                lst.add(inputReader.nextLine());
            }
            inputReader.close();
        }
        return lst;
    }

    /**
     * writes the given list of objects to the shift schedule file
     * @param lines list of the schedule
     * @throws IOException if an I/O error occurs while reading the store schedule file
     */
    public static void writeStoreScheduleToFile(List<String> lines) throws IOException {
        List<String> schedList = new ArrayList<>();
        if(storeScheduleFile.exists()){
            storeScheduleFile.delete();
        }
        FileWriter fw = new FileWriter(storeScheduleFile, true);
        BufferedWriter bw = new BufferedWriter(fw);
        //find current date and time
        Calendar calendar = Calendar.getInstance();
        String date = calendar.get(Calendar.MONTH) + "/"+ calendar.get(Calendar.DAY_OF_MONTH) +
                "/" + calendar.get(Calendar.YEAR);
        String time = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
        bw.write("Created on " + date + " at " + time);
        bw.flush();
        //write schedule in output file
        for (String line : lines){
            bw.newLine();
            bw.write(line);
            bw.flush();
        }
        System.out.println("Schedule created.");
        bw.close();
        fw.close();

    }

    /**
     * write the outputs to the file
     * @param line the list of ouputs
     * @throws IOException if an I/O error occurs while reading the output file
     */
    public static void writeLineToOutputFile(String line) throws IOException {
        //System.out.println(outputFile.toURI().getPath() + " file exists?"+outputFile.exists());
        FileWriter fw = new FileWriter(outputFile, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(line);
        bw.newLine();
        bw.flush();
        bw.close();
        fw.close();
    }
}
