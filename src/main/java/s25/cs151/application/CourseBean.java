package s25.cs151.application;

public class CourseBean {
    private String courseCode;
    private String courseName;
    private String sectionNumber;

    public CourseBean(String courseCode, String courseName, String sectionNumber) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.sectionNumber = sectionNumber;
    }

    // Getters
    public String getCourseCode() { return courseCode; }
    public String getCourseName() { return courseName; }
    public String getSectionNumber() { return sectionNumber; }

    // Setters
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public void setSectionNumber(String sectionNumber) { this.sectionNumber = sectionNumber; }
}
