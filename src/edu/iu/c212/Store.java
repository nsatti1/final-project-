package edu.iu.c212;

import edu.iu.c212.models.Hours;
import edu.iu.c212.models.Item;
import edu.iu.c212.models.Staff;
import edu.iu.c212.programs.SawPrimePlanks;
import edu.iu.c212.programs.StaffScheduler;
import edu.iu.c212.utils.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static edu.iu.c212.utils.FileUtils.readCommandsFromFile;
import static edu.iu.c212.utils.FileUtils.readInventoryFromFile;

public class Store implements IStore {
    //should also call takeAction() and run through the input file
    private List<Item> items;
    private List<Staff> staff;
    private List<Hours> hours;
    public Store(){
        takeAction();
    }
    //saves Items from File

    /**
     * saves items to the file
     */
    public void saveItemsFromFile() {
        try {
            FileUtils.writeInventoryToFile(this.items);
        }catch (Exception e){
            System.exit(0);
        }
    }
    /**
     * saves staff to the file
     */
    public void saveStaffFromFile(){
        try {
            FileUtils.writeStaffToFile(this.staff);
        }catch (Exception e){
            System.exit(0);
        }

    }
    /**
     * saves hours to the file
     */
    public void saveHoursFromFile(){
        try {
            FileUtils.writeHoursToFile(this.hours);
        }catch (Exception e){
            System.exit(0);
        }

    }

    /**
     * finding the cost of items
     * @param itemName is the name of the item
     * @return string response
     */
    public String findCost(String itemName){
        //empty string response
        String response = "";
        //iterate through items
        for(Item item : this.items){
            //check name of item and see if it matches itemName
            if(item.getName().equalsIgnoreCase(itemName)) {
                //if there is a match then append the price of the item to response
                response += item.getPrice();
                break;
            }
        }
        //return response with appended price of item
        return response;
    }
    /**
     * getting the items from File
     * @return items list of Items
     */
    @Override
    public List<Item> getItemsFromFile(){
        try{
            this.items = FileUtils.readInventoryFromFile();
        } catch (IOException e){
            System.out.println("Problem occurred...");
            System.exit(0);
        }
        return this.items;
    }

    /**
     * getting staff from file
     * @return staff list of Staff
     */
    @Override
    public List<Staff> getStaffFromFile(){
        try{
            this.staff = FileUtils.readStaffFromFile();
        } catch (IOException e){
            System.out.println("Problem occurred...");
            System.exit(0);
        }
        return this.staff;
    }

    /**
     * getting hours from file
     * @return hours list of Hours
     */

    public List<Hours> getHoursFromFile(){
        try{
            this.hours = FileUtils.readHours();
        } catch (IOException e){
            System.out.println("Problem occurred...");
            System.exit(0);
        }
        return this.hours;
    }

    /**
     * This loads in the inventory and staff information, reads from the input file and takes
     * the correct actions, then finally asks the user to hit Enter to end the program when
     * they’re finished.
     */

    @Override
    public void takeAction(){

        //Commands can be interpreted within Store’s takeAction() method. Use the method from
        //FileUtils you write to get a List of String s, where each String is one line/command from
        //the input. Then, for each String, determine which command it is attempting to use and its
        //arguments and call the appropriate method in your code
        try{
            this.items = FileUtils.readInventoryFromFile();
            System.out.println("Completed loading Inventory into memory...");
            this.staff = FileUtils.readStaffFromFile();
            System.out.println("Completed loading Staff Availability into memory...");


            List<String> commands = FileUtils.readCommandsFromFile();
            for(String command : commands){
                //System.out.println("COMMAND=="+command);
                boolean result = takeActionHelper(command);
            }

        } catch (IOException e){
            System.out.println("Problem occurred...");
            System.exit(0);
        }
    }

    /**
     * reading commands
     * @param cmd string with the command
     * @return boolean (true if command is found, false if it isn't)
     * @throws IOException if an I/O error occurs while reading the input file
     */

