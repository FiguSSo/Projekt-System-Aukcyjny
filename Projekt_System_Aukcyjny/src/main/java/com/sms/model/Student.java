package com.sms.model;


//tworzymy klase Student która pozwoli na tworzenie obiektów opartych na tej klasie
// poniżej w kodzie można zobaczyc że "settery" nie sa używane(Student jest aktualizowany w innej klasie , przy użyciu "PreparedStatement"
// dlaczego wiec je zostawiłem? Na przyszłosc, jakbym pisał nowe klasy i ich metody, moga wtedy sie przydać

public class Student
{
    private String name;
    private int age;
    private double grade;
    private String studentID;


    public Student(String name, int age, String studentID, double grade) {
        this.name = name;
        this.age = age;
        this.studentID = studentID;
        this.grade = grade;
    }


    public String getStudentID()
    {
        return studentID;
    }

    public void setStudentID(String studentID)
    {
        if (studentID == null || studentID.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID nie może byc puste");
        }
        this.studentID = studentID.trim();
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name nie może być puste");
        }
        this.name = name.trim();
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        if (age <= 0) throw new IllegalArgumentException("Age musi być dodatnim int-em");
        this.age = age;
    }

    public double getGrade()
    {
        return grade;
    }

    public void setGrade(double grade)
    {
        if (grade < 0.0 || grade > 100.0) {
            throw new IllegalArgumentException("Grade musi być w przedziale 0.0 - 100.0.");
        }
        this.grade = grade;
    }

    @Override
    public String toString()
    {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", grade=" + grade +
                ", studentID='" + studentID + '\'' +
                '}';
    }
}
