package com.example.hospitalmis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.function.Predicate;

public class Patient {
    private Connection conn;
    private Scanner scanner;

    public Patient(Connection conn, Scanner scanner){
        this.conn = conn;
        this.scanner = scanner;
    }

    public void addPatient(){
        System.out.println("Enter Patient Names: ");
        String name = scanner.nextLine();

        scanner.nextLine();

        System.out.println("Enter Patient Age: ");
        int age = scanner.nextInt();

        scanner.nextLine();

        System.out.println("Enter Patient Gender: ");
        String gender = scanner.nextLine();

        try{
            String sql = "INSERT INTO patient (name, age, gender) VALUES(?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,name);
            ps.setInt(2, age);
            ps.setString(3, gender);
            int affectedRows = ps.executeUpdate();

            if(affectedRows > 0){
                System.out.println("Patient Added Successfully!");
            }else{
                System.out.println("Failed to add patient");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void viewPatient(){
        String sql = "SELECT * FROM patient";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            System.out.println("Patient Details: ");
            System.out.println("+--------+----------------------+--------+-----------+");
            System.out.println("| Id     | Name                 | Age    | Gender    |");
            System.out.println("+--------+----------------------+--------+-----------+");
            while (rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String gender = rs.getString("gender");
                System.out.printf("|%-8s|%-22s|%-8s|%-11s|\n", id, name, age, gender);
                System.out.println("+--------+----------------------+--------+-----------+");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public boolean getPatientById(int id){
        String sql = "SELECT * FROM patient WHERE id = ?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }else{
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

}

