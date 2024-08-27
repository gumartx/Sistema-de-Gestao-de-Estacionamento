package model.entities;

import model.enums.Category;

public class Car extends Vehicle {
	private static final long serialVersionUID = 1L;

	public Car(Integer id, String plate, Category category) {
		super(id, plate, category);
	}

}
