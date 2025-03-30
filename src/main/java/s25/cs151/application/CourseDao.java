package s25.cs151.application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CourseDao implements CourseDaoInt {
    public static final String FILE_NAME = "Courses.csv";

    @Override
    public void storeCourse(String courseCode, String courseName, String sectionNumber) {
        CourseBean newCourse = new CourseBean(courseCode, courseName, sectionNumber);

        // 1. Read existing courses
        List<CourseBean> courseList = getCourses();

        // 2. Check for duplicates based on courseCode + sectionNumber
        HashSet<String> courseKeySet = new HashSet<>();
        courseKeySet.add(courseCode + sectionNumber);
        for (CourseBean course : courseList) {
            String key = course.getCourseCode() + course.getSectionNumber();
            if (!courseKeySet.contains(key)) {
                courseKeySet.add(key);
            } else {
                throw new IllegalArgumentException("Course with the same code and section already exists.");
            }
        }

        // 3. Write to file
        try (FileWriter fw = new FileWriter(FILE_NAME, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(newCourse.getCourseCode() + "," + newCourse.getCourseName() + "," + newCourse.getSectionNumber());
            System.out.println("Successfully wrote course to CSV.");
        } catch (IOException e) {
            System.err.println("Error writing to Courses.csv: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<CourseBean> getCourses() {
        List<CourseBean> courseList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 3) {
                    String code = values[0];
                    String name = values[1];
                    String section = values[2];
                    courseList.add(new CourseBean(code, name, section));
                }
            }
        } catch (FileNotFoundException e) {
            // File may not exist yet - return empty list
        } catch (IOException e) {
            System.err.println("Error reading Courses.csv: " + e.getMessage());
            e.printStackTrace();
        }
        return courseList;
    }
}