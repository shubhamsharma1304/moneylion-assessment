package com.moneylion.evaluation.features.access.exception;

//@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class FeatureNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8095663462939690735L;

	private FeatureNotFoundException(String message) {
		super(message);
	}

	public static FeatureNotFoundException forName(String featureName) {
		return new FeatureNotFoundException("Feature with name " + featureName + " could not be found.");
	}
}