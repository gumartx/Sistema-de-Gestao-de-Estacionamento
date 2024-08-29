package parking.exceptions;

public class ParkingException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ParkingException(String msg) {
		super(msg);
	}
}
