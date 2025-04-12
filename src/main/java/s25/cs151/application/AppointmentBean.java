package s25.cs151.application;

import java.time.LocalDate;

public class AppointmentBean {
    private String studentName;
    private LocalDate date;
    private String timeSlot;
    private String course;
    private String reason;
    private String comment;

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