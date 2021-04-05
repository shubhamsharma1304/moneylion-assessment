package com.moneylion.evaluation.features.access.service;

import java.util.Optional;

import com.moneylion.evaluation.features.access.model.Feature;
import com.moneylion.evaluation.features.access.model.FeatureUser;

public interface IFeatureUserService {

	/**
	 * This method will return an {@link Optional} over the {@link FeatureUser}
	 * entity for {@link Feature} {@code feature}, and email address {@code email}.
	 * Caller can then decide what to do if such a {@link FeatureUser} entity was
	 * not found. <br>
	 * 
	 * @param feature<br>
	 *                    <br>
	 * @param email<br>
	 *                    <br>
	 * @return<br>
	 *             <br>
	 */
	public Optional<FeatureUser> getFeatureUser(Feature feature, String email);

	/**
	 * This method will return a {@link FeatureUser} entity for {@link Feature}
	 * {@code feature}, and email address {@code email}. If such a
	 * {@link FeatureUser} entity cannot be found, a default {@link FeatureUser}
	 * will be returned. What values the returned default {@link FeatureUser} will
	 * be decided by the implementation.<br>
	 * 
	 * @param feature<br>
	 *                    <br>
	 * @param email<br>
	 *                    <br>
	 * @return a {@link FeatureUser} entity for {@link Feature} {@code feature}, and
	 *         email address {@code email}. If such a {@link FeatureUser} entity
	 *         cannot be found, a default {@link FeatureUser} will be returned. What
	 *         values the returned default {@link FeatureUser} will be decided by
	 *         the implementation. <br>
	 *         <br>
	 */
	public FeatureUser getFeatureUserOrDefault(Feature feature, String email);

	/**
	 * This method either creates a new persistent {@link FeatureUser} entity or
	 * updates the existing one if found.
	 * 
	 * @param featureUser The {@link FeatureUser} that needs to be persisted
	 *                    (created or updated).
	 * @return The {@link FeatureUser} {@code featureUser} after it has been
	 *         persisted.
	 * @throws Throwable If a problem was encountered while trying to create or
	 *                   update the {@link FeatureUser} {@code featureUser}.
	 */
	public FeatureUser createOrModify(FeatureUser featureUser) throws Throwable;
}