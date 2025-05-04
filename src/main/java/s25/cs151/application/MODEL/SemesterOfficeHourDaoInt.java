package s25.cs151.application.MODEL;

import java.util.List;

public interface SemesterOfficeHourDaoInt {
    List<SemesterOfficeHourBean> getSemesterOfficeHours();
    void storeSemesterOfficeHours(String semester, int year, boolean mon, boolean tues,
                                                boolean wed, boolean thurs, boolean fri);
    void storeSemesterOfficeHours(List<SemesterOfficeHourBean> beans);
}
