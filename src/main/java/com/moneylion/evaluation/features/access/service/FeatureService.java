package com.moneylion.evaluation.features.access.service;

import com.moneylion.evaluation.features.access.exception.FeatureNotFoundException;
import com.moneylion.evaluation.features.access.model.Feature;

public interface FeatureService {

	/**
	 * This method will return a {@link Feature} entity with name
	 * {@code featureName}. If any such entity exists.
	 * 
	 * @param featureName The name of the feature to search for.
	 * @return A {@link Feature} entity with name {@code featureName} if found.
	 * @throws FeatureNotFoundException If a {@link Feature} with name
	 *                                  {@code featureName} cannot be found.
	 */
	public Feature getFeatureByName(String featureName)
			throws FeatureNotFoundException;
}