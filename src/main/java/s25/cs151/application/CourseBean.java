package s25.cs151.application;

public class CourseBean {
    private String courseCode;
    private String courseName;
    private int sectionNumber;

    public CourseBean(String courseCode, String courseName, int sectionNumber) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.sectionNumber = sectionNumber;
    }

    // Getters
    public String getCourseCode() { return courseCode; }
    public String getCourseName() { return courseName; }
    public int getSectionNumber() { return sectionNumber; }

    // Setters
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public void setSectionNumber(int sectionNumber) { this.sectionNumber = sectionNumber; }
}
