package parking;

import java.util.List;
import java.util.Scanner;

import model.dao.GateDao;
import model.dao.ParkingSpotDao;
import model.dao.VehicleDao;
import model.entities.Gate;
import model.entities.ParkingSpot;
import model.entities.Vehicle;

public class ParkingHandler {

	public static void entryParking(VehicleDao vehicleDao, GateDao gateDao, ParkingSpotDao parkingDao, Scanner sc) {

		System.out.println("Do you want to enter a vehicle in the parking lot (s/n)? ");
		char n = sc.next().charAt(0);
		
		while (n == 's') {
			List<ParkingSpot> list = parkingDao.findAll();

			System.out.println("\n===Enter the vehicle in the parking lot===");

			System.out.print("\nEnter with license plate: ");
			String plate = sc.next();
			Vehicle vehicle = vehicleDao.findByPlate(plate);

			System.out.print("Enter the id of the entry gate: ");
			Gate gate = gateDao.findById(sc.nextInt());

			int vehicleSize = Parking.getVehicleSpotSize(vehicle);
			List<ParkingSpot> result = list.stream().limit(vehicleSize).toList();

			Parking.registerEntry(vehicle, gate, result, parkingDao);

			System.out.print("Enter with mote vehicles (s/n)? ");
			n = sc.next().charAt(0);
		}
	}

}
