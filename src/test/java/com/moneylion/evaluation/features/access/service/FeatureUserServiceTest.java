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

import com.moneylion.evaluation.features.access.exception.FeatureNotFoundException;
import com.moneylion.evaluation.features.access.model.Feature;
import com.moneylion.evaluation.features.access.model.FeatureUser;
import com.moneylion.evaluation.features.access.model.FeatureUser.FeatureUserId;
import com.moneylion.evaluation.features.access.repository.FeatureUserRepository;

@ExtendWith(SpringExtension.class)
class FeatureUserServiceTest {

	@Mock
	private FeatureUserRepository featureUserRepository;

	@InjectMocks
	private FeatureUserService featureUserService;

	private FeatureUser getMockFeatureUser(String featureName, String email, boolean canAccess) {

		Feature feature = getMockFeature(featureName);

		FeatureUserId featureUserId = FeatureUserId.builder().featureName(feature.getName()).userEmail(email).build();

		return FeatureUser.builder().id(featureUserId).feature(feature).isEnabled(canAccess).build();
	}

	private Feature getMockFeature(String featureName) {
		return Feature.builder().name(featureName).build();
	}

	@Test
	void shouldFindFeatureUserCanAccess() throws FeatureNotFoundException {

		String featureName = "UnitTestFeature";
		String email = "unittest1@test.com";
		boolean canAccess = true;

		FeatureUser featureUser = getMockFeatureUser(featureName, email, canAccess);

		Mockito.when(featureUserRepository.findById(featureUser.getId())).thenReturn(Optional.of(featureUser));

		FeatureUser testFeatureUser = featureUserService.getFeatureUserOrDefault(featureUser.getFeature(), email);

		assertEquals(featureUser, testFeatureUser);
		assertEquals(featureUser.getIsEnabled(), testFeatureUser.getIsEnabled());
	}

	@Test
	void shouldFindFeatureUserCannotAccess() throws FeatureNotFoundException {

		String featureName = "UnitTestFeature";
		String email = "unittest1@test.com";
		boolean canAccess = false;

		FeatureUser featureUser = getMockFeatureUser(featureName, email, canAccess);

		Mockito.when(featureUserRepository.findById(featureUser.getId())).thenReturn(Optional.of(featureUser));

		FeatureUser testFeatureUser = featureUserService.getFeatureUserOrDefault(featureUser.getFeature(), email);

		assertEquals(featureUser, testFeatureUser);
		assertEquals(featureUser.getIsEnabled(), testFeatureUser.getIsEnabled());
	}

	@Test
	void shouldFindFeatureUserNotPresentDefaultCannotAccess() throws FeatureNotFoundException {

		String featureName = "UnitTestFeature";
		String email = "unittest1@test.com";
		boolean canAccess = false;

		FeatureUser featureUser = getMockFeatureUser(featureName, email, canAccess);

		Mockito.when(featureUserRepository.findById(featureUser.getId())).thenReturn(Optional.ofNullable(null));

		FeatureUser testFeatureUser = featureUserService.getFeatureUserOrDefault(featureUser.getFeature(), email);

		assertEquals(featureUser, testFeatureUser);
		assertEquals(featureUser.getIsEnabled(), testFeatureUser.getIsEnabled());
	}

	@Test
	void shouldCreateFeatureUser() throws Throwable {
		String featureName = "UnitTestFeature";
		String email = "unittest1@test.com";
		boolean canAccess = false;

		FeatureUser featureUser = getMockFeatureUser(featureName, email, canAccess);

		Mockito.when(featureUserRepository.save(featureUser)).thenReturn(featureUser);

		assertEquals(featureUser, featureUserService.createOrModify(featureUser));
	}

	@Test
	void shouldNotCreateFeatureUser() {
		String featureName = "UnitTestFeature";
		String email = "unittest1@test.com";
		boolean canAccess = false;

		FeatureUser featureUser = getMockFeatureUser(featureName, email, canAccess);

		Mockito.when(featureUserRepository.save(featureUser))
				.thenThrow(new JpaObjectRetrievalFailureException(new EntityNotFoundException()));

		assertThrows(JpaObjectRetrievalFailureException.class, () -> featureUserService.createOrModify(featureUser));
	}
}
