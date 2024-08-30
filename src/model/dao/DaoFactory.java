package model.dao;

import db.DB;
import model.dao.impl.GateDaoJDBC;
import model.dao.impl.ParkingSpotDaoJDBC;
import model.dao.impl.TicketDaoJDBC;
import model.dao.impl.VehicleDaoJDBC;

public class DaoFactory {

	public static VehicleDao createVehicleDao() {
		return new VehicleDaoJDBC(DB.getConnection());
	}

	public static ParkingSpotDao createParkingSpotDao() {
		return new ParkingSpotDaoJDBC(DB.getConnection());
	}

	public static GateDao createGateDao() {
		return new GateDaoJDBC(DB.getConnection());
	}

	public static TicketDao createTicketDao() {
		return new TicketDaoJDBC(DB.getConnection());
	}
	
}
