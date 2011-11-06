package net.FarmTab.app;

public class FarmTabException extends RuntimeException {
    public FarmTabException() {
        super("There was an error with FarmTab. Please contact amk528@cs.nyu.edu to report this bug");
	}
	
	public FarmTabException(String message) {
        super(message);
	}
}
