package parking;

import java.util.Scanner;

import model.dao.VehicleDao;
import model.entities.Vehicle;
import model.enums.Category;
import model.enums.VehicleType;

public class VehicleRegistration {

	public static void register(VehicleDao vehicleDao, Scanner sc) {

		System.out.println("\nVehicle data:");
		System.out.print("Enter with the license plate: ");
		String plate = sc.next();

		System.out.print("Category of the vehicle (SUBSCRIBER, DELIVERY_TRUCK, CASUAL, PUBLIC_SERVICE): ");
		Category category = Category.valueOf(sc.next().toUpperCase());

		System.out.print("Vehicle type (Car, Motorcycle, Public_service, Delivery_truck): ");
		VehicleType type = VehicleType.valueOf(sc.next().toUpperCase());

		Vehicle vehicle = new Vehicle(null, plate, category, type);

		vehicleDao.insert(vehicle);

	}
}
