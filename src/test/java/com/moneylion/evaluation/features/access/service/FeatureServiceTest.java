package com.moneylion.evaluation.features.access.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.moneylion.evaluation.features.access.exception.FeatureNotFoundException;
import com.moneylion.evaluation.features.access.model.Feature;
import com.moneylion.evaluation.features.access.repository.FeatureRepository;
import com.moneylion.evaluation.features.access.service.impl.FeatureServiceImpl;

@ExtendWith(SpringExtension.class)
class FeatureServiceTest {

	private static final String DEFAULT_TEST_FEATURE_NAME = "UnitTestFeature";

	@Mock
	private FeatureRepository featureRepository;

	@InjectMocks
	private FeatureServiceImpl featureService;

	private Feature getMockFeature(String featureName) {
		return Feature.builder().name(featureName).build();
	}

	@Test
	void shouldFindFeature() throws FeatureNotFoundException {

		Feature feature = getMockFeature(DEFAULT_TEST_FEATURE_NAME);

		Mockito.when(featureRepository.findById(DEFAULT_TEST_FEATURE_NAME)).thenReturn(Optional.of(feature));

		assertEquals(feature, featureService.getFeatureByName(DEFAULT_TEST_FEATURE_NAME));
	}

	@Test
	void shouldNotFindFeature() {

		Mockito.when(featureRepository.findById(DEFAULT_TEST_FEATURE_NAME)).thenReturn(Optional.ofNullable(null));

		assertThrows(FeatureNotFoundException.class, () -> featureService.getFeatureByName(DEFAULT_TEST_FEATURE_NAME));
	}
}
