package com.moneylion.evaluation.features.access.controller;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import com.moneylion.evaluation.features.access.exception.FeatureUserNotFoundException;
import com.moneylion.evaluation.features.access.model.FeatureUser;
import com.moneylion.evaluation.features.access.model.bean.FeatureUserRequest;
import com.moneylion.evaluation.features.access.model.bean.FeatureUserResponse;
import com.moneylion.evaluation.features.access.service.FeatureUserService;

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
	private FeatureUserService featureUserService;

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
	 * @throws FeatureUserNotFoundException
	 */
	@GetMapping
	@ResponseBody
	public ResponseEntity<FeatureUserResponse> getFeatureAccessByEmail(
			@Email @NotBlank(message = "Request parameter email is not a valid email address. Please check.") @RequestParam String email,
			@NotBlank(message = "Request parameter featureName name should be a non-blank value.") @RequestParam String featureName)
			throws FeatureNotFoundException, FeatureUserNotFoundException {

		FeatureUser featureUser = FeatureUserRequest.of(featureName, email, false).getFeatureUser();

		return ResponseEntity.ok(FeatureUserResponse.of(featureUserService.getFeatureUser(featureUser)));
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
	 * @return
	 */
	@PostMapping
	public ResponseEntity<?> addFeatureAccessByEmail(@Valid @RequestBody FeatureUserRequest featureUserRequest)
			throws FeatureAccessModificationException {
		featureUserService.createOrModify(featureUserRequest.getFeatureUser());
		return ResponseEntity.ok().build();
	}
}