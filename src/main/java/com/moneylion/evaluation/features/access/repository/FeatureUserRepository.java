package com.moneylion.evaluation.features.access.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moneylion.evaluation.features.access.model.FeatureUser;
import com.moneylion.evaluation.features.access.model.FeatureUser.FeatureUserId;

@Repository
public interface FeatureUserRepository extends JpaRepository<FeatureUser, FeatureUserId>{
}
