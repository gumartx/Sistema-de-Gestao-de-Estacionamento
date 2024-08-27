package model.entities;

import model.enums.Category;

public class PublicService extends Vehicle {
	private static final long serialVersionUID = 1L;

	public PublicService(Integer id, String plate, Category category) {
		super(id, plate, category);
	} 

}
