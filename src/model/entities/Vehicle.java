package model.entities;

import java.io.Serializable;
import java.util.Objects;

import model.enums.Category;
import model.enums.VehicleType;

public class Vehicle implements Serializable {
	private static final long serialVersionUID = 1L;

	private String plate;
	private Category category;
	private VehicleType type;

	public Vehicle(String plate, Category category, VehicleType type) {
		this.plate = plate;
		this.category = category;
		this.type = type;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public VehicleType getType() {
		return type;
	}

	public void setType(VehicleType type) {
		this.type = type;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Override
	public int hashCode() {
		return Objects.hash(plate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vehicle other = (Vehicle) obj;
		return Objects.equals(plate, other.plate);
	}

}
