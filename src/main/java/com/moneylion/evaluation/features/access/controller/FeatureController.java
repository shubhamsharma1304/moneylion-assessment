package com.moneylion.evaluation.features.access.controller;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import com.moneylion.evaluation.features.access.model.Feature;
import com.moneylion.evaluation.features.access.model.FeatureUser;
import com.moneylion.evaluation.features.access.model.bean.FeatureUserRequest;
import com.moneylion.evaluation.features.access.model.bean.FeatureUserResponse;
import com.moneylion.evaluation.features.access.service.FeatureUserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

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
	 * Since {@link FeatureController#addOrModifyFeatureAccessByEmail} standardizes
	 * the email address (trimming and converting to lower case), and feature name
	 * (trimming) before processing, this method also does the same for
	 * consistency.<br>
	 * <br>
	 * 
	 * @param email       The email address of the user whose access to the feature
	 *                    with name {@code featureName} is to be checked.
	 * @param featureName The name of feature for which the access of the user with
	 *                    email {@code email} has to be checked.
	 * @return A {@link FeatureUserResponse} entity representing if the user with
	 *         email {@code email} can access the feature with name
	 *         {@code featureName}.
	 * @throws FeatureNotFoundException     If there's no {@link Feature} with name
	 *                                      {@code featureName}
	 * @throws FeatureUserNotFoundException If there's no recorded data about
	 *                                      {@code email}'s access of
	 *                                      {@link Feature} with name featureName.
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Finds out if a certain user can access a certain feature.", notes = "Finds out if a certain user (identified by email) has access to a certain feature (identified by the name of the feature - featureName).")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = FeatureUserResponse.class),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Server Failure"), })
	public ResponseEntity<FeatureUserResponse> getFeatureAccessByEmail(
			@ApiParam(value = "The name of the feature for which you want to check access of a certain user.", required = true) @Email(message = "Request parameter email is not a valid email address. Please check.") @NotBlank(message = "Request parameter email should be a non-blank value.") @RequestParam
			String email,
			@ApiParam(value = "The email address of the user whose access you want to check against a certain feature.", required = true) @NotBlank(message = "Request parameter featureName name should be a non-blank value.") @RequestParam
			String featureName) throws FeatureNotFoundException, FeatureUserNotFoundException {

		FeatureUserRequest featureUserRequest = FeatureUserRequest.of(featureName, email, false);

		return ResponseEntity
				.ok(FeatureUserResponse.of(featureUserService.getFeatureUser(featureUserRequest)));
	}

	/**
	 * This method adds, enables, or disables a certain user's access to a certain
	 * feature.<br>
	 * <br>
	 * 
	 * @param featureUserRequest {@link FeatureUserRequest} representation of the
	 *                           request to add/enable/disable a certain user's
	 *                           access to a certain feature. <br>
	 *                           <br>
	 *                           Note:
	 *                           <ul>
	 *                           <li>The email address contained in
	 *                           {@code featureUserRequest} will be trimmed and will
	 *                           be converted to lower case by
	 *                           {@link FeatureUser#fromRequest} before further
	 *                           checking.</li>
	 *                           <li>The name of the feature contained in
	 *                           {@code featureUserRequest} will be trimmed by
	 *                           {@link FeatureUser#fromRequest} before further
	 *                           checking.</li>
	 *                           </ul>
	 * @throws FeatureAccessModificationException If there's no data recorded about
	 *                                            {@code email}'s (from inside
	 *                                            {@code {@link FeatureUserRequest
	 *                                            featureUserRequest}
	 *                                            featureUserRequest}) access of
	 *                                            {@link Feature} with name
	 *                                            {@code featureName} (from inside
	 *                                            {@link FeatureUserRequest
	 *                                            featureUserRequest} ).
	 */
	@ApiOperation(value = "Adds/Enables/Disables a certain user's access to a certain feature.", notes = "Adds/Enables/Disables a certain user's (identified by email in the JSON request body) access to a certain feature (identified by the name of the feature - featureName in the JSON request body).")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Void.class),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 304, message = "Not Modified"),
			@ApiResponse(code = 500, message = "Server Failure"), })
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> addOrModifyFeatureAccessByEmail(@Valid @RequestBody
	FeatureUserRequest featureUserRequest) throws FeatureAccessModificationException {
		featureUserService.createOrModify(featureUserRequest);
		return ResponseEntity.ok().build();
	}
}