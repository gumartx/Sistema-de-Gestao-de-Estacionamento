package model.dao;

import model.entities.Vehicle;

public interface VehicleDao {

	void insert (Vehicle obj);
	Vehicle findByPlate(String id);
}
