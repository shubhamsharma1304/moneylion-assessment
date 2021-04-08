package com.moneylion.evaluation.features.access.model.bean;

import com.moneylion.evaluation.features.access.model.FeatureUser;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ApiModel(description = "Response to tell if the user/email in the corresponding request has access to the feature in the request.")
public class FeatureUserResponse {

	@ApiModelProperty(notes = "Boolean flag to tell if the user/email in the corresponding request has access to the feature in the request.", required = true)
	private final boolean canAccess;

	public static FeatureUserResponse of(FeatureUser featureUser) {
		return new FeatureUserResponse(featureUser.getIsEnabled());
	}
}