package model.entities;

import java.io.Serializable;
import java.util.Objects;

import model.enums.GateType;
import model.enums.Restriction;

public class Gate implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer number;
	private GateType type;
	private Restriction restriction;
	
	public Gate(Integer id, Integer number, GateType type, Restriction restriction) {
		this.id = id;
		this.number = number;
		this.type = type;
		this.restriction = restriction;
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
	public GateType getType() {
		return type;
	}
	public void setType(GateType type) {
		this.type = type;
	}
	public Restriction getRestriction() {
		return restriction;
	}
	public void setRestriction(Restriction restriction) {
		this.restriction = restriction;
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
		Gate other = (Gate) obj;
		return Objects.equals(id, other.id);
	}
	
	
}
