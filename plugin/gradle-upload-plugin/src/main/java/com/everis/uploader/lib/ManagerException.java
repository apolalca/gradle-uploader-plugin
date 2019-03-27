package com.everis.uploader.lib;

public class ManagerException extends Exception {

    /**
	 * Default serial version
	 */
	private static final long serialVersionUID = 1L;

	public ManagerException(String str) {
        super("ERROR: " + str);
    }

    public ManagerException(Exception ex) {
        super(ex);
    }
}
