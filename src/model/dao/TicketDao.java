package model.dao;

import java.util.List;

import model.entities.Ticket;
import model.entities.Vehicle;

public interface TicketDao {

	void update(Ticket obj);
	void insert(Ticket obj);
	Ticket findById(Integer id);
	List<Ticket> findByVehicle(Vehicle vehicle);
}
