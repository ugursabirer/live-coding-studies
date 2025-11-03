# 📝 Öğrenilenler - Kurs Yönetim Sistemi

## Kritik Noktalar

### 1. İki Yönlü İlişki (Bidirectional Relationship)

Bu problemin en kritik kısmı! Student ve Course birbirine referans tutuyor.

**❌ YANLIŞ (Tek yönlü):**
```java
public void enrollStudent(Student student, Course course) {
    course.addStudent(student);  // Sadece course'a ekledik
    // Student'ın listesini unutmuşuz!
}
```

**✅ DOĞRU (İki yönlü):**
```java
public void enrollStudent(Student student, Course course) {
    course.addStudent(student);   // Course'a student ekle
    student.addCourse(course);    // Student'a course ekle
}
```

**Neden önemli?**
- Veri tutarlılığı
- Her iki taraftan da sorgu yapılabilir
- `student.getEnrolledCourses()` doğru sonuç verir
- `course.getEnrolledStudents()` doğru sonuç verir

### 2. Encapsulation (Kapsülleme)

**OOP'nin temel prensibi**: Data hiding + Controlled access

```java
public class Student {
    // ✅ Private fields - dışarıdan direkt erişilemiyor
    private int id;
    private String name;
    private String email;
    private List<Course> enrolledCourses;
    
    // ✅ Public getters - kontrollü okuma
    public int getId() { return id; }
    public String getName() { return name; }
    
    // ✅ Public methods - kontrollü işlem
    public void addCourse(Course course) {
        enrolledCourses.add(course);
    }
    
    // ❌ Setter yok - immutability (id değiştirilemez)
}
```

**Avantajları:**
- Data integrity korunur
- Validation eklenebilir
- Internal implementation değişebilir
- API stable kalır

### 3. Constructor'da Liste Initialize Etme

**❌ YANLIŞ:**
```java
public class Student {
    private List<Course> enrolledCourses;  // null!
    
    public Student(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        // enrolledCourses hala null!
    }
}

// Kullanımda NullPointerException!
student.addCourse(course);  // CRASH!
```

**✅ DOĞRU:**
```java
public Student(int id, String name, String email) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.enrolledCourses = new ArrayList<>();  // ✅ Initialize!
}
```

### 4. Helper Methods

Helper methods kodu daha okunabilir yapar.

