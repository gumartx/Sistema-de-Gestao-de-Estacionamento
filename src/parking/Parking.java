package parking;

import java.time.LocalDateTime;

import model.entities.Gate;
import model.entities.ParkingSpot;
import model.entities.Ticket;
import model.entities.Vehicle;
import model.enums.Category;
import model.enums.GateType;
import model.enums.Restriction;
import model.enums.VehicleType;
import model.exceptions.GateException;

public class Parking {

	public Ticket registerEntry(Vehicle vehicle, Gate gate, ParkingSpot spot) {

		if (validateEntry(vehicle, gate)) {
			Ticket ticket = new Ticket();
			ticket.setVehicle(vehicle);
			ticket.setEntryGate(gate);
			ticket.setParkingSpot(spot);
			ticket.setEntryTime(LocalDateTime.now());

			spot.setStatus(true);

			return ticket;

		} else {
			throw new GateException("Entry not allowed for this vehicle at this gate.");
		}
	}

	public boolean validateEntry(Vehicle vehicle, Gate gate) {
		Category vehicleCategory = vehicle.getCategory();
		VehicleType vehicleType = vehicle.getType();
		Restriction gateRestriction = gate.getRestriction();

		if (!gate.getType().equals(GateType.ENTRY)) {
			return false;
		}

		switch (vehicleCategory) {
		case SUBSCRIBER:
			return true;

		case DELIVERY_TRUCK:
			return gate.getNumber() == 1;

		case CASUAL:
			return true;

		case PUBLIC_SERVICE:
			return true;
		}

		switch (gateRestriction) {
		case NONE:
			return true;

		case TRUCK_ONLY:
			return vehicleType.equals(VehicleType.DELIVERY_TRUCK);

		case MOTORCYCLE_ONLY:
			return vehicleType.equals(VehicleType.MOTORCYCLE);
		}
		
		return false;
	}

}
