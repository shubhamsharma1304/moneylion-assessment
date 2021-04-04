package com.moneylion.evaluation.features.access.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "feature_user")
public class FeatureUser {

	@NonNull
	@EmbeddedId
	private FeatureUserId id;

	@EqualsAndHashCode.Exclude
	@ManyToOne
	@MapsId("featureName")
	@JoinColumn(name = "feature_name")
	private Feature feature;

	@EqualsAndHashCode.Exclude
	@NonNull
	@Column(name = "enabled", nullable = false, updatable = true)
	private Boolean isEnabled;

	public static FeatureUser fromRequest(FeatureUserRequest featureUserRequest) {
		FeatureUser featureUser = new FeatureUser(
				new FeatureUserId(featureUserRequest.getFeatureName(),
						featureUserRequest.getEmail().trim().toLowerCase()),
				new Feature(featureUserRequest.getFeatureName()), featureUserRequest.isEnable());

		return featureUser;
	}

	@Data
	@Builder
	@EqualsAndHashCode
	@NoArgsConstructor
	@AllArgsConstructor
	@Embeddable
	static public class FeatureUserId implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -7183481013645453513L;

		@Column(name = "feature_name", nullable = false, updatable = false)
		private String featureName;

		@Column(name = "user_email", nullable = false, updatable = false)
		private String userEmail;
	}

	@Data
	@AllArgsConstructor
	static public class FeatureUserRequest {
		String featureName;
		String email;
		boolean enable;
	}
}
