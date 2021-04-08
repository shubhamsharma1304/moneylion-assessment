package com.moneylion.evaluation.features.access.service.impl;

import org.springframework.stereotype.Service;

import com.moneylion.evaluation.features.access.exception.FeatureNotFoundException;
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
	public Feature getFeatureByName(String featureName) throws FeatureNotFoundException {
		return featureRepository.findById(featureName).orElseThrow(() -> FeatureNotFoundException.forName(featureName));
	}
}