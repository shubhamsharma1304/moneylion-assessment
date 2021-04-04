package com.moneylion.evaluation.features.access.exception;

import com.moneylion.evaluation.features.access.model.FeatureUser.FeatureUserRequest;

public class FeatureAccessModificationException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8095663462939690735L;

	private FeatureAccessModificationException(String message, Throwable cause) {
		super(message, cause);
	}

	public static FeatureAccessModificationException forFeatureUserRequest(FeatureUserRequest featureUserRequest,
			Throwable cause) {

		return new FeatureAccessModificationException(
				"Could not modify/create feature access for " + featureUserRequest, cause);
	}
}