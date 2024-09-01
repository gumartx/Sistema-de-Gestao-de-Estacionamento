package application;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import model.entities.Vehicle;
import parking.ParkingHandler;
import parking.VehicleRegistration;

public class Program {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		//coleção dos veículos que não possuem cadastro
		Set<Vehicle> vehicles = new HashSet<>();
		
		int n = 1;
		while (n != 0) {
			System.out.println("\n========================");
			System.out.println("Register vehicle        :  1");
			System.out.println("Entry a vehicle         :  2");
			System.out.println("Exit a vehicle          :  3");
			System.out.println("Verify free spots       :  4");
			System.out.println("Exit system             :  0");
			System.out.println("========================");
			System.out.print("\nSelect a number: ");
			n = sc.nextInt();
			switch (n) {

			case 1:
				VehicleRegistration.register(sc);
				break;
			case 2:
				vehicles = ParkingHandler.entryParking(sc).stream().collect(Collectors.toSet());
				break;
			case 3:
				ParkingHandler.exitParking(vehicles, sc);
				break;
			case 4:
				ParkingHandler.verifySpots();
				break;
			default:
				n = 0;
			}
		}
		
		sc.close();
	}

}
