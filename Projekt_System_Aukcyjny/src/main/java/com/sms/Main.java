package com.sms;

import com.sms.service.StudentManager;
import com.sms.service.StudentManagerImpl;
import com.sms.ui.StudentManagementApp;

import javax.swing.*;

public class Main
{
    private static final String DB_URL = "jdbc:sqlite:C:\\Users\\Sebek\\Documents\\SQLite\\students";

    public static void main(String[] args) {
        StudentManager manager = new StudentManagerImpl(DB_URL);
        SwingUtilities.invokeLater(() -> new StudentManagementApp(manager).setVisible(true));  // uruchamia kod GUI na watku graficznym swing ,gdy będzie gotowy, a dopiero potem uruchamia cała aplikację
    }
}
