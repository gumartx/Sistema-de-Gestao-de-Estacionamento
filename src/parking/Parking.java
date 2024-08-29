package parking;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.dao.ParkingSpotDao;
import model.entities.Gate;
import model.entities.ParkingSpot;
import model.entities.Ticket;
import model.entities.Vehicle;
import model.enums.Category;
import model.enums.GateType;
import model.enums.VehicleType;
import model.exceptions.GateException;
import model.exceptions.TicketException;
import model.exceptions.VehicleException;
import parking.exceptions.ParkingException;

public class Parking {

	public static void registerEntry(Vehicle vehicle, Gate gate, List<ParkingSpot> spot, ParkingSpotDao parkingDao) {
		
		if (validateEntry(vehicle, gate, spot)) {
			Ticket ticket = new Ticket();
			ticket.setVehicle(vehicle);
			ticket.setEntryGate(gate);
			ticket.setEntryTime(LocalDateTime.now());

			Map<Integer, Vehicle> map = new HashMap<>();
			
			for (ParkingSpot s : spot) {

				Vehicle vec = map.get(vehicle.getId());

				if (vec == null) {
					vec = vehicle;
					map.put(vehicle.getId(), vec);
				}
				
				s.setVehicle(vec);
				
				s.setStatus(true);
				ticket.getSpots().add(s);
				
				parkingDao.update(s);
			}

		} else {
			throw new GateException("Entry not allowed for this vehicle at this gate.");
		}
	}

	public static Ticket registerExit(Ticket ticket, Gate gate) {
		VehicleType type = ticket.getVehicle().getType();
		
		if (!gate.getType().equals(GateType.EXIT)) {
			throw new ParkingException("Exit not allowed at this gate");
		}

		if (type == VehicleType.MOTORCYCLE && gate.getNumber() != 10) {
			throw new ParkingException("Exit not allowed for this vehicle at this gate");
		}

		ticket.setExitTime(LocalDateTime.now());
		ticket.setExitGate(gate);

		double amountPaid = calculateAmount(ticket);
		ticket.setAmountPaid(amountPaid);

		for (ParkingSpot s : ticket.getSpots()) {
			s.setStatus(false);
		}

		return ticket;
	}

	public static int getVehicleSpotSize(Vehicle vehicle) {
		VehicleType type = vehicle.getType();

		switch (type) {
		case MOTORCYCLE:
			return 1;
		case CAR:
			return 2;
		case DELIVERY_TRUCK:
			return 3;
		case PUBLIC_SERVICE:
			return 0;
		default:
			throw new VehicleException("Invalid vehicle type");
		}
	}

	private static boolean validateEntry(Vehicle vehicle, Gate gate, List<ParkingSpot> spot) {
		Category vehicleCategory = vehicle.getCategory();
		VehicleType type = vehicle.getType();

		if (!spot.isEmpty()) {
			for (ParkingSpot s : spot) {
				if (s.isStatus()) {
					throw new ParkingException("Spot occupied");
				}
				if (s.getReserve().name() == "SUBSCRIBER" && vehicle.getCategory().name() != "SUBSCRIBER") {
					throw new ParkingException("Spot reserved for Subscribers");
				}
			}
		} else {
			throw new ParkingException("Not enough spots");
		}

		if (!gate.getType().equals(GateType.ENTRY)) {
			return false;
		}

		if (type == VehicleType.MOTORCYCLE) {
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
