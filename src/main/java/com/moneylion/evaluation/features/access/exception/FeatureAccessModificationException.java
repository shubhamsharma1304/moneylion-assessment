package com.moneylion.evaluation.features.access.exception;

public class FeatureAccessModificationException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8095663462939690735L;

	private FeatureAccessModificationException(String message, Throwable cause) {
		super(message, cause);
	}

	public FeatureAccessModificationException(Throwable cause) {

		this("Could not modify/create feature access for this request.", cause);
	}
}