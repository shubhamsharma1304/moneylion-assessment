package com.moneylion.evaluation.features.access.model.bean;

import static com.moneylion.evaluation.features.access.helpers.CommonHelper.standardizeEmailInput;
import static com.moneylion.evaluation.features.access.helpers.CommonHelper.standardizeFeatureNameInput;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.moneylion.evaluation.features.access.model.Feature;
import com.moneylion.evaluation.features.access.model.FeatureUser;
import com.moneylion.evaluation.features.access.model.FeatureUser.FeatureUserId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents the JSON request for {@link FeatureUser}
 * modification/creation. Structurally it mirrors the actual persistent
 * {@link FeatureUser} entity (in database).
 * 
 * @author Shubham Sharma
 *
 */
@ApiModel(description = "Request to add, enable, or disable a certain user's access to a certain feature.")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "featureName", "email", "enable" })
public class FeatureUserRequest {

	@ApiModelProperty(notes = "The name of the feature for which you want to check access of a certain user.")
	@Valid
	@NotBlank(message = "Field featureName should be a non-blank value.")
	private String featureName;

	@ApiModelProperty(notes = "The email address of the user whose access you want to check against a certain feature.")
	@Valid
	@NotBlank(message = "Field email should be a non-blank value.")
	@Email(message = "Field email is not a valid email address. Please check.")
	private String email;

	@ApiModelProperty(notes = "The boolean flag that indicates if a certain user's access to a certain feature is enabled/disabled")
	boolean enable;

	/**
	 * Creates a new {@link FeatureUserRequest} using {@code featureName},
	 * {@code email}, and {@code enable}.
	 * 
	 * @param featureName Name of the {@link Feature} for which to create this
	 *                    {@link FeatureUserRequest}
	 * @param email       email of the user for which to create this
	 *                    {@link FeatureUserRequest}
	 * @param enable      Flag to signify if {@code email}'s access to
	 *                    {@code featureName} should be enabled or disabled.
	 * @return
	 */
	public static FeatureUserRequest of(String featureName, String email,
			boolean enable) {
		return new FeatureUserRequest(featureName, email, enable);
	}

	/**
	 * Get a {@link FeatureUser} entity representation of {@code this}
	 * {@link FeatureUserRequest}.
	 * 
	 * @param featureUserRequest A {@link FeatureUserRequest} created from JSON
	 *                           request for {@link FeatureUser}
	 *                           modification/creation from user.
	 * @return {@link FeatureUser} entity representation of {@code this}
	 *         {@link FeatureUserRequest}
	 */
	@JsonIgnore
	public FeatureUser getFeatureUser() {

		String validEmail = standardizeEmailInput(this.getEmail());
		String validFeatureName = standardizeFeatureNameInput(
				this.getFeatureName());

		FeatureUser featureUser = new FeatureUser(
				new FeatureUserId(validFeatureName, validEmail),
				new Feature(validFeatureName), this.isEnable());

		return featureUser;
	}
}