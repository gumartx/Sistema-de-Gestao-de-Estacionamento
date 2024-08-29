package model.dao;

import model.entities.Vehicle;

public interface VehicleDao {

	void insert (Vehicle obj);
	Vehicle findById(Integer id);
}
