package s25.cs151.application;

import java.util.List;

public interface CoursesDAO {

        List<CoursesBean> getCourses();
        void storeSemesterOfficeHours(String course);
        void storeSemesterOfficeHours(List<CoursesBean> beans);
}
