package parking;

import java.time.Duration;
import java.time.LocalDateTime;

import model.entities.Gate;
import model.entities.Motorcycle;
import model.entities.ParkingSpot;
import model.entities.Ticket;
import model.entities.Vehicle;
import model.enums.Category;
import model.enums.GateType;
import model.exceptions.GateException;
import model.exceptions.TicketException;
import model.exceptions.VehicleException;

public class Parking {

	public static Ticket registerEntry(Vehicle vehicle, Gate gate, ParkingSpot spot) {

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
	
	public static boolean registerExit(Ticket ticket, Gate gate) {
		
		if (!gate.getType().equals(GateType.EXIT)) {
			return false;
		}
		
		if(ticket.getVehicle() instanceof Motorcycle) {
			return gate.getNumber() == 10;
		}
		
	    ticket.setExitTime(LocalDateTime.now());
	    ticket.setExitGate(gate);

	    double amountPaid = calculateAmount(ticket);
	    ticket.setAmountPaid(amountPaid);

	    ticket.getParkingSpot().setStatus(false);

	    return true;
	}

	private static boolean validateEntry(Vehicle vehicle, Gate gate) {
		Category vehicleCategory = vehicle.getCategory();
		
		if (!gate.getType().equals(GateType.ENTRY)) {
			return false;
		}
		
		if (vehicle instanceof Motorcycle) {
			return gate.getNumber() == 5;
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
		
		return false;
	}
	
	private static double calculateAmount(Ticket ticket) {
	    Category vehicleCategory = ticket.getVehicle().getCategory();
	    double amount = 0.0;

	    switch (vehicleCategory) {
	        case SUBSCRIBER:
	            amount = 250.00;
	            break;

	        case DELIVERY_TRUCK:
	        case CASUAL:
	            LocalDateTime entryTime = ticket.getEntryTime();
	            LocalDateTime exitTime = ticket.getExitTime();

	            if (exitTime == null) {
	                throw new TicketException("Exit time is not recorded.");
	            }

	            long minutesParked = Duration.between(entryTime, exitTime).toMinutes();

	            amount = minutesParked * 0.10;

	            if (amount < 5.00) {
	                amount = 5.00;
	            }
	            break;

	        case PUBLIC_SERVICE:
	            amount = 0.00;
	            break;

	        default:
	            throw new VehicleException("Unknown vehicle category.");
	    }

	    return amount;
	}

}
