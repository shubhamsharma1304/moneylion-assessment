package com.moneylion.evaluation.features.access.service;

import java.util.Optional;

import com.moneylion.evaluation.features.access.exception.FeatureAccessModificationException;
import com.moneylion.evaluation.features.access.exception.FeatureNotFoundException;
import com.moneylion.evaluation.features.access.exception.FeatureUserNotFoundException;
import com.moneylion.evaluation.features.access.model.FeatureUser;

public interface FeatureUserService {

	/**
	 * This method will return an {@link Optional} over a persistent
	 * {@link FeatureUser} entity that matches with {@code featureUser}. If such a
	 * persistent {@link FeatureUser} entity cannot be found, it will return an
	 * {@link Optional} over {@code null}. <br>
	 * 
	 * @param featureUser<br>
	 *                        <br>
	 * @return An {@link Optional} over a persistent {@link FeatureUser} entity that
	 *         matches with {@code featureUser}. If such a persistent
	 *         {@link FeatureUser} entity cannot be found, then an {@link Optional}
	 *         over {@code null}.<br>
	 *         <br>
	 */
	public FeatureUser getFeatureUser(FeatureUser featureUser)
			throws FeatureNotFoundException, FeatureUserNotFoundException;

	/**
	 * This method either creates a new persistent {@link FeatureUser} entity or
	 * updates the existing one if found.
	 * 
	 * @param featureUser The {@link FeatureUser} that needs to be persisted
	 *                    (created or updated).
	 * @return The {@link FeatureUser} {@code featureUser} after it has been
	 *         persisted.
	 * @throws FeatureAccessModificationException If a problem was encountered while
	 *                                            trying to create or update the
	 *                                            {@link FeatureUser}
	 *                                            {@code featureUser}.
	 */
	public FeatureUser createOrModify(FeatureUser featureUser) throws FeatureAccessModificationException;
}