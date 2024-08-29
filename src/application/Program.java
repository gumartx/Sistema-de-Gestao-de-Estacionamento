package application;

import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.GateDao;
import model.dao.ParkingSpotDao;
import model.dao.VehicleDao;
import model.entities.Ticket;
import parking.ParkingHandler;
import parking.VehicleRegistration;

public class Program {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		
		VehicleDao vehicleDao = DaoFactory.createVehicleDao();
		GateDao gateDao = DaoFactory.createGateDao();
		ParkingSpotDao parkingDao = DaoFactory.createParkingSpotDao();

		VehicleRegistration.register(vehicleDao, sc);
		ParkingHandler.entryParking(vehicleDao, gateDao, parkingDao, sc);
		
		sc.close();
	}

}