**❌ YANLIŞ (Logic CourseManager'da):**
```java
public void enrollStudent(Student student, Course course) {
    if (course.getEnrolledStudents().size() >= course.getMaxCapacity()) {
        System.out.println("Dolu!");
        return;
    }
}
```

**✅ DOĞRU (Helper method):**
```java
// Course class'ında
public boolean isFull() {
    return enrolledStudents.size() >= maxCapacity;
}

// CourseManager'da
public void enrollStudent(Student student, Course course) {
    if (course.isFull()) {
        System.out.println("Dolu!");
        return;
    }
}
```

**Avantajları:**
- Okunabilirlik artar
- Reusable
- Test edilebilir
- Single responsibility

### 5. Guard Clauses (Erken Return)

**❌ YANLIŞ (Nested if):**
```java
public void enrollStudent(Student student, Course course) {
    if (student != null && course != null) {
        if (!student.isEnrolledIn(course)) {
            if (!course.isFull()) {
                // Kayıt yap
            } else {
                System.out.println("Dolu!");
            }
        } else {
            System.out.println("Zaten kayıtlı!");
        }
    } else {
        System.out.println("Null!");
    }
}
```

**✅ DOĞRU (Guard clauses):**
```java
public void enrollStudent(Student student, Course course) {
    // Guard clause 1
    if (student == null || course == null) {
        System.out.println("Null!");
        return;
    }
    
    // Guard clause 2
    if (student.isEnrolledIn(course)) {
        System.out.println("Zaten kayıtlı!");
        return;
    }
    
    // Guard clause 3
    if (course.isFull()) {
        System.out.println("Dolu!");
        return;
    }
    
    // Happy path
    course.addStudent(student);
    student.addCourse(course);
}
```

### 6. Collection Seçimi: List vs Set

**Bu problemde neden List?**

```java
private List<Course> enrolledCourses;  // ✅ List kullandık
```

**List kullanma sebepleri:**
- Sıralı tutmak istiyoruz (kayıt sırası önemli)
- Aynı öğrenci/kurs birden fazla kez eklenemez (business rule ile kontrol ediyoruz)
- Index-based erişim gerekebilir

**Set kullansaydık:**
```java
private Set<Course> enrolledCourses;  // Alternatif
```
- Otomatik duplicate önleme
- Sırasız
- equals/hashCode override gerekli

**Seçim kriteri:**
- Sıralama önemliyse → List
- Duplicate'leri otomatik engellemek istiyorsan → Set
- Her ikisi de lazımsa → LinkedHashSet

## İdeal Çözüm Yapısı

### Student.java
```java
import java.util.ArrayList;
import java.util.List;

public class Student {
    private int id;
    private String name;
    private String email;
    private List<Course> enrolledCourses;
    
    public Student(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.enrolledCourses = new ArrayList<>();
    }
    
    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public List<Course> getEnrolledCourses() { return enrolledCourses; }
    
    // Helper methods
    public boolean isEnrolledIn(Course course) {
        return enrolledCourses.contains(course);
    }
    
    public void addCourse(Course course) {
        enrolledCourses.add(course);
    }
}
```

### Course.java
```java
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
    
    // Getters
    public String getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
    public String getInstructor() { return instructor; }
    public int getMaxCapacity() { return maxCapacity; }
    public List<Student> getEnrolledStudents() { return enrolledStudents; }
    
    // Helper methods
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
```

### CourseManager.java
```java
import java.util.ArrayList;
import java.util.List;

public class CourseManager {
    private List<Course> courses;
    
    public CourseManager() {
        this.courses = new ArrayList<>();
    }
    
    public void enrollStudent(Student student, Course course) {
        // Guard clauses
        if (student == null || course == null) {
            System.out.println("❌ Hata: Student veya Course null olamaz!");
            return;
        }
        
        if (student.isEnrolledIn(course)) {
            System.out.println("❌ Hata: " + student.getName() + " zaten " + 
                course.getCourseName() + " kursuna kayıtlı!");
            return;
        }
        
        if (course.isFull()) {
            System.out.println("❌ Hata: " + course.getCourseName() + 
                " kursu dolu! (Kapasite: " + course.getMaxCapacity() + ")");
            return;
        }
        
        // Bidirectional relationship
        course.addStudent(student);
        student.addCourse(course);
        
        // Course'u manager'ın listesine ekle (eğer yoksa)
        if (!courses.contains(course)) {
            courses.add(course);
        }
        
        System.out.println("✅ Başarılı: " + student.getName() + 
            " -> " + course.getCourseName() + " kursuna kaydedildi!");
    }
    
    public List<Student> getStudentsByCourse(String courseId) {
        if (courseId == null) {
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
```

## Interview'da Bahsedilecekler

### 1. "İki yönlü ilişki kurdum"
```java
course.addStudent(student);
student.addCourse(course);
```
Veri tutarlılığı için kritik!

### 2. "Guard clause pattern kullandım"
Erken return ile nested if'lerden kaçındım.

### 3. "Helper methods ile kod daha okunabilir oldu"
```java
course.isFull()  // vs  course.getEnrolledStudents().size() >= course.getMaxCapacity()
```

### 4. "Encapsulation'a dikkat ettim"
Private fields, public methods.

### 5. "Constructor'da liste initialize ettim"
NullPointerException riski yok.

## Alternatif Yaklaşımlar

### 1. Stream API ile getMostPopularCourse

```java
public Course getMostPopularCourse() {
    return courses.stream()
        .max(Comparator.comparingInt(Course::getCurrentEnrollmentCount))
        .orElse(null);
}
```

### 2. Map ile Daha Hızlı Arama

```java
public class CourseManager {
    private Map<String, Course> coursesMap;  // courseId -> Course
    
    public List<Student> getStudentsByCourse(String courseId) {
        Course course = coursesMap.get(courseId);  // O(1) lookup
        return course != null ? course.getEnrolledStudents() : new ArrayList<>();
    }
}
```

### 3. Custom Exception Kullanımı

```java
public void enrollStudent(Student student, Course course) 
        throws CourseFullException, DuplicateEnrollmentException {
    
    if (student.isEnrolledIn(course)) {
        throw new DuplicateEnrollmentException(student, course);
    }
    
    if (course.isFull()) {
        throw new CourseFullException(course);
    }
    
    course.addStudent(student);
    student.addCourse(course);
}
```

## Yaygın Hatalar

### 1. ❌ İki yönlü ilişkiyi unutmak
```java
// Sadece course'a ekledik, student'a eklemeyi unuttuk!
course.addStudent(student);
```

### 2. ❌ Constructor'da initialize etmemek
```java
private List<Course> enrolledCourses;  // null!
// Constructor'da new ArrayList<>() yok
```

### 3. ❌ equals/hashCode override etmemek

`contains()` çalışmaz! Student/Course objeleri için:

```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Student student = (Student) o;
    return id == student.id;
}

@Override
public int hashCode() {
    return Objects.hash(id);
}
```

### 4. ❌ Null kontrolü yapmamak
```java
if (student == null || course == null) {
    return;
}
```

## Performance Notları

### List Operations
```java
list.add(element)        // O(1) - amortized
list.contains(element)   // O(n) - linear search
list.get(index)          // O(1) - index access
```

### Optimizasyon İçin
Eğer çok fazla arama yapılacaksa:
- `List<Student>` yerine `Set<Student>` kullan
- `contains()` → O(n)'den O(1)'e düşer

## Unit Test Örnekleri

```java
@Test
public void testSuccessfulEnrollment() {
    Student student = new Student(1, "Ahmet", "ahmet@mail.com");
    Course course = new Course("J101", "Java", "Ali Hoca", 2);
    CourseManager manager = new CourseManager();
    
    manager.enrollStudent(student, course);
    
    assertTrue(student.isEnrolledIn(course));
    assertTrue(course.hasStudent(student));
}

@Test
public void testCapacityLimit() {
    Course course = new Course("J101", "Java", "Ali Hoca", 1);
    Student s1 = new Student(1, "Ahmet", "ahmet@mail.com");
    Student s2 = new Student(2, "Ayşe", "ayse@mail.com");
    CourseManager manager = new CourseManager();
    
    manager.enrollStudent(s1, course);
    manager.enrollStudent(s2, course);  // Kapasite dolu
    
    assertEquals(1, course.getCurrentEnrollmentCount());
}

@Test
public void testDuplicateEnrollment() {
    Student student = new Student(1, "Ahmet", "ahmet@mail.com");
    Course course = new Course("J101", "Java", "Ali Hoca", 5);
    CourseManager manager = new CourseManager();
    
    manager.enrollStudent(student, course);
    manager.enrollStudent(student, course);  // Duplicate
    
    assertEquals(1, course.getCurrentEnrollmentCount());
}
```

## Hatırlatmalar

⚠️ **İki yönlü ilişki** - Hem course'a hem student'a ekle
⚠️ **Guard clauses** - Erken return kullan
⚠️ **Constructor'da initialize** - Liste null olmasın
⚠️ **Helper methods** - Kod okunabilirliği için
⚠️ **Null kontrolü** - Defensive programming

## Sonraki Adımlar

Bu problemi çözdükten sonra:
1. ✅ OOP prensiplerine hakimsin
2. ✅ Collections kullanabiliyorsun
3. ✅ İki yönlü ilişkileri yönetebiliyorsun
4. ➡️ Intermediate Problem 2'ye geç (Shopping Cart - HashMap kullanımı)