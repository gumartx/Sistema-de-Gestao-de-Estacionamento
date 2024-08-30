package application;

import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.GateDao;
import model.dao.ParkingSpotDao;
import model.dao.TicketDao;
import model.dao.VehicleDao;
import parking.ParkingHandler;
import parking.VehicleRegistration;

public class Program {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		VehicleDao vehicleDao = DaoFactory.createVehicleDao();
		GateDao gateDao = DaoFactory.createGateDao();
		ParkingSpotDao parkingDao = DaoFactory.createParkingSpotDao();
		TicketDao ticketDao = DaoFactory.createTicketDao();

		int n = 1;
		while (n != 0) {
			System.out.println("\n========================");
			System.out.println("Register vehicle : 1");
			System.out.println("Entry a vehicle  : 2");
			System.out.println("Exit a vehicle   : 3");
			System.out.println("Exit system      : 0");
			System.out.println("========================");
			System.out.print("\nSelect a number: ");
			n = sc.nextInt();
			switch (n) {

			case 1:
				VehicleRegistration.register(vehicleDao, sc);
				break;
			case 2:
				ParkingHandler.entryParking(vehicleDao, gateDao, ticketDao, parkingDao, sc);
				break;
			case 3:
				ParkingHandler.exitParking(vehicleDao, ticketDao, parkingDao, gateDao, sc);
				break;
			default:
				n = 0;
			}
		}
		
		sc.close();
	}

}
