package s25.cs151.application.MODEL;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the AppointmentDaoInt interface that stores and retrieves
 * appointment data from a CSV file.
 */
public class AppointmentDao implements AppointmentDaoInt {
    public static final String FILE_NAME = "Appointments.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Retrieves all appointments from the CSV file.
     * 
     * @return List of AppointmentBean objects created from CSV data
     */
    @Override
    public List<AppointmentBean> getAppointments() {
        List<AppointmentBean> appointmentsList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1); // -1 to handle empty fields
                if (values.length >= 6) {
                    String studentName = values[0];
                    LocalDate date = LocalDate.parse(values[1], DATE_FORMATTER);
                    String timeSlot = values[2];
                    String course = values[3];
                    String reason = values[4];
                    String comment = values[5];
                    appointmentsList.add(new AppointmentBean(studentName, date, timeSlot, course, reason, comment));
                }
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, return empty list
            System.out.println("Appointments file not found, returning empty list");
        } catch (IOException e) {
            System.err.println("Error reading appointments file: " + e.getMessage());
            e.printStackTrace();
        }
        return appointmentsList;
    }

    /**
     * Stores a single appointment in the CSV file.
     * Appends the new appointment to the existing file.
     * 
     * @param studentName The full name of the student
     * @param date The date of the appointment
     * @param timeSlot The time slot for the appointment
     * @param course The course code and section
     * @param reason The reason for the appointment (optional)
     * @param comment Additional comments (optional)
     */
    @Override
    public void storeAppointment(String studentName, LocalDate date, String timeSlot, String course, String reason, String comment) {
        try (FileWriter fw = new FileWriter(FILE_NAME, true);
             PrintWriter pw = new PrintWriter(fw)) {
            // Format: studentName,date,timeSlot,course,reason,comment
            pw.println(studentName + "," + 
                      date.format(DATE_FORMATTER) + "," + 
                      timeSlot + "," + 
                      course + "," + 
                      reason + "," + 
                      comment);
            System.out.println("Successfully wrote appointment to CSV file");
        } catch (IOException e) {
            System.err.println("Error writing to appointments CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Stores multiple appointments in the CSV file.
     * Overwrites any existing data in the file.
     * 
     * @param appointments List of AppointmentBean objects to store
     */
    @Override
    public void storeAppointments(List<AppointmentBean> appointments) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME, false))) {
            for (AppointmentBean bean : appointments) {
                pw.println(bean.getStudentName() + "," + 
                          bean.getDate().format(DATE_FORMATTER) + "," + 
                          bean.getTimeSlot() + "," + 
                          bean.getCourse() + "," + 
                          bean.getReason() + "," + 
                          bean.getComment());
            }
            System.out.println("Successfully wrote all appointments to CSV file");
        } catch (IOException e) {
            System.err.println("Error writing all appointments to CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 