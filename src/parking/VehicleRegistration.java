package parking;

import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.VehicleDao;
import model.entities.Vehicle;
import model.enums.Category;
import model.enums.VehicleType;
import model.exceptions.VehicleException;

public class VehicleRegistration {
	
	private static VehicleDao vehicleDao = DaoFactory.createVehicleDao();

	public static void register(Scanner sc) {

		System.out.println("\nVehicle data:");
		System.out.print("Enter with the license plate: ");
		String plate = sc.next();

		System.out.print("Category of the vehicle (SUBSCRIBER, DELIVERY_TRUCK): ");
		Category category = Category.valueOf(sc.next().toUpperCase());

		if (category.name() != "SUBSCRIBER" && category.name() != "DELIVERY_TRUCK") {
			throw new VehicleException("You can not register this vehicle");
		}
		
		System.out.print("Vehicle type (Car, Motorcycle, Delivery_truck): ");
		VehicleType type = VehicleType.valueOf(sc.next().toUpperCase());

		if (type.name() == "PUBLIC_SERVICE") {
			throw new VehicleException("This vehicle type does not need a registration");
		}
		
		Vehicle vehicle = new Vehicle(plate, category, type);

		vehicleDao.insert(vehicle);

	}
}
