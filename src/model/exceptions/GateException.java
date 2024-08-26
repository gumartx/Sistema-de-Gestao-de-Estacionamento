package model.exceptions;

public class GateException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public GateException(String msg) {
		super(msg);
	}
}
