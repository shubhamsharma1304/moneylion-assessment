package com.moneylion.evaluation.features.access.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moneylion.evaluation.features.access.model.Feature;

@Repository
public interface FeatureRepository extends JpaRepository<Feature, String> {
}
