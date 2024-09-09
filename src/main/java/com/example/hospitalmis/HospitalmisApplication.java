package com.example.hospitalmis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;
import java.util.Scanner;

@SpringBootApplication
public class HospitalmisApplication {
	public static void main(String[] args) {
		try {
			// Load PostgresSQL JDBC driver
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		// PostgresSQL connection details
		String url = "jdbc:postgresql://localhost:5432/hospitaldb"; // Adjust your DB name and URL
		String username = "postgres";
		String password = "0000";

		Scanner scanner = new Scanner(System.in);

		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			Patient patient = new Patient(connection, scanner);
			Doctor doctor = new Doctor(connection, scanner);

			while (true) {
				System.out.println("HOSPITAL MANAGEMENT SYSTEM");
				System.out.println("1. Add Patient");
				System.out.println("2. View Patients");
				System.out.println("3. View Doctors");
				System.out.println("4. Book Appointment");
				System.out.println("5. Exit");
				System.out.println("Enter your choice: ");
				int choice = scanner.nextInt();

				switch (choice) {
					case 1:
						// Add Patient
						patient.addPatient();
						System.out.println();
						break;
					case 2:
						// View Patient
						patient.viewPatient();
						System.out.println();
						break;
					case 3:
						// View Doctors
						doctor.viewDoctor();
						System.out.println();
						break;
					case 4:
						// Book Appointment
						bookAppointment(patient, doctor, connection, scanner);
						System.out.println();
						break;
					case 5:
						System.out.println("THANK YOU! FOR USING HOSPITAL MANAGEMENT SYSTEM!!");
						return;
					default:
						System.out.println("Enter valid choice!!!");
						break;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner scanner) {
		System.out.print("Enter Patient Id: ");
		int patientId = scanner.nextInt();
		System.out.print("Enter Doctor Id: ");
		int doctorId = scanner.nextInt();
		System.out.print("Enter appointment date (YYYY-MM-DD): ");
		String appointmentDate = scanner.next();
		if (patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)) {
			if (checkDoctorAvailability(doctorId, appointmentDate, connection)) {
				String appointmentQuery = "INSERT INTO appointments(patient_id, doctor_id, appointment_date) VALUES(?, ?, ?)";
				try {
					PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
					preparedStatement.setInt(1, patientId);
					preparedStatement.setInt(2, doctorId);
					preparedStatement.setString(3, appointmentDate);
					int rowsAffected = preparedStatement.executeUpdate();
					if (rowsAffected > 0) {
						System.out.println("Appointment Booked!");
					} else {
						System.out.println("Failed to Book Appointment!");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("Doctor not available on this date!!");
			}
		} else {
			System.out.println("Either doctor or patient doesn't exist!!!");
		}
	}

	public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection) {
		String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, doctorId);
			preparedStatement.setString(2, appointmentDate);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				int count = resultSet.getInt(1);
				return count == 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
