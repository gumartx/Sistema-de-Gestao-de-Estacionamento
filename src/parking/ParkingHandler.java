package parking;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import model.dao.DaoFactory;
import model.dao.GateDao;
import model.dao.ParkingSpotDao;
import model.dao.VehicleDao;
import model.entities.Gate;
import model.entities.ParkingSpot;
import model.entities.Vehicle;
import model.enums.Category;
import model.enums.Reserve;
import model.enums.VehicleType;
import model.exceptions.VehicleException;

public class ParkingHandler {

	private static VehicleDao vehicleDao = DaoFactory.createVehicleDao();
	private static GateDao gateDao = DaoFactory.createGateDao();
	private static ParkingSpotDao parkingDao = DaoFactory.createParkingSpotDao();

	public static void verifySpots() {
		List<ParkingSpot> spot = parkingDao.findAll();
		//retorna a quantidade de vagas livres reservadas para mensalistas
		long subsSpots = spot.stream().map(x -> x.getReserve()).filter(x -> x == Reserve.SUBSCRIBER).count();
		//retorna a quantidade de vagas livres sem reserva
		long noneSpots = spot.stream().map(x -> x.getReserve()).filter(x -> x == Reserve.NONE).count();
		System.out.println("\nFree Subscriber spots: " + subsSpots);
		System.out.println("Free spots: " + noneSpots);
	}

	public static List<Vehicle> entryParking(Scanner sc) {
		List<Vehicle> vehicles = new ArrayList<>();
		
		System.out.println("\n===Entering with a vehicle in the parking lot===");

		System.out.print("\nEnter with license plate: ");
		String plate = sc.next();
		Vehicle vehicle = vehicleDao.findByPlate(plate);
		Gate gate;
		
		//verifica se a placa do veículo existe no banco (se é um veículo mensalista ou caminhão de entrega
		//se não estiver no banco, vai ser realizado a entrada dos dados do veículo
		if (vehicle == null) {
			System.out.print("Enter the category of the vehicle (CASUAL, PUBLIC_SERVICE): ");
			Category category = Category.valueOf(sc.next().toUpperCase());

			//se o veículo for diferente de avulso ou de serviço público, é necessário o cadastro do veículo
			if (category.name() != "CASUAL" && category.name() != "PUBLIC_SERVICE") {
				throw new VehicleException("This vehicle need a previous register to entry");
			}
			
			//veículo da categoria PUBLIC SERVICE é atribuito PUBLIC SERVICE ao tipo dele
			VehicleType type;
			if (category.name() == "PUBLIC_SERVICE") {
				type = VehicleType.PUBLIC_SERVICE;
			} else {
				System.out.print("Enter the vehicle type (Car, Motorcycle, Truck): ");
				type = VehicleType.valueOf(sc.next().toUpperCase());
			}

			vehicle = new Vehicle(plate, category, type);
		}

		//veículos que não são mensalistas ou caminhões de entrega precisam entrar pelas suas respectivas cancelas
		if (vehicle.getCategory().name() != "SUBSCRIBER" && vehicle.getCategory().name() != "PUBLIC_SERVICE"
				&& vehicle.getCategory().name() != "DELIVERY_TRUCK") {
			System.out.print("Enter the id of the entry gate: ");
			gate = gateDao.findById(sc.nextInt());
		} else if (vehicle.getType().name() == "MOTORCYCLE") {
			gate = gateDao.findById(5);
		} else {
			gate = gateDao.findById(1);
		}

		//adiciona o veículo não cadastrado a coleção
		vehicles.add(Parking.registerEntry(vehicle, gate));

		return vehicles;
	}

	public static void exitParking(Set<Vehicle> vehicles, Scanner sc) {
		
		System.out.println("\n===Exiting a vehicle in the parking lot===");

		System.out.print("\nEnter the license plate to exit the parking lot: ");
		String plate = sc.next();
		Vehicle vehicle = vehicleDao.findByPlate(plate);
		//busca o veículo não cadastrado no estacionamento
		if (vehicle == null) {
			vehicle = vehicles.stream().filter(x -> x.getPlate().equals(plate)).findFirst().get();
		}
		List<ParkingSpot> list = parkingDao.findByVehicle(vehicle);
		Gate gate;
		
		//veículos que não são mensalistas ou caminhões de entrega precisam sair pelas suas respectivas cancelas
		if (vehicle.getCategory().name() != "SUBSCRIBER" && vehicle.getCategory().name() != "PUBLIC_SERVICE"
				&& vehicle.getCategory().name() != "DELIVERY_TRUCK") {
			System.out.print("Enter the id of the exit gate: ");
			gate = gateDao.findById(sc.nextInt());
		} else if (vehicle.getType().name() == "MOTORCYCLE") {
			gate = gateDao.findById(10);
		} else {
			gate = gateDao.findById(6);
		}

		Parking.registerExit(vehicle, gate, list);
		vehicles.remove(vehicle);
	}

}
