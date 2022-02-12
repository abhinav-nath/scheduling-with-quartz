package com.codecafe.scheduling.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "target_products")
public class TargetProduct {

    @Id
    private int id;

    private String name;
    private Date createdAt;
    private Date modifiedAt;

}
