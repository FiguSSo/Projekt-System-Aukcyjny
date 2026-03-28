package com.sms.service;

import com.sms.model.Student;

import java.sql.*;
import java.util.ArrayList;

public class StudentManagerImpl implements StudentManager {

    private final String dbUrl;

    //konstruktor
    public StudentManagerImpl(String dbUrl) {
        if (dbUrl == null || !dbUrl.startsWith("jdbc:sqlite:")) {
            throw new IllegalArgumentException("Adres bazy danych musi zaczynac sie od 'jdbc:sqlite:'");
        }
        this.dbUrl = dbUrl;
        initDatabase();
    }
    //ta metoda tworzy tabele "students" jeżeli jej nie ma w bazie danych
    private void initDatabase() {
        String sql = """
                CREATE TABLE IF NOT EXISTS students (
                    name TEXT NOT NULL,
                    age INTEGER NOT NULL,
                    grade REAL NOT NULL,
                    studentID TEXT PRIMARY KEY
                );
                """;
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Tworzenie bazy danych nie powiodla sie: " + e.getMessage(), e);
        }
    }

    //metoda dodająca studenta do tabeli
    @Override
    public void addStudent(Student student) {
        String sql = "INSERT INTO students(name, age, grade, studentID) VALUES(?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(dbUrl)) {

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, student.getName());
            ps.setInt(2, student.getAge());
            ps.setDouble(3, student.getGrade());
            ps.setString(4, student.getStudentID());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Dodanie studenta nie powiodlo sie: " + e.getMessage(), e);
        }
    }

    //metoda usuwająca studenta do tabeli
    @Override
    public void removeStudent(String studentID) {
        String sql = "DELETE FROM students WHERE studentID = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl)) {

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, studentID);

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new IllegalArgumentException("Nie znaleziono studenta o ID: " + studentID);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Usuniecie studenta nie powiodlo sie: " + e.getMessage(), e);
        }
    }

    //metoda "wyciągająca" dane studenta z tabeli
    @Override
    public Student getStudentById(String studentID) {
        String sql = "SELECT name, age, grade, studentID FROM students WHERE studentID = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl)) {

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, studentID);

            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new IllegalArgumentException("Nie znaleziono studenta o ID: " + studentID);
            }

            return new Student(
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("studentID"),
                    rs.getDouble("grade")
            );

        } catch (SQLException e) {
            throw new RuntimeException("Odczyt studenta nie powiodl sie: " + e.getMessage(), e);
        }
    }

    //metoda aktualizująca studenta w tabeli
    @Override
    public void updateStudent(Student student) {
        String sql = "UPDATE students SET name=?, age=?, grade=? WHERE studentID=?";

        try (Connection conn = DriverManager.getConnection(dbUrl)) {

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, student.getName());
            ps.setInt(2, student.getAge());
            ps.setDouble(3, student.getGrade());
            ps.setString(4, student.getStudentID());

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new IllegalArgumentException(
                        "Nie znaleziono studenta o ID: " + student.getStudentID()
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Aktualizacja studenta nie powiodla sie: " + e.getMessage(), e);
        }
    }

    //metoda wyświetlająca wszystkich studentów
    @Override
    public ArrayList<Student> displayAllStudents() {
        ArrayList<Student> list = new ArrayList<>();
        String sql = "SELECT name, age, grade, studentID FROM students ORDER BY studentID";

        try (Connection conn = DriverManager.getConnection(dbUrl)) {

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Student(
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("studentID"),
                        rs.getDouble("grade")
                ));
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException("Odczyt studentow nie powiodl sie: " + e.getMessage(), e);
        }
    }

    //metoda obliczająca średnią ocen studentów
    @Override
    public double calculateAverageGrade() {
        String sql = "SELECT AVG(grade) AS avgGrade FROM students";

        try (Connection conn = DriverManager.getConnection(dbUrl)) {

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            double avg = rs.getDouble("avgGrade");
            if (rs.wasNull()) return 0.0;
            return avg;

        } catch (SQLException e) {
            throw new RuntimeException("Obliczanie sredniej ocen nie powiodlo sie: " + e.getMessage(), e);
        }
    }
}