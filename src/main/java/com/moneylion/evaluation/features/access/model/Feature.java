package com.moneylion.evaluation.features.access.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * This class models a record in the "feature" table. Which in turn represents a
 * real life feature of an applications. Each feature is identified by its
 * {@code name}, which is unique to that feature and is case-sensitive (meaning
 * a feature named "FeatureA" and a feature named "featureA" will be treated as
 * two different features).
 * 
 * 
 * @author Shubham Sharma
 */
@Data
@Builder
@EqualsAndHashCode
@Entity
@Table(name = "feature")
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Feature {

	@NonNull
	@Id
	private String name;

	/**
	 * A representation of the "One-to-many" relationship between the
	 * {@link Feature} entity and the {@link FeatureUser} entity.
	 */
	@EqualsAndHashCode.Exclude
	@OneToMany(mappedBy = "id.featureName", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	Set<FeatureUser> featureUsers;
}