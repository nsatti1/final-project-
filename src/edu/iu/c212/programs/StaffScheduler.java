package edu.iu.c212.programs;
import edu.iu.c212.models.*;
import edu.iu.c212.utils.FileUtils;

import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

public class StaffScheduler {
    //new map for day of week vs list of available employees
    static HashMap<String, List<String>> empAvail = new HashMap<>();

    private List<Staff> staff;
    private List<Hours> hours;

    /**
     * finds store hours
     * @param hours list of Hours
     * @return list listOfHours
     */

    public static List<Hours> storeHours(List<Hours> hours) {
        List<Hours> listOfHours = new ArrayList<>();
        listOfHours = hours;
        return listOfHours;
    }
    static class StaffAvailability {
        String tenders;
        List<String> availability;

        StaffAvailability(String tenders, List<String> availability) {
            this.tenders = tenders;
            this.availability = availability;
        }
    }
    static class Shift {
        String days;
        String hours;

        Shift(String d, String h) {
            this.days = days;
            this.hours = hours;
        }
    }

    /**
     * will schedule staff based on their availability
     * @param staffList list of Staff objects
     * @param hoursList List of Hours
     * @throws IOException if an I/O error occurs while reading the store schedule file
     */

    public static void  scheduleStaff(List<Staff> staffList, List<Hours> hoursList) throws IOException {
        //Map<String, String> schedOpt = new HashMap<>();
        List<String> availableStaff = new ArrayList<>();


        //map of emp name and list of their availability
        Map<String, List<String>> namesAvail = new HashMap<>();

        //iterate through staff to get name and availability
        for(Staff exist : staffList){
            //save each name with the schedule belonging to that person
            String schedName = exist.getName();
            int spaceIndex = schedName.indexOf(" ");
            String firstPart = schedName.substring(0, spaceIndex + 1);
            char afterSpace = schedName.charAt(spaceIndex + 1);
            String abvName = firstPart + afterSpace;
            String avail = exist.getAvailability();
            //separate days in availability and make it populate a list of days
            String[] daysList = avail.split("\\.");
            //turn avail string into a list
            List<String> days = new ArrayList<>();
            for(String staffAvail : daysList){
                days.add(staffAvail);
            }
            //load name and avail into map
            namesAvail.put(abvName, days);
        }




        //average num of days per employee
        int totDays = namesAvail.values().stream().mapToInt(List::size).sum();
        int avgShiftNum = totDays/namesAvail.size();


        for(String day : namesAvail.values().stream().flatMap(Collection::stream).collect(Collectors.toSet())){
            List<String> openStaff = namesAvail.entrySet().stream()
                    .filter(e -> e.getValue().contains(day))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            Collections.shuffle(openStaff);
            empAvail.putIfAbsent(day, new ArrayList<>());
            int count = 0;
            while(!openStaff.isEmpty() && count < avgShiftNum){
                String name = "("+openStaff.remove(0)+")";
                empAvail.get(day).add(name);
                count++;
            }
        }


        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : empAvail.entrySet()) {
            String key = entry.getKey();
            List<String> values = entry.getValue();
            sb.append(key).append(" ").append(String.join(" ", values)).append("\n");
        }
        
        String listyidfk = sb.toString();
        List<String> finalSched = List.of(listyidfk.split("\n"));
        FileUtils.writeStoreScheduleToFile(finalSched);
    }
}
