package edu.iu.c212;

import edu.iu.c212.models.Hours;
import edu.iu.c212.models.Item;
import edu.iu.c212.models.Staff;

import java.util.List;

public interface IStore {
    List<Item> getItemsFromFile();
    List<Staff> getStaffFromFile();
    List<Hours> getHoursFromFile();
    void saveItemsFromFile();
    void saveStaffFromFile();
    void saveHoursFromFile();
    void takeAction();
}
