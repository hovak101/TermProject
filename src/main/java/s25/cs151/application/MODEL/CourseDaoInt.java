package s25.cs151.application.MODEL;

import java.util.List;

public interface CourseDaoInt {
    void storeCourse(String courseCode, String courseName, String sectionNumber);
    List<CourseBean> getCourses();
}
