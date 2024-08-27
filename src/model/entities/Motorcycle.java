package model.entities;

import model.enums.Category;

public class Motorcycle extends Vehicle {
	private static final long serialVersionUID = 1L;

	public Motorcycle(Integer id, String plate, Category category) {
		super(id, plate, category);
	} 

}
