package application;

import java.sql.Connection;
import java.util.Scanner;

import db.DB;
import model.dao.GateDao;
import model.dao.VehicleDao;
import model.dao.impl.GateDaoJDBC;
import model.dao.impl.VehicleDaoJDBC;
import model.entities.Car;
import model.entities.DeliveryTruck;
import model.entities.Motorcycle;
import model.entities.PublicService;
import model.entities.Vehicle;
import model.enums.Category;

public class Program {

	public static void main(String[] args) {

		Connection conn = DB.getConnection();
		VehicleDao vehicleDao = new VehicleDaoJDBC(conn);
		GateDao gateDao = new GateDaoJDBC(conn);

		Scanner sc = new Scanner(System.in);

		System.out.print("Deseja cadastrar um veículo (s/n)? ");
		char n = sc.next().charAt(0);

		if (n == 's') {
			System.out.println("Vehicle data:");
			System.out.print("Enter with the license plate: ");
			String plate = sc.next();

			System.out.print("Category of the vehicle (SUBSCRIBER, DELIVERY_TRUCK, CASUAL, PUBLIC_SERVICE): ");
			Category category = Category.valueOf(sc.next());

			Vehicle vehicle;
			System.out.print("Vehicle type (C/M/P/D): ");
			char vehicleType = sc.next().charAt(0);

			switch (vehicleType) {
			case 'M':
				vehicle = new Motorcycle(null, plate, category);
				break;
			case 'P':
				vehicle = new PublicService(null, plate, category);
				break;
			case 'D':
				vehicle = new DeliveryTruck(null, plate, category);
				break;
			default:
				vehicle = new Car(null, plate, category);
			}
			
			vehicleDao.insert(vehicle);
		}
		
		
	}

}
