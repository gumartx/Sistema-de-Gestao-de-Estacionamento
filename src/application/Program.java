package application;

import java.sql.Connection;
import java.util.Scanner;

import db.DB;
import model.entities.Motorcycle;
import model.entities.Vehicle;
import model.enums.Category;

public class Program {

	public static void main(String[] args) {
		
		Connection conn = DB.getConnection();
		DB.closeConnection();
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Vehicle data:");
		System.out.print("Enter with the license plate: ");
		String plate = sc.next();
		
		System.out.print("Category of the vehicle (SUBSCRIBER, DELIVERY_TRUCK, CASUAL, PUBLIC_SERVICE): ");
		Category category = Category.valueOf(sc.next());
		
		Vehicle vehicle = new Motorcycle(null, plate, category);
	}
	
}
