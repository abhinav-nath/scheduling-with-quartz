package com.codecafe.scheduling.repository;

import com.codecafe.scheduling.entity.SourceProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SourceProductsRepository extends JpaRepository<SourceProduct, Integer> {

}