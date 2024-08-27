package model.entities;

import model.enums.Category;

public class DeliveryTruck extends Vehicle {
	private static final long serialVersionUID = 1L;

	public DeliveryTruck(Integer id, String plate, Category category) {
		super(id, plate, category);
	} 

}
