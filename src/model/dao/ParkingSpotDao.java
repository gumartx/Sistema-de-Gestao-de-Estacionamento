package model.dao;

import java.util.List;

import model.entities.ParkingSpot;
import model.entities.Vehicle;

public interface ParkingSpotDao {

	ParkingSpot findByNumber (Integer id);
	void update(ParkingSpot obj);
	List<ParkingSpot> findAll();
	List<ParkingSpot> findByVehicle(Vehicle obj);
}
