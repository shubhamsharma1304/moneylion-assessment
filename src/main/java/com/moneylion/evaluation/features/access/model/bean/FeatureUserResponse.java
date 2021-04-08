package com.moneylion.evaluation.features.access.model.bean;

import com.moneylion.evaluation.features.access.model.FeatureUser;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class FeatureUserResponse {

	private final boolean canAccess;

	public static FeatureUserResponse of(FeatureUser featureUser) {
		return new FeatureUserResponse(featureUser.getIsEnabled());
	}
}