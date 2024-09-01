package parking;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.dao.DaoFactory;
import model.dao.ParkingSpotDao;
import model.dao.TicketDao;
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

	private static ParkingSpotDao parkingDao = DaoFactory.createParkingSpotDao();
	private static TicketDao ticketDao = DaoFactory.createTicketDao();

	public static Vehicle registerEntry(Vehicle vehicle, Gate gate) {

		List<ParkingSpot> spot = parkingDao.findAll();
		
		//retorna a quantidade de vagas que serão preenchidas
		List<ParkingSpot> result = getFreeSpots(vehicle, spot);

		//apenas veículos avulsos recebem ticket
		if (vehicle.getCategory().name() == "CASUAL") {
			
			if (validateEntry(vehicle, gate, result)) {
				Ticket ticket = registerTicket(vehicle, gate);
				ticketDao.insert(ticket);
			} else {
				throw new GateException("Entry not allowed for this vehicle at this gate.");
			}
			
		} else {
			
			if (validateEntry(vehicle, gate, result)) {

				//usando a mesma instancia de veículo para vincular as vagas
				Map<String, Vehicle> map = new HashMap<>();

				for (ParkingSpot s : result) {

					Vehicle vec = map.get(vehicle.getPlate());

					if (vec == null) {
						vec = vehicle;
						map.put(vehicle.getPlate(), vec);
					}
					s.setVehicle(vec);
					s.setStatus(true);

					parkingDao.update(s);

				}
			} else {
				throw new GateException("Entry not allowed for this vehicle at this gate.");
			}

		}

		return vehicle;
	}

	private static Ticket registerTicket(Vehicle vehicle, Gate gate) {

		List<ParkingSpot> spot = parkingDao.findAll();
		List<ParkingSpot> result = getFreeSpots(vehicle, spot);

		Ticket ticket = new Ticket();
		ticket.setVehicle(vehicle);
		ticket.setEntryGate(gate);
		ticket.setEntryTime(LocalDateTime.now());

		Map<String, Vehicle> map = new HashMap<>();

		if (vehicle.getCategory().name() != "PUBLIC_SERVICE") {
			for (ParkingSpot s : result) {

				Vehicle vec = map.get(vehicle.getPlate());

				if (vec == null) {
					vec = vehicle;
					map.put(vehicle.getPlate(), vec);
				}
				s.setVehicle(vec);
				s.setStatus(true);
				ticket.getSpots().add(s);

				parkingDao.update(s);
			}
		}
		return ticket;
	}

	private static List<ParkingSpot> getFreeSpots(Vehicle vehicle, List<ParkingSpot> spot) {
		List<ParkingSpot> result;

		//retorna a quantidade de vaga que serão ocupadas pelo veículo
		int vehicleSize = getVehicleSpotSize(vehicle);
		
		if (vehicle.getCategory().name() == "SUBSCRIBER") {
			//retorna as vagas livres reservadas
			result = spot.stream().limit(vehicleSize).toList();

		} else {
			//retorna as demais vagas livres
			result = spot.stream().skip(200).limit(vehicleSize).toList();

		}
		return result;
	}

	public static void registerExit(Vehicle vehicle, Gate gate, List<ParkingSpot> spot) {
		VehicleType type = vehicle.getType();

		//verifica se a cancela é de saída
		if (!gate.getType().equals(GateType.EXIT)) {
			throw new ParkingException("Exit not allowed at this gate");
		}

		//verifica se a moto está saindo pela sua cancela
		if (type == VehicleType.MOTORCYCLE && gate.getNumber() != 10) {
			throw new ParkingException("Exit not allowed for this vehicle at this gate");
		}

		//atualização do ticket no banco do veículo avulso
		if (vehicle.getCategory() == Category.CASUAL) {
			Ticket ticket = ticketDao.findByVehicle(vehicle).stream().findFirst().get();
			ticket.setExitTime(LocalDateTime.now());
			ticket.setExitGate(gate);

			double amountPaid = calculateAmount(ticket, vehicle);
			ticket.setAmountPaid(amountPaid);

			for (ParkingSpot s : spot) {
				s.setStatus(false);
				s.setVehicle(null);

				parkingDao.update(s);
			}

			ticketDao.update(ticket);
		} else {
			if (vehicle.getCategory() != Category.PUBLIC_SERVICE) {
				for (ParkingSpot s : spot) {
					s.setStatus(false);
					s.setVehicle(null);

					parkingDao.update(s);
				}
			}

		}

	}

	//retorna quantidade de vagas utilizadas por cada tipo de veículo
	private static int getVehicleSpotSize(Vehicle vehicle) {
		VehicleType type = vehicle.getType();

		switch (type) {
		case MOTORCYCLE:
			return 1;
		case CAR:
			return 2;
		case TRUCK:
			return 4;
		case PUBLIC_SERVICE:
			return 0;
		default:
			throw new VehicleException("Invalid vehicle type");
		}
	}

	//validar os dados para a entrada no estacionamento
	private static boolean validateEntry(Vehicle vehicle, Gate gate, List<ParkingSpot> spot) {
		Category vehicleCategory = vehicle.getCategory();
		VehicleType type = vehicle.getType();

		if (!parkingDao.findByVehicle(vehicle).isEmpty()) {
			throw new ParkingException("Vehicle is already in the parking lot");
		}

		//verifica se tem vaga para o veículo (veículos de serviço publico não ocupam vaga)
		if (vehicleCategory != Category.PUBLIC_SERVICE) {
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
		}

		//verifica se a cancela é de entrada
		if (!gate.getType().equals(GateType.ENTRY)) {
			return false;
		}

		//motos so podem entrar pela cancela 5
		if (type == VehicleType.MOTORCYCLE) {
			return gate.getNumber() == 5;
		}

		//caminhões so podem entrar pela cancela 1
		if (type == VehicleType.TRUCK) {
			return gate.getNumber() == 1;
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

	//calculo da quantidade paga com base no tempo do veiculo dentro do estacionamento
	private static double calculateAmount(Ticket ticket, Vehicle vehicle) {
		Category vehicleCategory = vehicle.getCategory();
		double amount = 0.0;

		switch (vehicleCategory) {

		case DELIVERY_TRUCK:
		case CASUAL:
			LocalDateTime entryTime = ticket.getEntryTime();
			LocalDateTime exitTime = ticket.getExitTime();

			if (exitTime == null) {
				throw new TicketException("Exit time is not recorded.");
			}

			long minutesParked = Duration.between(entryTime, exitTime).toMinutes();

			amount = minutesParked * 0.10 * getVehicleSizeSpot(vehicle);

			if (amount < 5.00) {
				amount = 5.00;
			}
			break;

		default:
			throw new VehicleException("Unknown vehicle category.");
		}

		return amount;
	}
	
}
