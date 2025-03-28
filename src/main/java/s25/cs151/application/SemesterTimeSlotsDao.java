package s25.cs151.application;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SemesterTimeSlotsDao implements SemesterTimeSlotsDaoInt {
    public static final String FILE_NAME = "TimeSlots.csv";

    @Override
    public void storeTimeSlot(String fromHour, String toHour) {
        SemesterTimeSlotsBean newSlot = new SemesterTimeSlotsBean(fromHour, toHour);

        // 1. Read existing slots
        List<SemesterTimeSlotsBean> slotList = getTimeSlots();

        // 2. Check for duplicates (same from and to)
        HashSet<String> timeSlotSet = new HashSet<>();
        timeSlotSet.add(fromHour + toHour);
        for (SemesterTimeSlotsBean slot : slotList) {
            String key = slot.getFromHour() + slot.getToHour();
            if (!timeSlotSet.contains(key)) {
                timeSlotSet.add(key);
            } else {
                throw new IllegalArgumentException("Time slot already exists.");
            }
        }

        // 3. Write to file
        try (FileWriter fw = new FileWriter(FILE_NAME, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(newSlot.getFromHour() + "," + newSlot.getToHour());
            System.out.println("Successfully wrote time slot to CSV.");
        } catch (IOException e) {
            System.err.println("Error writing to TimeSlots.csv: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<SemesterTimeSlotsBean> getTimeSlots() {
        List<SemesterTimeSlotsBean> slotList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 2) {
                    String from = values[0];
                    String to = values[1];
                    slotList.add(new SemesterTimeSlotsBean(from, to));
                }
            }
        } catch (FileNotFoundException e) {
            // File may not exist yet — return empty list
        } catch (IOException e) {
            System.err.println("Error reading TimeSlots.csv: " + e.getMessage());
            e.printStackTrace();
        }
        return slotList;
    }
}
