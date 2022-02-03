package com.codecafe.scheduling.repository;

import com.codecafe.scheduling.entity.TargetProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TargetProductsRepository extends JpaRepository<TargetProduct, Integer> {

}