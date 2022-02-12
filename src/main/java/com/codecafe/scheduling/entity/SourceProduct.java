package com.codecafe.scheduling.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@Entity(name = "source_products")
public class SourceProduct {

    @Id
    private int id;

    private String name;
    private Date createdAt;
    private Date modifiedAt;
    private boolean isDeleted;

}
