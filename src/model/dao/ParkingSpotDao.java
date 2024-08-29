package model.dao;

import java.util.List;

import model.entities.ParkingSpot;

public interface ParkingSpotDao {

	ParkingSpot findByNumber (Integer id);
	void update(ParkingSpot obj);
	List<ParkingSpot> findAll();
}
