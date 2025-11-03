package intermediateLevel.problem01CourseManagement;

import java.util.*;

public class CourseManager {
    private List<Course> courses;

    public CourseManager() {
        this.courses = new ArrayList<>();
    }

    public void enrollStudent(Student student, Course course) {
        if (student == null || course == null) {
            System.out.println("Hata: second.Student veya second.Course null olamaz!");
            return;
        }

        if (student.isEnrolledIn(course)) {
            System.out.println("Hata: " + student.getName() + " zaten " + course.getCourseName() + " kursuna kayıtlı!");
            return;
        }

        if (course.isFull()) {
            System.out.println("Hata: " + course.getCourseName() + " kursu dolu! (Kapasite: " + course.getMaxCapacity() + ")");
            return;
        }

        course.addStudent(student);
        student.addCourse(course);

        if (!courses.contains(course)) {
            courses.add(course);
        }

        System.out.println("Başarılı: " + student.getName() + " -> " + course.getCourseName() + " kursuna kaydedildi!");
    }

    public List<Student> getStudentsByCourse(String courseId) {
        if (courseId == null || courseId.isEmpty()) {
            return new ArrayList<>();
        }

        for (Course course : courses) {
            if (course.getCourseId().equals(courseId)) {
                return course.getEnrolledStudents();
            }
        }

        return new ArrayList<>();
    }

    public Course getMostPopularCourse() {
        if (courses.isEmpty()) {
            return null;
        }

        Course mostPopular = courses.get(0);

        for (Course course : courses) {
            if (course.getCurrentEnrollmentCount() > mostPopular.getCurrentEnrollmentCount()) {
                mostPopular = course;
            }
        }

        return mostPopular;
    }
}