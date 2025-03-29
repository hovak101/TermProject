package s25.cs151.application;

import java.util.List;

public interface CourseDaoInt {
    void storeCourse(String courseCode, String courseName, int sectionNumber);
    List<CourseBean> getCourses();
}
