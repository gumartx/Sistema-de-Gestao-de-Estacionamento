package model.entities;

import java.io.Serializable;
import java.util.Objects;

public class ParkingSpot implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer number;
	private boolean status = false;

	public ParkingSpot(Integer id, Integer number, boolean status) {
		this.id = id;
		this.number = number;
		this.status = status;
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

}
