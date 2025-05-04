package s25.cs151.application.MODEL;

import java.time.LocalDate;

/**
 * A bean class that represents an appointment/office hours schedule.
 * This class holds all the information needed for an appointment between
 * a faculty member and a student.
 */
public class AppointmentBean {
    private String studentName;      // Student's full name
    private LocalDate date;          // Date of the appointment
    private String timeSlot;         // Time slot (e.g. "9:30 AM - 9:45 AM")
    private String course;           // Course code and section (e.g. "CS151-04")
    private String reason;           // Optional reason for the appointment
    private String comment;          // Optional additional comments

    /**
     * Creates a new appointment with all required and optional information.
     * 
     * @param studentName The full name of the student
     * @param date The date of the appointment
     * @param timeSlot The time slot for the appointment
     * @param course The course code and section
     * @param reason The reason for the appointment (optional)
     * @param comment Additional comments (optional)
     */
    public AppointmentBean(String studentName, LocalDate date, String timeSlot, String course, String reason, String comment) {
        this.studentName = studentName;
        this.date = date;
        this.timeSlot = timeSlot;
        this.course = course;
        this.reason = reason;
        this.comment = comment;
    }

    // Getters and Setters
    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
} 