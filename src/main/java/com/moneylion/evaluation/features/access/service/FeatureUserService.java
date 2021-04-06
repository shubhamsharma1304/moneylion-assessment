package com.moneylion.evaluation.features.access.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.moneylion.evaluation.features.access.model.FeatureUser;
import com.moneylion.evaluation.features.access.repository.FeatureUserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class FeatureUserService implements IFeatureUserService {

	private final FeatureUserRepository featureUserRepository;

	@Override
	public Optional<FeatureUser> getFeatureUser(FeatureUser featureUser) {
		return featureUserRepository.findById(featureUser.getId());
	}

	@Override
	public FeatureUser createOrModify(FeatureUser featureUser) throws Throwable {
		return featureUserRepository.save(featureUser);
	}
}