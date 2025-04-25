package s25.cs151.application;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface for data access operations related to appointments/office hours.
 * Defines methods for retrieving and storing appointment data.
 */
public interface AppointmentDaoInt {
    /**
     * Retrieves all appointments from the data source.
     * 
     * @return List of AppointmentBean objects representing all stored appointments
     */
    List<AppointmentBean> getAppointments();
    
    /**
     * Stores a single appointment in the data source.
     * 
     * @param studentName The full name of the student
     * @param date The date of the appointment
     * @param timeSlot The time slot for the appointment
     * @param course The course code and section
     * @param reason The reason for the appointment (optional)
     * @param comment Additional comments (optional)
     */
    void storeAppointment(String studentName, LocalDate date, String timeSlot, String course, String reason, String comment);
    
    /**
     * Stores multiple appointments in the data source, overwriting any existing data.
     * 
     * @param appointments List of AppointmentBean objects to store
     */
    void storeAppointments(List<AppointmentBean> appointments);
} 