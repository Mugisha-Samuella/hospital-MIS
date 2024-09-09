package com.example.hospitalmis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctor {
    private final Connection conn;

    public Doctor(Connection conn, Scanner scanner){
        this.conn = conn;
    }

    public void viewDoctor(){
        String sql = "SELECT * FROM doctors";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            System.out.println("Doctors: ");
            System.out.println("+-----------+----------------------+--------------------+");
            System.out.println("|Doctor Id  | Name                 | Specialistaion     |");
            System.out.println("+-----------+----------------------+--------------------+");
            while (rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String specialisation = rs.getString("specialisation");
                System.out.println();
                System.out.println("+--------+----------------------+--------------------+");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public boolean getDoctorById(int id){
        String sql = "SELECT * FROM doctors WHERE id = ?";
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
