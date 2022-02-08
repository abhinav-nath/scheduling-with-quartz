package com.codecafe.scheduling.repository;

import com.codecafe.scheduling.entity.SourceProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SourceProductsRepository extends JpaRepository<SourceProduct, Integer> {

    @Query(value = "SELECT * FROM source_products sp WHERE sp.created_at >= :lastSuccessfulJobRun", nativeQuery = true)
    List<SourceProduct> findByLastSuccessfulJobRun(Date lastSuccessfulJobRun);

}