package s25.cs151.application;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDao implements AppointmentDaoInt {
    public static final String FILE_NAME = "Appointments.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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