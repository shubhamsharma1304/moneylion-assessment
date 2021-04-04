package com.moneylion.evaluation.features.access.service;

import org.springframework.stereotype.Service;

import com.moneylion.evaluation.features.access.dao.FeatureUserDAO;
import com.moneylion.evaluation.features.access.exception.FeatureNotFoundException;
import com.moneylion.evaluation.features.access.model.Feature;
import com.moneylion.evaluation.features.access.model.FeatureUser;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class FeatureUserService {

	private final FeatureUserDAO featureUserDAO;

	public FeatureUser getFeatureUser(Feature feature, String email) throws FeatureNotFoundException {
		return featureUserDAO.findOrDefaultNoAccess(feature, email);
	}

	public FeatureUser createOrModify(FeatureUser featureUser) throws Throwable {
		return featureUserDAO.save(featureUser);
	}
}