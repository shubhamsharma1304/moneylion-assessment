package com.moneylion.evaluation.features.access.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
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

@ExtendWith(SpringExtension.class)
class FeatureServiceTest {

	@Mock
	private FeatureRepository featureRepository;


	@InjectMocks
	private FeatureService featureService;

	private Feature getMockFeature(String featureName) {
		return Feature.builder().name(featureName).build();
	}

	@Test
	void shouldFindFeature() throws FeatureNotFoundException {

		String featureName = "UnitTestFeature";

		Feature feature = getMockFeature(featureName);

		Mockito.when(featureRepository.findById(featureName)).thenReturn(Optional.of(feature));

		assertEquals(feature, featureService.getFeatureByName(featureName).get());
	}

	@Test
	void shouldNotFindFeature() {

		String featureName = "UnitTestFeature";

		Mockito.when(featureRepository.findById(featureName)).thenReturn(Optional.ofNullable(null));

		assertThrows(NoSuchElementException.class, () -> featureService.getFeatureByName(featureName).get());
	}
}
