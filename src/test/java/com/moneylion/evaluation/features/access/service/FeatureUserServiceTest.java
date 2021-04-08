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
import com.moneylion.evaluation.features.access.model.FeatureUser;
import com.moneylion.evaluation.features.access.model.bean.FeatureUserRequest;
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

	@Test
	void shouldFindFeatureUserCanAccess()
			throws FeatureUserNotFoundException, FeatureNotFoundException {

		FeatureUserRequest featureUserRequest = FeatureUserRequest.of(DEFAULT_TEST_FEATURE_NAME,
				DEFAULT_TEST_EMAIL, CAN_ACCESS_YES);

		FeatureUser featureUser = featureUserRequest.getFeatureUser();

		Mockito.when(featureUserRepository.findById(featureUser.getId()))
				.thenReturn(Optional.of(featureUser));

		FeatureUser testFeatureUser = featureUserService.getFeatureUser(FeatureUserRequest
				.of(DEFAULT_TEST_FEATURE_NAME, DEFAULT_TEST_EMAIL, CAN_ACCESS_YES));

		assertEquals(featureUser, testFeatureUser);
		assertEquals(featureUser.getIsEnabled(), testFeatureUser.getIsEnabled());
	}

	@Test
	void shouldFindFeatureUserCannotAccess()
			throws FeatureNotFoundException, FeatureUserNotFoundException {

		FeatureUserRequest featureUserRequest = FeatureUserRequest.of(DEFAULT_TEST_FEATURE_NAME,
				DEFAULT_TEST_EMAIL, CANNOT_ACCESS_NO);

		FeatureUser featureUser = featureUserRequest.getFeatureUser();

		Mockito.when(featureUserRepository.findById(featureUser.getId()))
				.thenReturn(Optional.of(featureUser));

		FeatureUser testFeatureUser = featureUserService.getFeatureUser(featureUserRequest);

		assertEquals(featureUser, testFeatureUser);
		assertEquals(featureUser.getIsEnabled(), testFeatureUser.getIsEnabled());
	}

	@Test
	void shouldNotFindFeatureUser() throws FeatureNotFoundException {

		FeatureUserRequest featureUserRequest = FeatureUserRequest.of(DEFAULT_TEST_FEATURE_NAME,
				DEFAULT_TEST_EMAIL, CANNOT_ACCESS_NO);

		FeatureUser featureUser = featureUserRequest.getFeatureUser();

		Mockito.when(featureService.getFeatureByName(featureUser.getId().getFeatureName()))
				.thenReturn(featureUser.getFeature());

		Mockito.when(featureUserRepository.findById(featureUser.getId()))
				.thenReturn(Optional.ofNullable(null));

		assertThrows(FeatureUserNotFoundException.class,
				() -> featureUserService.getFeatureUser(featureUserRequest));
	}

	@Test
	void shouldCreateFeatureUser() throws Throwable {

		FeatureUserRequest featureUserRequest = FeatureUserRequest.of(DEFAULT_TEST_FEATURE_NAME,
				DEFAULT_TEST_EMAIL, CANNOT_ACCESS_NO);

		FeatureUser featureUser = featureUserRequest.getFeatureUser();

		Mockito.when(featureUserRepository.save(featureUser)).thenReturn(featureUser);

		assertEquals(featureUser, featureUserService.createOrModify(featureUserRequest));
	}

	@Test
	void shouldNotCreateFeatureUser() {

		FeatureUserRequest featureUserRequest = FeatureUserRequest.of(DEFAULT_TEST_FEATURE_NAME,
				DEFAULT_TEST_EMAIL, CANNOT_ACCESS_NO);

		FeatureUser featureUser = featureUserRequest.getFeatureUser();

		Mockito.when(featureUserRepository.save(featureUser))
				.thenThrow(new JpaObjectRetrievalFailureException(new EntityNotFoundException()));

		assertThrows(FeatureAccessModificationException.class,
				() -> featureUserService.createOrModify(featureUserRequest));
	}
}
