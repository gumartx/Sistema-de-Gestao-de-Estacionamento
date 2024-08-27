package model.dao.impl;

import java.sql.Connection;

import model.dao.VehicleDao;
import model.entities.Vehicle;

public class VehicleDaoJDBC implements VehicleDao {

	private Connection conn;

	public VehicleDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Vehicle obj) {
		// TODO Auto-generated method stub
		
	}

	
	
}
