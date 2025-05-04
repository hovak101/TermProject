package s25.cs151.application.MODEL;

public class SemesterOfficeHourBean {
    private String semester;
    private int year;
    private String days;

    public SemesterOfficeHourBean(String semester, int year, String days) {
        this.semester = semester;
        this.year = year;
        this.days = days;
    }

    public SemesterOfficeHourBean(String semester, int year, boolean mon, boolean tues,
                                  boolean wed, boolean thurs, boolean fri) {
        this.semester = semester;
        this.year = year;
        this.days = new StringBuilder(5)
                .append(mon ? '1' : '0')
                .append(tues ? '1' : '0')
                .append(wed ? '1' : '0')
                .append(thurs ? '1' : '0')
                .append(fri ? '1' : '0')
                .toString();
    }

    // Getters
    public String getSemester() { return semester; }
    public int getYear() { return year; }
    public String getDays() { return days; }

    // Setters
    public void setSemester(String semester) { this.semester = semester; }
    public void setYear(int year) { this.year = year; }
    public void setDays(String days) { this.days = days; }
}
