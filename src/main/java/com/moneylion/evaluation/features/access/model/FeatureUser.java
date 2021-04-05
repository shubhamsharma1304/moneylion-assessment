package com.moneylion.evaluation.features.access.model;

import static com.moneylion.evaluation.features.access.helpers.CommonHelper.sanitizeEmailInput;
import static com.moneylion.evaluation.features.access.helpers.CommonHelper.sanitizeFeatureNameInput;;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.moneylion.evaluation.features.access.exception.InvalidInputException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * This class models a record in the "feature_user" table. Which in turn
 * represents a real life mapping between a {@link Feature} and a user (email).
 * The information each {@link FeatureUser} represents is if a certain user
 * (email) can access a certain {@link Feature} or not, as described by the
 * {@code boolean} field {@code isEnabled} of that {@link Feature}. The field
 * {@code isEnabled} maps to the column "enabled". A {@link FeatureUser} is
 * identified by the combination of {@link Feature} and user (email). Meaning
 * there could be multiple {@link FeatureUser} entities corresponding to the
 * same {@link Feature}, as long as the combination of {@link Feature} and user
 * (email) for that entity is unique.
 * 
 * @author Shubham Sharma
 *
 */
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

	/**
	 * This class represents the unique key for {@link FeatureUser} entity. This
	 * unique key is a combination of the name of the {@link Feature} to which the
	 * {@link FeatureUser} belongs, and the user (email).
	 * 
	 * @author Shubham Sharma
	 *
	 */
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

	/**
	 * This class represents the JSON request for {@link FeatureUser}
	 * modification/creation. Structurally it mirrors the actual persistent (in
	 * database) {@link FeatureUser} entity.
	 * 
	 * @author Shubham Sharma
	 *
	 */
	@Data
	@AllArgsConstructor
	static public class FeatureUserRequest {
		String featureName;
		String email;
		boolean enable;
	}

	/**
	 * Creates a {@link FeatureUser} entity representation of a
	 * {@link FeatureUserRequest}.
	 * 
	 * @param featureUserRequest A {@link FeatureUserRequest} created from JSON
	 *                           request for {@link FeatureUser}
	 *                           modification/creation from user.
	 * @return {@link FeatureUser} entity representation of the
	 *         {@code featureUserRequest}.
	 * @throws InvalidInputException if either the feature name in the
	 *                               {@code featureUserRequest} is blank or if the
	 *                               email in the {@code featureUserRequest} is not
	 *                               a valid email address.
	 */
	public static FeatureUser fromRequest(FeatureUserRequest featureUserRequest) throws InvalidInputException {

		String validEmail = sanitizeEmailInput(featureUserRequest.getEmail());
		String validFeatureName = sanitizeFeatureNameInput(featureUserRequest.getFeatureName());

		FeatureUser featureUser = new FeatureUser(new FeatureUserId(validFeatureName, validEmail),
				new Feature(validFeatureName), featureUserRequest.isEnable());

		return featureUser;
	}
}
