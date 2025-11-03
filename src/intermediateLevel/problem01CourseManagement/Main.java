package intermediateLevel.problem01CourseManagement;

public class Main {
    public static void main(String[] args) {
        Student s1 = new Student(1, "Ahmet", "ahmet@mail.com");
        Student s2 = new Student(2, "Ayşe", "ayse@mail.com");
        Student s3 = new Student(3, "Mehmet", "mehmet@mail.com");

        Course java = new Course("J101", "Java Fundamentals", "Ali Hoca", 2);
        Course python = new Course("P101", "Python Basics", "Veli Hoca", 3);

        CourseManager manager = new CourseManager();

        manager.enrollStudent(s1, java);
        manager.enrollStudent(s2, java);
        manager.enrollStudent(s3, java);

        manager.enrollStudent(s1, python);
        manager.enrollStudent(s1, python);

        System.out.println("\nEn popüler kurs: " + manager.getMostPopularCourse().getCourseName());
    }
}