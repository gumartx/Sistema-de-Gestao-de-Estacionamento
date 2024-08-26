package model.entities;

import java.io.Serializable;
import java.util.Objects;

import model.enums.Category;
import model.enums.Type;

public class Vehicle implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String plate;
	private Category category;
	private Type type;

	public Vehicle(Integer id, String plate, Category category, Type type) {
		this.id = id;
		this.plate = plate;
		this.category = category;
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
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
		Vehicle other = (Vehicle) obj;
		return Objects.equals(id, other.id);
	}

}
