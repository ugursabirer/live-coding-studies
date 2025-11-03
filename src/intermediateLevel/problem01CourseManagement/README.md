# 🎓 Kurs Yönetim Sistemi

## Problem Açıklaması

Bir online kurs platformu geliştiriyorsunuz. Öğrencilerin kurs kayıtlarını yönetecek bir sistem yapın.

## Gereksinimler

### Student Class
```java
- id (int, unique)
- name (String)
- email (String)
- enrolledCourses (List<Course>)
```

**Metodlar:**
- `isEnrolledIn(Course course)` - Öğrenci bu kursa kayıtlı mı?
- `addCourse(Course course)` - Kursu öğrencinin listesine ekle

### Course Class
```java
- courseId (String)
- courseName (String)
- instructor (String)
- maxCapacity (int)
- enrolledStudents (List<Student>)
```

**Metodlar:**
- `isFull()` - Kurs dolu mu?
- `hasStudent(Student student)` - Bu öğrenci kayıtlı mı?
- `addStudent(Student student)` - Öğrenciyi kursa ekle
- `getCurrentEnrollmentCount()` - Kaç öğrenci kayıtlı?

### CourseManager Class
```java
- courses (List<Course>)
```

**Metodlar:**
- `enrollStudent(Student student, Course course)` - Öğrenciyi kursa kaydet
- `getStudentsByCourse(String courseId)` - Kursa kayıtlı öğrencileri getir
- `getMostPopularCourse()` - En çok kayıtlı olan kursu döndür

## İş Kuralları

1. **Duplicate Prevention**: Bir öğrenci aynı kursa birden fazla kayıt olamaz
2. **Capacity Control**: Kurs kapasitesi doluysa kayıt yapılamaz
3. **Null Safety**: Null kontrollerini yapın
4. **Bidirectional Relationship**: Hem student'a course ekle, hem course'a student ekle

## Test Senaryosu

```java
Student s1 = new Student(1, "Ahmet", "ahmet@mail.com");
Student s2 = new Student(2, "Ayşe", "ayse@mail.com");
Student s3 = new Student(3, "Mehmet", "mehmet@mail.com");

Course java = new Course("J101", "Java Fundamentals", "Ali Hoca", 2);
Course python = new Course("P101", "Python Basics", "Veli Hoca", 3);

CourseManager manager = new CourseManager();

manager.enrollStudent(s1, java);    // ✅ Başarılı
manager.enrollStudent(s2, java);    // ✅ Başarılı
manager.enrollStudent(s3, java);    // ❌ Kapasite dolu

manager.enrollStudent(s1, python);  // ✅ Başarılı
manager.enrollStudent(s1, python);  // ❌ Duplicate

System.out.println(manager.getMostPopularCourse()); // Java döndürmeli (2 öğrenci)
```

## Öğrenilecek Konular

### OOP Concepts
- Encapsulation (private fields, public methods)
- Class relationships (Student ↔ Course)
- Helper methods
- Constructor design

### Collections
- ArrayList kullanımı
- List operations
- `contains()` metodu
- Collection iteration

### Design Patterns
- Guard clauses
- Validation pattern
- Two-way relationship management

### Best Practices
- Defensive programming
- Meaningful naming
- Single responsibility
- Clear error messages

## Zorluk Seviyesi

⭐⭐⭐ Orta

## Tahmini Süre

30-40 dakika

## Değerlendirme Kriterleri

1. **OOP Prensipleri**: Proper encapsulation ve class design
2. **Collection Seçimi**: Doğru collection kullanımı
3. **Business Logic**: İş kurallarının doğru implementasyonu
4. **Code Organization**: Temiz ve okunabilir kod
5. **Error Handling**: Uygun validasyon ve hata yönetimi

## İpuçları

### 1. Bidirectional Relationship
```java
// Her iki yönde de ekleme yapın
course.addStudent(student);
student.addCourse(course);
```

### 2. Guard Clauses
```java
if (student == null || course == null) {
    System.out.println("Hata!");
    return;
}
```

### 3. Helper Methods
```java
public boolean isFull() {
    return enrolledStudents.size() >= maxCapacity;
}
```

### 4. Meaningful Messages
```java
System.out.println("✅ Başarılı: " + student.getName() + " -> " + course.getCourseName());
```

## Bonus Görevler

- [ ] `unenrollStudent()` metodu ekle
- [ ] `getAvailableCourses()` metodu ekle (dolu olmayanlar)
- [ ] `getStudentCount()` metodu ekle
- [ ] Exception'lar kullan (Custom exceptions)
- [ ] Unit test'ler yaz

## Yaygın Hatalar

❌ İki yönlü ilişkiyi kurmayı unutmak
❌ Kapasite kontrolü yapmamak
❌ Duplicate kontrolü yapmamak
❌ Null kontrolü yapmamak
❌ Constructor'da liste initialize etmemeyi unutmak

## Interview İpuçları

Şunları vurgulayın:
- "İki yönlü ilişki kurdum - hem course'a hem student'a ekledim"
- "Guard clause pattern kullandım"
- "Helper methods ile kod daha okunabilir oldu"
- "ArrayList kullandım çünkü sıralı liste gerekiyordu"
- "Encapsulation'a dikkat ettim - private fields, public methods"

## Çıktı Örneği

```
✅ Hesap eklendi: Account[ACC001 - Ahmet: 10000.00 TL]
✅ Başarılı: Ahmet -> Java Fundamentals kursuna kaydedildi!
✅ Başarılı: Ayşe -> Java Fundamentals kursuna kaydedildi!
❌ Hata: Java Fundamentals kursu dolu! (Kapasite: 2)
✅ Başarılı: Ahmet -> Python Basics kursuna kaydedildi!
❌ Hata: Ahmet zaten Python Basics kursuna kayıtlı!

En popüler kurs: Java Fundamentals (2 öğrenci)
```