    private boolean takeActionHelper(String cmd) throws IOException{
        boolean r = false;
        int index = cmd.indexOf(" ");
        index = (index==-1 ? cmd.length() : index);
        String c = cmd.substring(0,index);
        //System.out.println("cmd="+cmd);
        switch (c) {
            case "ADD":
                //executing ADD item
               // System.out.println("Executing ADD command.");
                String regex_ADD = "ADD\\s+‘([^‘’]+)’\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)";
                Pattern pattern_ADD = Pattern.compile(regex_ADD);
                Matcher matcher_ADD = pattern_ADD.matcher(cmd);
                if(matcher_ADD.matches()){
                    String name = matcher_ADD.group(1);
                    double price = Double.parseDouble(matcher_ADD.group(2));
                    int qty = Integer.parseInt(matcher_ADD.group(3));
                    int aisleNum = Integer.parseInt(matcher_ADD.group(4));
                    Item item = new Item("'"+name+"'", price, qty, aisleNum);
                    //System.out.println("ADDing item... "+item.toString());
                    this.items.add(item);   //Add to the memory
                    saveItemsFromFile();    //Save Items to the Inventory File
                    FileUtils.writeLineToOutputFile(name+" was added to inventory");
                }
                break;
            case "COST":
                // executing cost
                //System.out.println("Executing COST command.");
                String response = "";
                String regex_COST = "^COST\\s+‘([^’]+)’$";
                Pattern pattern_COST = Pattern.compile(regex_COST);
                Matcher matcher_COST = pattern_COST.matcher(cmd);
                if(matcher_COST.matches()){
                    String itemName = matcher_COST.group(1);
                    //System.out.println("Item Name="+itemName);
                    String resp = findCost("'"+itemName+"'");
                    double ret = Double.parseDouble(resp);
                    FileUtils.writeLineToOutputFile(itemName+": $"+String.format("%.0f",ret));
                }
                break;
            case "EXIT":
                //execute exit
                FileUtils.writeLineToOutputFile("Thank you for visiting High's Hardware and Gardening!");
                System.out.println("Press enter to continue...");
                try {
                    System.in.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.exit(0);
                break;
            case "FIND":
                //execute find
                //System.out.println("Executing FIND command. ");
                String itemName="";
                String res_find = "";
                String regex_FIND = "FIND\\s+(?:‘|')([^‘’']+)(?:’|')";
                Pattern pattern_FIND = Pattern.compile(regex_FIND);
                Matcher matcher_FIND = pattern_FIND.matcher(cmd);
                if(matcher_FIND.matches()){
                    itemName = matcher_FIND.group(1);
                    //System.out.println("FIND..... item name="+itemName);
                    for(Item item : this.items){
                        //FileUtils.writeLineToOutputFile("inside this.items");
                        //if(item.getName().equalsIgnoreCase("'"+itemName+"'")) {
                        if(item.getName().equalsIgnoreCase("'"+itemName+"'")) {
                            res_find = item.getQuantity()+" "+item.getName()+" are available in aisle "+item.getAisle();
                            break;
                        }
                    }
                    if(res_find=="")
                        res_find="ERROR: "+itemName + " cannot be found";
                    FileUtils.writeLineToOutputFile(res_find);
                }
                break;
            case "FIRE":
                //execute fire
                //remove staff from avail file, print
                //find index of where the staff member is in the array list
                //remove that index place from list
                //System.out.println("Executing FIRE command. ");
                String regex_FIRE = "^FIRE\\s+‘([^‘’]+)’$";
                Pattern firePattern = Pattern.compile(regex_FIRE);
                Matcher fireMatcher = firePattern.matcher(cmd);
                boolean flag = false;
                String staffName = "";
                if(fireMatcher.matches()){
                    staffName = fireMatcher.group(1);
                    //System.out.println("FIRE emp="+staffName);
                    for (Staff elem : this.staff) {
                        if (elem.getName().equalsIgnoreCase(fireMatcher.group(1))) {
                            this.staff.remove(elem);
                            FileUtils.writeStaffToFile(this.staff);
                            FileUtils.writeLineToOutputFile(elem.getName()+" was fired");
                            flag = true;
                            break; // Break out of the loop once the target name is found
                        }
                    }
                }
                if(!flag){
                    FileUtils.writeLineToOutputFile("ERROR: " + staffName + " cannot be found.");
                }
                break;
            case "HIRE":
                // execute hire
                //if we use a space delimiter?
                //and put the first two elements into a String
                //the rest are separate
                //then make them as a new staff
                //System.out.println("Executing HIRE command.");
                String regex_HIRE = "^HIRE\\s+'(\\S+\\s+\\S+)'\\s+(\\d+)\\s+(\\S)(?:\\s+(\\S+(?:\\.\\S+)))?$";
                Pattern hirePattern = Pattern.compile(regex_HIRE);
                Matcher hireMatcher = hirePattern.matcher(cmd);
                String fullRole = "";
                if(hireMatcher.matches()){
                    //System.out.println("inside matcher IF...");
                    String name = hireMatcher.group(1);
                    int age = Integer.parseInt(hireMatcher.group(2));
                    String role = hireMatcher.group(3);
                    switch (role) {
                        case "M":
                            fullRole = "Manager";
                            break;
                        case "C":
                            fullRole = "Cashier";
                            break;
                        case "G":
                            fullRole = "Gardening Expert";
                            break;
                    }
                    String availability = hireMatcher.group(4);
                    //System.out.println("Name="+name+ " Age="+age+ " Role="+role+ " Avail="+availability);
                    Staff emp = new Staff(name, age, role, availability);
                    this.staff.add(emp);
                    FileUtils.writeStaffToFile(this.staff);
                    FileUtils.writeLineToOutputFile(name + " has been hired as a " + fullRole);
                }
                break;
            case "PROMOTE":
                // execute promote
                // execute promote
                //System.out.println("Executing PROMOTE command. ");
                String res_promote = "";
                String regex_PROMOTE = "^PROMOTE\\s+'([^'']+)'\\s+(\\S)$";
                Pattern pattern_PROMOTE = Pattern.compile(regex_PROMOTE);
                Matcher matcher_PROMOTE = pattern_PROMOTE.matcher(cmd);
                if(matcher_PROMOTE.matches()){
                 String name = matcher_PROMOTE.group(1);
                 String role = matcher_PROMOTE.group(2);
                 List<Staff> newStaff = new ArrayList<>();
                 boolean check = false;
                 String res_PROMOTE = "";
                 //System.out.println("name="+ name + role);
                 for(Staff emp: this.staff){
                     if(emp.getName().equalsIgnoreCase(name)){
                         emp.setRole(role);
                         String fullRole1 = "";
                         switch (role) {
                             case "M":
                                 fullRole1 = "Manager";
                                 break;
                             case "C":
                                 fullRole1 = "Cashier";
                                 break;
                             case "G":
                                 fullRole1 = "Gardening Expert";
                                 break;
                         }
                         res_PROMOTE = name + " was promoted to " + fullRole1;
                         break;
                     }
                 }
                 if(res_PROMOTE=="")
                        res_PROMOTE="ERROR: "+ name + " could not be promoted";
                    else
                        saveStaffFromFile();
                    FileUtils.writeLineToOutputFile(res_PROMOTE);
                }
                break;
            case "SAW":
                //execute saw
                //System.out.println("Executing SAW command.");
                List<Item> newItems = new ArrayList<>();
                List<Integer> lst = new ArrayList<>();
                boolean found = false;
                for(Item item : this.items){
                    //System.out.print("Item Name="+item.getName()+ " try to saw");
                    String pname = item.getName();
                    boolean ind = pname.startsWith("'Plank-");
                    //System.out.println(" IND="+ind);
                    if(ind) {
                        int plen = Integer.parseInt(pname.substring(pname.indexOf('-')+1, pname.length()-1));
                        List<Integer> ls = SawPrimePlanks.getPlankLengths(plen);

                        int length = ls.getFirst();
                        //System.out.println("SIZE="+ls.size());
                        String new_name = "Plank-" + length;
                        double new_price = length * length;
                        int new_quantity = ls.size() * item.getQuantity();
                        int new_aisle = item.getAisle();
                        Item it = new Item("'" + new_name + "'", new_price, new_quantity, new_aisle);
                        newItems.add(it);
                        found = true;
                    } else {
                        newItems.add(item);
                    }
                }
                if(found){
                    this.items.clear();
                    this.items = newItems;
                    saveItemsFromFile();
                    FileUtils.writeLineToOutputFile("Planks sawed.");
                }
                break;
            case "SCHEDULE":
                //execute schedule
                //System.out.println("Executing SCHEDULE command. ");
                //call StaffScheduler
                StaffScheduler staffSched = new StaffScheduler();
                staffSched.scheduleStaff(this.staff, this.hours);
                FileUtils.writeLineToOutputFile("Schedule created.");
                break;
            case "SELL":
                // execute sell
                //System.out.println("Executing SELL command.");
                String res_sell = "";
                String regex_SELL = "SELL\\s+‘([^‘’]+)’\\s+(\\d+)";
                Pattern pattern_SELL = Pattern.compile(regex_SELL);
                Matcher matcher_SELL = pattern_SELL.matcher(cmd);
                if(matcher_SELL.matches()) {
                    String name = matcher_SELL.group(1);
                    String qty = matcher_SELL.group(2);
                    int sold = Integer.parseInt(qty);
                    //System.out.println("Trying to sell--"+name+ " qty="+qty);
                    for(Item item : this.items){
                        //if(item.getName().equalsIgnoreCase("'"+itemName+"'")) {
                        //System.out.println("Item Name="+item.getName()+ " try to sell="+name);
                        if(item.getName().equalsIgnoreCase("'"+name+"'")) {
                            if (item.getQuantity() >= sold) {
                                int avl = item.getQuantity();
                                item.setQuantity(avl - sold);
                                res_sell = qty+ " "+ name+ " was sold";
                            }
                            break;
                        }
                    }
                    if(res_sell=="")
                        res_sell="ERROR: "+name + " could not be sold";
                    else
                        saveItemsFromFile();    //Save Items to the Inventory File
                    FileUtils.writeLineToOutputFile(res_sell);
                }
                break;
            case "QUANTITY":
                // execute quantity
                //System.out.println("Executing QUANTITY command. ");
                String res_qty = "";
                String regex_QTY = "QUANTITY\\s+(?:‘|')([^‘’']+)(?:’|')";
                Pattern pattern_QTY = Pattern.compile(regex_QTY);
                Matcher matcher_QTY = pattern_QTY.matcher(cmd);

                if(matcher_QTY.matches()){
                    String name_qty = matcher_QTY.group(1);
                    for(Item item : this.items){
                        //if(item.getName().equalsIgnoreCase("'"+itemName+"'")) {
                        if(item.getName().equalsIgnoreCase("'"+name_qty+"'")) {
                            res_qty = String.valueOf(item.getQuantity());
                            break;
                        }
                    }
                    if(res_qty=="")
                        res_qty="ERROR: "+name_qty + " cannot be found";
                    FileUtils.writeLineToOutputFile(res_qty);
                }
                break;
            default:
                //default
                System.out.println("Please enter a valid command...");
        }
        return r;
    }

}
