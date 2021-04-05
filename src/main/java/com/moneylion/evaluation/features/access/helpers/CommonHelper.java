package com.moneylion.evaluation.features.access.helpers;

import org.apache.commons.validator.routines.EmailValidator;

import com.moneylion.evaluation.features.access.exception.InvalidInputException;

public class CommonHelper {

	private static final EmailValidator EMAIL_VALIDATOR = EmailValidator.getInstance();

	public static String sanitizeEmailInput(String email) throws InvalidInputException {

		String standardizedEmail = email.trim().toLowerCase();

		if (EMAIL_VALIDATOR.isValid(standardizedEmail)) {
			return standardizedEmail;
		} else {
			throw new InvalidInputException("Not a valid email address. Please check.");
		}
	}

	public static String sanitizeFeatureNameInput(String featureName) throws InvalidInputException {

		String standardizedfeatureName = featureName.trim();

		if (!standardizedfeatureName.isEmpty()) {
			return standardizedfeatureName;
		} else {
			throw new InvalidInputException("Feature name should be a non-blank value.");
		}
	}
}
