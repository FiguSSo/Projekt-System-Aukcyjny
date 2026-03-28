package com.sms.service;

import com.sms.model.Student;

import java.util.ArrayList;

public interface StudentManager
{
    void addStudent(Student student);

    void removeStudent(String studentID);

    Student getStudentById(String studentID);

    void updateStudent(Student student);

    ArrayList<Student> displayAllStudents();

    double calculateAverageGrade();
}
