package com.moneylion.evaluation.features.access.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.moneylion.evaluation.features.access.exception.FeatureAccessModificationException;
import com.moneylion.evaluation.features.access.exception.FeatureNotFoundException;
import com.moneylion.evaluation.features.access.exception.FeatureUserNotFoundException;
import com.moneylion.evaluation.features.access.model.Feature;
import com.moneylion.evaluation.features.access.model.FeatureUser;
import com.moneylion.evaluation.features.access.model.FeatureUser.FeatureUserId;
import com.moneylion.evaluation.features.access.repository.FeatureUserRepository;
import com.moneylion.evaluation.features.access.service.impl.FeatureUserServiceImpl;

@ExtendWith(SpringExtension.class)
class FeatureUserServiceTest {

	private static final String DEFAULT_TEST_FEATURE_NAME = "UnitTestFeature";
	private static final String DEFAULT_TEST_EMAIL = "unittest1@test.com";
	boolean CAN_ACCESS_YES = true;
	boolean CANNOT_ACCESS_NO = !CAN_ACCESS_YES;

	@Mock
	private FeatureUserRepository featureUserRepository;

	@Mock
	private FeatureService featureService;

	@InjectMocks
	private FeatureUserServiceImpl featureUserService;

	private FeatureUser getMockFeatureUser(String featureName, String email, boolean canAccess) {

		Feature feature = getMockFeature(featureName);

		FeatureUserId featureUserId = FeatureUserId.builder().featureName(feature.getName()).userEmail(email).build();

		return FeatureUser.builder().id(featureUserId).feature(feature).isEnabled(canAccess).build();
	}

	private Feature getMockFeature(String featureName) {
		return Feature.builder().name(featureName).build();
	}

	@Test
	void shouldFindFeatureUserCanAccess() throws FeatureUserNotFoundException, FeatureNotFoundException {

		FeatureUser featureUser = getMockFeatureUser(DEFAULT_TEST_FEATURE_NAME, DEFAULT_TEST_EMAIL, CAN_ACCESS_YES);

		Mockito.when(featureUserRepository.findById(featureUser.getId())).thenReturn(Optional.of(featureUser));

		FeatureUser testFeatureUser = featureUserService.getFeatureUser(featureUser);

		assertEquals(featureUser, testFeatureUser);
		assertEquals(featureUser.getIsEnabled(), testFeatureUser.getIsEnabled());
	}

	@Test
	void shouldFindFeatureUserCannotAccess() throws FeatureNotFoundException, FeatureUserNotFoundException {

		FeatureUser featureUser = getMockFeatureUser(DEFAULT_TEST_FEATURE_NAME, DEFAULT_TEST_EMAIL, CANNOT_ACCESS_NO);

		Mockito.when(featureUserRepository.findById(featureUser.getId())).thenReturn(Optional.of(featureUser));

		FeatureUser testFeatureUser = featureUserService.getFeatureUser(featureUser);

		assertEquals(featureUser, testFeatureUser);
		assertEquals(featureUser.getIsEnabled(), testFeatureUser.getIsEnabled());
	}

	@Test
	void shouldNotFindFeatureUser() throws FeatureNotFoundException {

		boolean canAccess = false;

		FeatureUser featureUser = getMockFeatureUser(DEFAULT_TEST_FEATURE_NAME, DEFAULT_TEST_EMAIL, canAccess);

		Mockito.when(featureService.getFeatureByName(featureUser.getId().getFeatureName()))
				.thenReturn(featureUser.getFeature());

		Mockito.when(featureUserRepository.findById(featureUser.getId())).thenReturn(Optional.ofNullable(null));

		assertThrows(FeatureUserNotFoundException.class, () -> featureUserService.getFeatureUser(featureUser));
	}

	@Test
	void shouldCreateFeatureUser() throws Throwable {
		boolean canAccess = false;

		FeatureUser featureUser = getMockFeatureUser(DEFAULT_TEST_FEATURE_NAME, DEFAULT_TEST_EMAIL, canAccess);

		Mockito.when(featureUserRepository.save(featureUser)).thenReturn(featureUser);

		assertEquals(featureUser, featureUserService.createOrModify(featureUser));
	}

	@Test
	void shouldNotCreateFeatureUser() {
		boolean canAccess = false;

		FeatureUser featureUser = getMockFeatureUser(DEFAULT_TEST_FEATURE_NAME, DEFAULT_TEST_EMAIL, canAccess);

		Mockito.when(featureUserRepository.save(featureUser))
				.thenThrow(new JpaObjectRetrievalFailureException(new EntityNotFoundException()));

		assertThrows(FeatureAccessModificationException.class, () -> featureUserService.createOrModify(featureUser));
	}
}
