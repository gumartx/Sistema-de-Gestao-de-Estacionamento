package model.entities;

import java.io.Serializable;
import java.util.Objects;

import model.enums.Reserve;

public class ParkingSpot implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer number;
	private boolean status = false;
	private Reserve reserve = Reserve.NONE;
	private Vehicle vehicle;
	private Ticket ticket;

	public ParkingSpot(Integer id, Integer number, boolean status, Reserve reserve, Vehicle vehicle) {
		this.id = id;
		this.number = number;
		this.status = status;
		this.reserve = reserve;
		this.vehicle = vehicle;
	}

	public ParkingSpot(Integer id, Integer number, boolean status, Reserve reserve, Ticket ticket) {
		this.id = id;
		this.number = number;
		this.status = status;
		this.reserve = reserve;
		this.ticket = ticket;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
		ParkingSpot other = (ParkingSpot) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return number + "";
	}

}
