package com.moneylion.evaluation.features.access.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.moneylion.evaluation.features.access.dao.FeatureDAO;
import com.moneylion.evaluation.features.access.dao.FeatureUserDAO;
import com.moneylion.evaluation.features.access.exception.FeatureNotFoundException;
import com.moneylion.evaluation.features.access.model.Feature;
import com.moneylion.evaluation.features.access.model.FeatureUser;
import com.moneylion.evaluation.features.access.model.FeatureUser.FeatureUserId;

@ExtendWith(SpringExtension.class)
class FeatureServiceTest {

	@Mock
	private FeatureDAO featureDAO;

	@Mock
	private FeatureUserDAO featureUserDAO;

	private FeatureService featureService;

	private FeatureUserService featureUserService;

	@BeforeEach
	void beforeEach() {
		featureUserService = new FeatureUserService(featureUserDAO);
		featureService = new FeatureService(featureDAO);
	}

	private FeatureUser getMockFeatureUser(String featureName, String email, boolean canAccess) {

		Feature feature = getMockFeature(featureName);

		FeatureUserId featureUserId = FeatureUserId.builder().featureName(feature.getName()).userEmail(email).build();

		return FeatureUser.builder().id(featureUserId).feature(feature).isEnabled(canAccess).build();
	}

	private Feature getMockFeature(String featureName) {
		return Feature.builder().name(featureName).build();
	}

	@Test
	void shouldFindFeature() throws FeatureNotFoundException {

		String featureName = "UnitTestFeature";

		Feature feature = getMockFeature(featureName);

		Mockito.when(featureDAO.findOrDie(featureName)).thenCallRealMethod();

		Mockito.when(featureDAO.find(featureName)).thenReturn(Optional.ofNullable(feature));

		assertEquals(feature, featureService.getFeatureByName(featureName));
	}

	@Test
	void shouldThrowExceptionFeatureNotFound() throws FeatureNotFoundException {

		String featureName = "UnitTestFeature";

		Mockito.when(featureDAO.findOrDie(featureName)).thenCallRealMethod();

		Mockito.when(featureDAO.find(featureName)).thenReturn(Optional.ofNullable(null));

		assertThrows(FeatureNotFoundException.class, () -> featureService.getFeatureByName(featureName));
	}

	@Test
	void shouldFindFeatureUserCanAccess() throws FeatureNotFoundException {

		String featureName = "UnitTestFeature";
		String email = "unittest1@test.com";
		boolean canAccess = true;

		FeatureUser featureUser = getMockFeatureUser(featureName, email, canAccess);

		Mockito.when(featureUserDAO.findOrDefaultNoAccess(featureUser.getFeature(), email)).thenCallRealMethod();

		Mockito.when(featureUserDAO.find(featureUser.getFeature(), email)).thenReturn(Optional.of(featureUser));

		assertEquals(featureUser, featureUserService.getFeatureUser(featureUser.getFeature(), email));
		assertEquals(featureUser.getIsEnabled(),
				featureUserService.getFeatureUser(featureUser.getFeature(), email).getIsEnabled());
	}

	@Test
	void shouldFindFeatureUserCannotAccess() throws FeatureNotFoundException {

		String featureName = "UnitTestFeature";
		String email = "unittest1@test.com";
		boolean canAccess = false;

		FeatureUser featureUser = getMockFeatureUser(featureName, email, canAccess);

		Mockito.when(featureUserDAO.findOrDefaultNoAccess(featureUser.getFeature(), email)).thenCallRealMethod();

		Mockito.when(featureUserDAO.find(featureUser.getFeature(), email)).thenReturn(Optional.of(featureUser));

		assertEquals(featureUser, featureUserService.getFeatureUser(featureUser.getFeature(), email));
		assertEquals(featureUser.getIsEnabled(),
				featureUserService.getFeatureUser(featureUser.getFeature(), email).getIsEnabled());
	}

	@Test
	void shouldFindFeatureUserCannotAccessUserNotPresent() throws FeatureNotFoundException {

		String featureName = "UnitTestFeature";
		String email = "unittest1@test.com";
		boolean canAccess = false;

		FeatureUser featureUser = getMockFeatureUser(featureName, email, canAccess);

		Mockito.when(featureUserDAO.findOrDefaultNoAccess(featureUser.getFeature(), email)).thenCallRealMethod();

		Mockito.when(featureUserDAO.find(featureUser.getFeature(), email)).thenReturn(Optional.ofNullable(null));

		assertEquals(featureUser, featureUserService.getFeatureUser(featureUser.getFeature(), email));
		assertEquals(featureUser.getIsEnabled(),
				featureUserService.getFeatureUser(featureUser.getFeature(), email).getIsEnabled());
	}
}
