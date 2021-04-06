package com.moneylion.evaluation.features.access.controller;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.moneylion.evaluation.features.access.exception.FeatureAccessModificationException;
import com.moneylion.evaluation.features.access.exception.FeatureNotFoundException;
import com.moneylion.evaluation.features.access.exception.handler.FeaturesAccessServiceExceptionHandler;
import com.moneylion.evaluation.features.access.model.FeatureUser;
import com.moneylion.evaluation.features.access.model.FeatureUser.FeatureUserRequest;
import com.moneylion.evaluation.features.access.model.FeatureUser.FeatureUserResponse;
import com.moneylion.evaluation.features.access.service.IFeatureService;
import com.moneylion.evaluation.features.access.service.IFeatureUserService;

/**
 * @author Shubham Sharma
 *
 */
@RestController
@RequestMapping(FeatureController.BASE_URL)
@Validated
public class FeatureController {

	static final String BASE_URL = "/feature";

	@Autowired
	private IFeatureService featureService;

	@Autowired
	private IFeatureUserService featureUserService;

	/**
	 * This method checks if the user with email address {@code email} can access
	 * the feature with name {@code featureName}. <br>
	 * <br>
	 * Note:
	 * <ul>
	 * <li>The email address in {@code email} will be trimmed and will be converted
	 * to lower case before further checking.</li>
	 * <li>The name of the feature in {@code featureName} will be trimmed before
	 * further checking.</li>
	 * </ul>
	 * Since {@link FeatureController#addFeatureAccessByEmail} standardizes the
	 * email address (trimming and converting to lower case), and feature name
	 * (trimming) before processing, this method also does the same.<br>
	 * <br>
	 * 
	 * @param email       The email address of the user whose access to the feature
	 *                    with name {@code featureName} is to be checked.
	 * @param featureName The name of feature for which the access of the user with
	 *                    email {@code email} has to be checked.
	 * @return A singleton map representing if the user with email {@code email} can
	 *         access the feature with name {@code featureName}. This map will then
	 *         later be converted to JSON before sending the response back to the
	 *         API consumer.
	 * @throws FeatureNotFoundException If the feature with name {@code featureName}
	 *                                  doesn't exist yet. This Exception will then
	 *                                  be caught by the relevant method in
	 *                                  {@link FeaturesAccessServiceExceptionHandler}
	 *                                  class and will be processed appropriately.
	 */
	@GetMapping
	@ResponseBody
	public FeatureUserResponse getFeatureAccessByEmail(
			@RequestParam @Email @NotBlank(message = "Request parameter email is not a valid email address. Please check.") String email,
			@RequestParam @NotBlank(message = "Request parameter featureName name should be a non-blank value.") String featureName)
			throws FeatureNotFoundException {

		FeatureUser featureUser = FeatureUser.fromRequest(new FeatureUserRequest(featureName, email, false));

		// Validate if a feature with name featureName exists.
		featureService.getFeatureByName(featureUser.getId().getFeatureName())
				.orElseThrow(() -> FeatureNotFoundException.forName(featureUser.getId().getFeatureName()));

		return featureUserService.getFeatureUser(featureUser).orElse(featureUser).getResponse();
	}

	/**
	 * This method adds or modifies the access of a use (email) to a certain
	 * feature.<br>
	 * <br>
	 * Note:
	 * <ul>
	 * <li>The {@code featureUserRequest} is the Java Object representation of the
	 * JSON present in the request body.</li>
	 * <li>The email address contained in {@code featureUserRequest} will be trimmed
	 * and will be converted to lower case by {@link FeatureUser#fromRequest} before
	 * further checking.</li>
	 * <li>The name of the feature contained in {@code featureUserRequest} will be
	 * trimmed by {@link FeatureUser#fromRequest} before further checking.</li>
	 * </ul>
	 * <br>
	 * 
	 * @param featureUserRequest Representation of a new request to change the
	 *                           access of a certain user for a certain request.
	 *                           This is created from the JSON present in the
	 *                           request body.<br>
	 *                           <br>
	 * @throws FeatureAccessModificationException If there's any problem while
	 *                                            trying to execute this request,
	 *                                            including but not limited to:
	 *                                            <ul>
	 *                                            <li>The feature in the request
	 *                                            doesn't exist</li>
	 *                                            <li>If either the input email
	 *                                            address is not a valid email
	 *                                            address or the input featureName
	 *                                            is blank.</li>
	 *                                            <li>Database connectivity
	 *                                            issue</li>. This Exception will
	 *                                            then be caught by the relevant
	 *                                            method in
	 *                                            {@link FeaturesAccessServiceExceptionHandler}
	 *                                            class and will be processed
	 *                                            appropriately.
	 */
	@PostMapping
	public void addFeatureAccessByEmail(@RequestBody @Valid FeatureUserRequest featureUserRequest)
			throws FeatureAccessModificationException {
		try {
			featureUserService.createOrModify(FeatureUser.fromRequest(featureUserRequest));
		} catch (Throwable e) {
			throw FeatureAccessModificationException.forFeatureUserRequest(featureUserRequest, e);
		}
	}
}