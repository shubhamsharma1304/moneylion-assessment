package com.moneylion.evaluation.features.access.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.moneylion.evaluation.features.access.exception.FeatureAccessModificationException;
import com.moneylion.evaluation.features.access.exception.FeatureNotFoundException;
import com.moneylion.evaluation.features.access.exception.handler.FeaturesExceptionHandler;
import com.moneylion.evaluation.features.access.model.FeatureUser;
import com.moneylion.evaluation.features.access.model.FeatureUser.FeatureUserRequest;
import com.moneylion.evaluation.features.access.service.FeatureService;
import com.moneylion.evaluation.features.access.service.FeatureUserService;

/**
 * @author Shubham Sharma
 *
 */
@RestController
@RequestMapping(FeatureController.BASE_URL)
public class FeatureController {

	static final String BASE_URL = "/feature";

	@Autowired
	private FeatureService featureService;

	@Autowired
	private FeatureUserService featureUserService;

	/**
	 * 
	 * The methods for adding users/emails, need to sanitize the data (trimming and
	 * converting to lower case) before saving This method will always trim the
	 * email and convert to lower case before further processing.
	 * 
	 * @param email       The email address of the user whose access to the feature
	 *                    with name {@code featureName} is to be checked. This email
	 *                    address will be trimmed and will be converted to lower
	 *                    case before further checking.
	 * @param featureName The name of feature for which the access of the user with
	 *                    email {@code email} has to be checked.
	 * @return A singleton map representing if the user with email {@code email} can
	 *         access the feature with name {@code featureName}. This map will then
	 *         later be converted to JSON before sending the response back to the
	 *         API consumer.
	 * @throws FeatureNotFoundException If the feature with name {@code featureName}
	 *                                  doesn't exist yet. This Exception will then
	 *                                  be caught by the relevant method in
	 *                                  {@link FeaturesExceptionHandler} class and
	 *                                  will be processed appropriately.
	 */
	@GetMapping
	@ResponseBody
	public Map<String, Boolean> getFeatureAccessByEmail(@RequestParam String email, @RequestParam String featureName)
			throws FeatureNotFoundException {

		boolean canAccess = featureUserService
				.getFeatureUser(featureService.getFeatureByName(featureName), email.trim().toLowerCase())
				.getIsEnabled();

		return Collections.singletonMap("canAccess", canAccess);
	}

	/**
	 * @param featureUserRequest
	 * @throws FeatureAccessModificationException
	 */
	@PostMapping
	public void addFeatureAccessByEmail(@RequestBody FeatureUserRequest featureUserRequest)
			throws FeatureAccessModificationException {
		try {
			featureUserService.createOrModify(FeatureUser.fromRequest(featureUserRequest));
		} catch (Throwable e) {
			throw FeatureAccessModificationException.forFeatureUserRequest(featureUserRequest, e);
		}
	}
}