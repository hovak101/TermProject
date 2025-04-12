package s25.cs151.application;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentDaoInt {
    List<AppointmentBean> getAppointments();
    void storeAppointment(String studentName, LocalDate date, String timeSlot, String course, String reason, String comment);
    void storeAppointments(List<AppointmentBean> appointments);
} 