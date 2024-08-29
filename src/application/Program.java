package application;

import java.util.ArrayList;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.GateDao;
import model.dao.ParkingSpotDao;
import model.dao.VehicleDao;
import model.entities.Gate;
import model.entities.ParkingSpot;
import model.entities.Ticket;
import model.entities.Vehicle;
import model.enums.Category;
import parking.Parking;
import parking.VehicleRegistration;

public class Program {

	public static void main(String[] args) {

		VehicleDao vehicleDao = DaoFactory.createVehicleDao();
		GateDao gateDao = DaoFactory.createGateDao();
		ParkingSpotDao parkingDao = DaoFactory.createParkingSpotDao();

		VehicleRegistration.register(vehicleDao);

		Vehicle v = vehicleDao.findById(24);
		Gate g = gateDao.findById(5);
		Gate g1 = gateDao.findById(10);
		int spots = Parking.getVehicleSpotSize(v);
		List<ParkingSpot> list = new ArrayList<>();

		ParkingSpot p = parkingDao.findByNumber(1);
		list.add(p);

		Ticket ticket = Parking.registerEntry(v, g, list);
		boolean r = Parking.registerExit(ticket, g1);

		System.out.println(ticket);

	}

}
