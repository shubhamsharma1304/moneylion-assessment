package com.moneylion.evaluation.features.access.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.moneylion.evaluation.features.access.model.Feature;
import com.moneylion.evaluation.features.access.repository.FeatureRepository;
import com.moneylion.evaluation.features.access.service.FeatureService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class FeatureServiceImpl implements FeatureService {

	private final FeatureRepository featureRepository;

	@Override
	public Optional<Feature> getFeatureByName(String featureName) {
		return featureRepository.findById(featureName);
	}
}