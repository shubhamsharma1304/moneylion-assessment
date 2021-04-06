package com.moneylion.evaluation.features.access.helpers;

public class CommonHelper {

	public static String standardizeEmailInput(String email) {

		return email.trim().toLowerCase();
	}

	public static String standardizeFeatureNameInput(String featureName) {

		return featureName.trim();
	}
}
