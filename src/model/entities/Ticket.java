package model.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Ticket implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private Vehicle vehicle;
	private Gate exitGate;
	private Gate entryGate;
	private ParkingSpot parkingSpot;
	private LocalDateTime entryTime;
	private LocalDateTime exitTime;
	private Double amountPaid;

	public Ticket() {
	}

	public Ticket(Integer id, Vehicle vehicle, Gate exitGate, Gate entryGate, ParkingSpot parkingSpot,
			LocalDateTime entryTime, LocalDateTime exitTime, Double amountPaid) {
		this.id = id;
		this.vehicle = vehicle;
		this.exitGate = exitGate;
		this.entryGate = entryGate;
		this.parkingSpot = parkingSpot;
		this.entryTime = entryTime;
		this.exitTime = exitTime;
		this.amountPaid = amountPaid;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public Gate getExitGate() {
		return exitGate;
	}

	public void setExitGate(Gate exitGate) {
		this.exitGate = exitGate;
	}

	public Gate getEntryGate() {
		return entryGate;
	}

	public void setEntryGate(Gate entryGate) {
		this.entryGate = entryGate;
	}

	public ParkingSpot getParkingSpot() {
		return parkingSpot;
	}

	public void setParkingSpot(ParkingSpot parkingSpot) {
		this.parkingSpot = parkingSpot;
	}

	public LocalDateTime getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(LocalDateTime entryTime) {
		this.entryTime = entryTime;
	}

	public LocalDateTime getExitTime() {
		return exitTime;
	}

	public void setExitTime(LocalDateTime exitTime) {
		this.exitTime = exitTime;
	}

	public Double getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ticket other = (Ticket) obj;
		return Objects.equals(id, other.id);
	}

}
