package com.sms.ui;

import com.sms.model.Student;
import com.sms.service.StudentManager;
import com.sms.service.StudentManagerImpl;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class StudentManagementApp extends JFrame {

    //tworzenie pól dla konkretnych wartości jakie będzie wprowadzał user

    private final JTextField idField = new JTextField(16);
    private final JTextField nameField = new JTextField(16);
    private final JTextField ageField = new JTextField(16);
    private final JTextField gradeField = new JTextField(16);

    private final JTextArea outputArea = new JTextArea(14, 55);

    //dekalarcja StudentManager jako pola klasy
    private final StudentManager manager;

    //konstruktor
    public StudentManagementApp(StudentManager manager) {
        //wywołanie konstruktora klasy nadrzędnej w tym przypadku "JFrame", ustawiamy tytuł okna
        super("Student Management System (SMS)");
        this.manager = manager;

        //"zabicie" aplikacji w momencie kiedy zamkniemy okno, zamykamy ono, procesy są przerywane nie działają gdzies w tle
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //ustawienie przerwy pomiędzy elementami w oknie, zwykłe ustawienie marginesów
        setLayout(new BorderLayout(10, 10));

        //rozmieszczenie paneli po oknie
        add(buildInputPanel(), BorderLayout.NORTH);
        add(buildButtonsPanel(), BorderLayout.CENTER);
        add(buildOutputPanel(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    //panel do wprowadzania danych przez usera
    private JPanel buildInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Wprowadz Dane"));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 8, 6, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0;
        panel.add(new JLabel("ID Studenta:"), c);
        c.gridx = 1;
        panel.add(idField, c);

        c.gridx = 0; c.gridy = 1;
        panel.add(new JLabel("Imie:"), c);
        c.gridx = 1;
        panel.add(nameField, c);

        c.gridx = 0; c.gridy = 2;
        panel.add(new JLabel("Wiek:"), c);
        c.gridx = 1;
        panel.add(ageField, c);

        c.gridx = 0; c.gridy = 3;
        panel.add(new JLabel("Ocena (0-100):"), c);
        c.gridx = 1;
        panel.add(gradeField, c);

        return panel;
    }

    //panel do przycisków
    private JPanel buildButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Akcje"));

        JButton addBtn = new JButton("Dodaj Studenta");
        JButton removeBtn = new JButton("Usun Studenta");
        JButton updateBtn = new JButton("Zaktualizuj Studenta");
        JButton displayBtn = new JButton("Wyswietl wszystkich studentow");
        JButton avgBtn = new JButton("Przelicz srednia ocen");
        JButton clearBtn = new JButton("Wyczysc wynik");

        addBtn.addActionListener(e -> onAdd());
        removeBtn.addActionListener(e -> onRemove());
        updateBtn.addActionListener(e -> onUpdate());
        displayBtn.addActionListener(e -> onDisplayAll());
        avgBtn.addActionListener(e -> onAverage());
        clearBtn.addActionListener(e -> outputArea.setText(""));

        panel.add(addBtn);
        panel.add(removeBtn);
        panel.add(updateBtn);
        panel.add(displayBtn);
        panel.add(avgBtn);
        panel.add(clearBtn);

        return panel;
    }

    //panel do outputu(wyniku)
    private JScrollPane buildOutputPanel() {
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(outputArea);
        scroll.setBorder(BorderFactory.createTitledBorder("Wynik:"));
        return scroll;
    }

//METODY KTÓRE OBSŁUJĄ POLA I WYWOUJĄ METODY KLASY "StudentManagerImpl"

    //dodawanie studenta
    private void onAdd() {
        try {
            Student s = readStudentFromFields();
            manager.addStudent(s);
            log("Dodano Studenta o ID: " + s);
        } catch (Exception ex) {
            logError(ex);
        }
    }

    //usuwanie studenta
    private void onRemove() {
        try {
            String id = readId();
            manager.removeStudent(id);
            log("Usunieto Studenta o ID: " + id);
        } catch (Exception ex) {
            logError(ex);
        }
    }


    //aktualizowanie studenta
    private void onUpdate() {
        try {
            Student s = readStudentFromFields();
            manager.updateStudent(s);
            log("Zaktualizowano studenta o ID: " + s.getStudentID());
        } catch (Exception ex) {
            logError(ex);
        }
    }


    //wyświetlenie całej tabeli studentow
    private void onDisplayAll() {
        try {
            ArrayList<Student> all = manager.displayAllStudents();
            if (all.isEmpty()) {
                log("Brak studentow w bazie danych :(.");
                return;
            }
            String text = all.stream()
                    .map(Student::toString)
                    .collect(Collectors.joining("\n"));
            log(text);
        } catch (Exception ex) {
            logError(ex);
        }
    }

    //obliczanie sredniej ocen
    private void onAverage() {
        try {
            double avg = manager.calculateAverageGrade();
            log(String.format("Srednia ocen to: %.2f", avg));
        } catch (Exception ex) {
            logError(ex);
        }
    }

    //Pobieranie ID z pola tekstowego, metoda readId została wydzielona bo pozwala pobierać sam ID , bez reszty danych
    private String readId() {
        String id = idField.getText();
        if (id == null || id.trim().isEmpty())
            throw new IllegalArgumentException("Student ID jest wymagane!");
        return id.trim();
    }

    //Pobieranie danych z pól tekstowych
    private Student readStudentFromFields() {
        String id = readId();

        String name = nameField.getText();
        String ageTxt = ageField.getText();
        String gradeTxt = gradeField.getText();


            if (name == null || name.trim().isEmpty())
                throw new IllegalArgumentException("Imie jest wymagane!");

            if (ageTxt == null || ageTxt.trim().isEmpty())
                throw new IllegalArgumentException("Wiek jest wymagane!");

            if (gradeTxt == null || gradeTxt.trim().isEmpty())
                throw new IllegalArgumentException("Ocena jest wymagana!");


        int age;
        double grade;

        try {
            age = Integer.parseInt(ageTxt.trim());
            if (age <= 0) throw new IllegalArgumentException("Wiek musi byc liczba dodatnia!");
        } catch (Exception e) {
            throw new IllegalArgumentException("Nieprawidlowy wiek.");
        }

        try {
            grade = Double.parseDouble(gradeTxt.trim());
            if (grade < 0.0 || grade > 100.0)
                throw new IllegalArgumentException("Ocena musi byc w przedziale 0–100!");
        } catch (Exception e) {
            throw new IllegalArgumentException("Nieprawidlowa ocena.");
        }

        return new Student(name.trim(), age, id, grade);
    }


    private void log(String msg) {
        outputArea.append(msg + "\n");
    }

    private void logError(Exception ex) {
        outputArea.append("ERROR: " + ex.getMessage() + "\n");
    }
}
