package intermediateLevel.problem01CourseManagement;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private String courseId;
    private String courseName;
    private String instructor;
    private int maxCapacity;
    private List<Student> enrolledStudents;

    public Course(String courseId, String courseName, String instructor, int maxCapacity) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.instructor = instructor;
        this.maxCapacity = maxCapacity;
        this.enrolledStudents = new ArrayList<>();
    }

    public String getCourseId() {
        return courseId;
    }
    public String getCourseName() {
        return courseName;
    }
    public String getInstructor() {
        return instructor;
    }
    public int getMaxCapacity() {
        return maxCapacity;
    }
    public List<Student> getEnrolledStudents() {
        return enrolledStudents;
    }

    public int getCurrentEnrollmentCount() {
        return enrolledStudents.size();
    }

    public boolean isFull() {
        return enrolledStudents.size() >= maxCapacity;
    }

    public boolean hasStudent(Student student) {
        return enrolledStudents.contains(student);
    }

    public void addStudent(Student student) {
        enrolledStudents.add(student);
    }
}
