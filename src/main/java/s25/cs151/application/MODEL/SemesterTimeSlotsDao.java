package s25.cs151.application.MODEL;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SemesterTimeSlotsDao implements SemesterTimeSlotsDaoInt {
    public static final String FILE_NAME = "TimeSlots.csv";
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("h:mm a");

    @Override
    public void storeTimeSlot(String fromHour, String toHour) {
        List<String> timeSlots = generateTimeSlots(fromHour, toHour);

        // Read existing slots to avoid duplicates
        HashSet<String> existingSlots = new HashSet<>();
        for (SemesterTimeSlotsBean slot : getTimeSlots()) {
            existingSlots.add(slot.getFromHour() + "-" + slot.getToHour());
        }

        try (FileWriter fw = new FileWriter(FILE_NAME, true);  // true for append mode
             PrintWriter pw = new PrintWriter(fw)) {

            for (String slot : timeSlots) {
                if (!existingSlots.contains(slot)) {
                    pw.println(slot);
                    existingSlots.add(slot);  // Add to set to prevent duplicate writes
                } else {
                    System.out.println("Duplicate time slot skipped: " + slot);
                }
            }

            System.out.println("Successfully wrote time slots to CSV.");
        } catch (IOException e) {
            System.err.println("Error writing to TimeSlots.csv: " + e.getMessage());
            //e.printStackTrace();
        }
    }

    private List<String> generateTimeSlots(String fromHour, String toHour) {
        List<String> slots = new ArrayList<>();
        // Just add the exact time range selected
        slots.add(fromHour + "-" + toHour);
        return slots;
    }

    @Override
    public List<SemesterTimeSlotsBean> getTimeSlots() {
        List<SemesterTimeSlotsBean> slotList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split("-");
                if (values.length == 2) {
                    slotList.add(new SemesterTimeSlotsBean(values[0].trim(), values[1].trim()));
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