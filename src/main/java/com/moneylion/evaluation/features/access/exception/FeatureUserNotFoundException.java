package com.moneylion.evaluation.features.access.exception;

import com.moneylion.evaluation.features.access.model.FeatureUser;

public class FeatureUserNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8095663462939690735L;

	private FeatureUserNotFoundException(String message) {
		super(message);
	}

	public static FeatureUserNotFoundException forFeatureUser(FeatureUser featureUser) {
		return new FeatureUserNotFoundException("No access record for user '" + featureUser.getId().getUserEmail()
				+ "', against feature '" + featureUser.getId().getFeatureName() + "'.");
	}
}