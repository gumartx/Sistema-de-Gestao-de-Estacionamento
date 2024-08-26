package model.exceptions;

public class TicketException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public TicketException(String msg) {
		super(msg);
	}
}
