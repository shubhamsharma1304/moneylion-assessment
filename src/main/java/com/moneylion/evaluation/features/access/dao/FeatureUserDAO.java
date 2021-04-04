package com.moneylion.evaluation.features.access.dao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.moneylion.evaluation.features.access.exception.FeatureNotFoundException;
import com.moneylion.evaluation.features.access.model.Feature;
import com.moneylion.evaluation.features.access.model.FeatureUser;
import com.moneylion.evaluation.features.access.model.FeatureUser.FeatureUserId;
import com.moneylion.evaluation.features.access.repository.FeatureUserRepository;

@Component
public class FeatureUserDAO {

	@Autowired
	FeatureUserRepository featureUserRepository;

	public Optional<FeatureUser> find(Feature feature, String email) {
		return featureUserRepository.findById(new FeatureUserId(feature.getName(), email));
	}

	public FeatureUser findOrDefaultNoAccess(Feature feature, String email) throws FeatureNotFoundException {
		return find(feature, email).orElse(new FeatureUser(new FeatureUserId(feature.getName(), email), false));
	}

	public FeatureUser save(FeatureUser featureUser) throws Throwable {
		return featureUserRepository.save(featureUser);
	}
}