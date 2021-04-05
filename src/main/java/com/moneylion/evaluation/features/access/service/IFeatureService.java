package com.moneylion.evaluation.features.access.service;

import java.util.Optional;

import com.moneylion.evaluation.features.access.model.Feature;

public interface IFeatureService {

	/**
	 * This method will return an {@link Optional} over the {@link Feature} entity
	 * with name {@code featureName}. Caller can then decide what to do if such a
	 * {@link Feature} was not found. <br>
	 * 
	 * @param featureName The name of the feature to search for.<br>
	 *                    <br>
	 * @return An {@link Optional} over the {@link Feature} entity with name
	 *         {@code featureName}.
	 */
	public Optional<Feature> getFeatureByName(String featureName);
}