package parking;

import java.util.List;
import java.util.Scanner;

import model.dao.GateDao;
import model.dao.ParkingSpotDao;
import model.dao.TicketDao;
import model.dao.VehicleDao;
import model.entities.Gate;
import model.entities.ParkingSpot;
import model.entities.Ticket;
import model.entities.Vehicle;

public class ParkingHandler {

	public static void entryParking(VehicleDao vehicleDao, GateDao gateDao, TicketDao ticketDao,
			ParkingSpotDao parkingDao, Scanner sc) {

		System.out.println("\n===Enter the vehicle in the parking lot===");

		System.out.print("\nEnter with license plate: ");
		String plate = sc.next();
		Vehicle vehicle = vehicleDao.findByPlate(plate);

		System.out.print("Enter the id of the entry gate: ");
		Gate gate = gateDao.findById(sc.nextInt());

		Ticket ticket = Parking.registerEntry(vehicle, gate, parkingDao);

		ticketDao.insert(ticket);

	}

	public static void exitParking(VehicleDao vehicleDao, TicketDao ticketDao, ParkingSpotDao parkingDao,
			GateDao gateDao, Scanner sc) {

		System.out.print("\nEnter the license plate to exit the parking lot: ");
		String plate = sc.next();

		Vehicle vehicle = vehicleDao.findByPlate(plate);
		Ticket ticket = ticketDao.findByVehicle(vehicle).stream().findFirst().get();
		List<ParkingSpot> list = parkingDao.findByVehicle(vehicle);

		System.out.print("Enter the id of the exit gate: ");
		Gate gate = gateDao.findById(sc.nextInt());

		ticket = Parking.registerExit(ticket, vehicle, gate, list, parkingDao);

		ticketDao.update(ticket);

	}

}
