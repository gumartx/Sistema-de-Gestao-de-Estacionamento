package model.entities;

import java.io.Serializable;
import java.util.Objects;

import model.enums.Reserve;

public class ParkingSpot implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer number;
	private boolean status = false;
	private Reserve reserve = Reserve.NONE;
	private Vehicle vehicle;

	public ParkingSpot(Integer number, boolean status, Reserve reserve, Vehicle vehicle) {
		this.number = number;
		this.status = status;
		this.reserve = reserve;
		this.vehicle = vehicle;
	}

	public ParkingSpot(Integer number, boolean status, Reserve reserve) {
		this.number = number;
		this.status = status;
		this.reserve = reserve;
	}

	public Reserve getReserve() {
		return reserve;
	}

	public void setReserve(Reserve reserve) {
		this.reserve = reserve;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(number);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParkingSpot other = (ParkingSpot) obj;
		return Objects.equals(number, other.number);
	}

	@Override
	public String toString() {
		return number + "";
	}

}
