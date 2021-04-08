package com.moneylion.evaluation.features.access.service;

import com.moneylion.evaluation.features.access.exception.FeatureAccessModificationException;
import com.moneylion.evaluation.features.access.exception.FeatureNotFoundException;
import com.moneylion.evaluation.features.access.exception.FeatureUserNotFoundException;
import com.moneylion.evaluation.features.access.model.Feature;
import com.moneylion.evaluation.features.access.model.FeatureUser;
import com.moneylion.evaluation.features.access.model.bean.FeatureUserRequest;

public interface FeatureUserService {

	/**
	 * This method will first check if the {@link Feature} which the
	 * {@link FeatureUserRequest featureUserRequest} represents, exists or not.
	 * If it does, then this method will return a {@link FeatureUser} entity
	 * that matches the {@link FeatureUserRequest featureUserRequest}, if such
	 * an entity exists.
	 * 
	 * @param featureUserRequest The {@link FeatureUserRequest} for which
	 *                           corresponding {@link FeatureUser} has to be
	 *                           retrieved.
	 * @return A {@link FeatureUser} entity that matches
	 *         {@link FeatureUserRequest featureUserRequest} if such an entity
	 *         exists.
	 * @throws FeatureNotFoundException     If the {@link Feature} entity which
	 *                                      the {@link FeatureUserRequest
	 *                                      featureUserRequest} represents,
	 *                                      doesn't exist.
	 * @throws FeatureUserNotFoundException If a persistent {@link FeatureUser}
	 *                                      entity matching
	 *                                      {@link FeatureUserRequest
	 *                                      featureUserRequest} doesn't exist.
	 * 
	 */
	public FeatureUser getFeatureUser(FeatureUserRequest featureUserRequest)
			throws FeatureNotFoundException, FeatureUserNotFoundException;

	/**
	 * This method either creates a new persistent {@link FeatureUser} entity or
	 * updates the existing one if found.
	 * 
	 * @param featureUserRequest {@link FeatureUserRequest} representation of
	 *                           {@link FeatureUser} entity that has to be added
	 *                           or modified.
	 * @return The {@link FeatureUser} The {@link FeatureUser} entity that was
	 *         added or modified as a resulr of this method execution.
	 *         persisted.
	 * @throws FeatureAccessModificationException If a problem was encountered
	 *                                            while trying to create or
	 *                                            update {@link FeatureUser}
	 *                                            represented by
	 *                                            {@link FeatureUserRequest}.
	 */
	public FeatureUser createOrModify(FeatureUserRequest featureUserRequest)
			throws FeatureAccessModificationException;
}