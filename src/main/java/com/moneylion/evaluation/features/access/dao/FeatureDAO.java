package com.moneylion.evaluation.features.access.dao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.moneylion.evaluation.features.access.exception.FeatureNotFoundException;
import com.moneylion.evaluation.features.access.model.Feature;
import com.moneylion.evaluation.features.access.repository.FeatureRepository;

@Component
public class FeatureDAO {

	@Autowired
	FeatureRepository featureRepository;

	public Optional<Feature> find(String featureName) {
		return featureRepository.findById(featureName);
	}

	public Feature findOrDie(String featureName) throws FeatureNotFoundException {
		return find(featureName).orElseThrow(() -> FeatureNotFoundException.forName(featureName));
	}
}