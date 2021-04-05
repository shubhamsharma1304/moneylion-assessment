package com.moneylion.evaluation.features.access.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.moneylion.evaluation.features.access.model.Feature;
import com.moneylion.evaluation.features.access.repository.FeatureRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class FeatureService implements IFeatureService {

	private final FeatureRepository featureRepository;

	@Override
	public Optional<Feature> getFeatureByName(String featureName) {
		return featureRepository.findById(featureName);
	}
}