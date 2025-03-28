package s25.cs151.application;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.io.FileReader;

public class SemesterOfficeHourDao implements SemesterOfficeHourDaoInt {
    public static final String FILE_NAME = "OfficeHours.csv";

    @Override
    public List<SemesterOfficeHourBean> getSemesterOfficeHours() {
        List<SemesterOfficeHourBean> officeHoursList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 3) {
                    int year = Integer.parseInt(values[0]);
                    String semester = values[1];
                    String days = values[2];
                    officeHoursList.add(new SemesterOfficeHourBean(semester, year, days));
                }
            }
        }
        catch (IOException e) {
            System.err.println("Error reading office hours file: " + e.getMessage());
            e.printStackTrace();
        }
        return officeHoursList;
    }

    @Override
    public void storeSemesterOfficeHours(String semester, int year, boolean mon, boolean tues,
                                         boolean wed, boolean thurs, boolean fri) {
        SemesterOfficeHourBean bean = new SemesterOfficeHourBean(semester, year, mon,
                tues, wed, thurs, fri);
        // Check that no duplicates exist
        // 1. get list of beans
        List<SemesterOfficeHourBean> officeHours = getSemesterOfficeHours();

        // 2. check to see if semester-year combo already exists
        HashSet<String> semesterYearSet = new HashSet<>();
        semesterYearSet.add(bean.getSemester() + bean.getYear());
        for (SemesterOfficeHourBean beanInList : officeHours) {
            String semesterYear = beanInList.getSemester() + beanInList.getYear();
            if (!semesterYearSet.contains(semesterYear)) {
                semesterYearSet.add(semesterYear);
            }
            else {
                throw new IllegalArgumentException("Semester and Year already exists.");
            }
        }
        try (FileWriter fw = new FileWriter(FILE_NAME, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(bean.getYear() + "," + bean.getSemester() + "," + bean.getDays());
            System.out.println("Successfully wrote to CSV file");
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void storeSemesterOfficeHours(List<SemesterOfficeHourBean> officeHoursList) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("OfficeHours.csv", false))) {
            for (SemesterOfficeHourBean bean : officeHoursList) {
                String days = bean.getDays();
                pw.println(bean.getYear() + "," + bean.getSemester() + "," + days);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
