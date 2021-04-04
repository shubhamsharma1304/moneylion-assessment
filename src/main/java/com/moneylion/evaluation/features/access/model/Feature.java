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

	@EqualsAndHashCode.Exclude
	@OneToMany(mappedBy = "id.featureName", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	Set<FeatureUser> featureUsers;
}