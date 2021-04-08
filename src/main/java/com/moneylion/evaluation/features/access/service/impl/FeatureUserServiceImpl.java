package com.moneylion.evaluation.features.access.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moneylion.evaluation.features.access.exception.FeatureAccessModificationException;
import com.moneylion.evaluation.features.access.exception.FeatureNotFoundException;
import com.moneylion.evaluation.features.access.exception.FeatureUserNotFoundException;
import com.moneylion.evaluation.features.access.model.FeatureUser;
import com.moneylion.evaluation.features.access.repository.FeatureUserRepository;
import com.moneylion.evaluation.features.access.service.FeatureService;
import com.moneylion.evaluation.features.access.service.FeatureUserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class FeatureUserServiceImpl implements FeatureUserService {

	private final FeatureUserRepository featureUserRepository;

	@Autowired
	private final FeatureService featureService;

	@Override
	public FeatureUser getFeatureUser(FeatureUser featureUser)
			throws FeatureNotFoundException, FeatureUserNotFoundException {

		featureService.getFeatureByName(featureUser.getId().getFeatureName());

		return featureUserRepository.findById(featureUser.getId())
				.orElseThrow(() -> FeatureUserNotFoundException.forFeatureUser(featureUser));
	}

	@Override
	public FeatureUser createOrModify(FeatureUser featureUser) throws FeatureAccessModificationException {
		try {
			return featureUserRepository.save(featureUser);
		} catch (Throwable e) {
			throw new FeatureAccessModificationException(e);
		}
	}
}