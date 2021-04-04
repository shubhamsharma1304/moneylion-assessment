package com.moneylion.evaluation.features.access.service;

import org.springframework.stereotype.Service;

import com.moneylion.evaluation.features.access.dao.FeatureDAO;
import com.moneylion.evaluation.features.access.exception.FeatureNotFoundException;
import com.moneylion.evaluation.features.access.model.Feature;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class FeatureService {

	private final FeatureDAO featureDAO;

	public Feature getFeatureByName(String featureName) throws FeatureNotFoundException {
		return featureDAO.findOrDie(featureName);
	}
}