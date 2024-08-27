package model.dao;

import db.DB;
import model.dao.impl.VehicleDaoJDBC;

public class DaoFactory {

	public static VehicleDao createSellerDao() {
		return new VehicleDaoJDBC(DB.getConnection());
	}
	
}
