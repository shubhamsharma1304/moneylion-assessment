package com.moneylion.evaluation.features.access.service;

import java.util.Optional;
import java.util.function.BiFunction;

import org.springframework.stereotype.Service;

import com.moneylion.evaluation.features.access.model.Feature;
import com.moneylion.evaluation.features.access.model.FeatureUser;
import com.moneylion.evaluation.features.access.model.FeatureUser.FeatureUserId;
import com.moneylion.evaluation.features.access.repository.FeatureUserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class FeatureUserService implements IFeatureUserService {

	private final FeatureUserRepository featureUserRepository;

	private static final boolean DEFAULT_ACCESS = false;

	private static final BiFunction<Feature, String, FeatureUser> DEFAULT = (feature,
			email) -> new FeatureUser(new FeatureUserId(feature.getName(), email), DEFAULT_ACCESS);

	@Override
	public Optional<FeatureUser> getFeatureUser(Feature feature, String email) {
		return featureUserRepository.findById(new FeatureUserId(feature.getName(), email));
	}

	/**
	 * @implNote The returned default {@link FeatureUser} will be as if the
	 *           {@code email} doesn't have access to the {@link Feature}
	 *           {@code feature}.
	 */
	@Override
	public FeatureUser getFeatureUserOrDefault(Feature feature, String email) {
		return getFeatureUser(feature, email).orElse(DEFAULT.apply(feature, email));
	}

	@Override
	public FeatureUser createOrModify(FeatureUser featureUser) throws Throwable {
		return featureUserRepository.save(featureUser);
	}
}